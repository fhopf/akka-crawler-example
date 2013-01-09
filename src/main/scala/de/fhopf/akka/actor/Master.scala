package de.fhopf.akka.actor

import akka.actor.ActorRef
import akka.actor.UntypedActor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch
import de.fhopf.akka._

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
abstract class Master(latch: CountDownLatch) extends UntypedActor {

  private var logger: Logger = LoggerFactory.getLogger(classOf[Master])
  private var visitedPageStore: VisitedPageStore = new VisitedPageStore()

  override def onReceive(message: Any) = {

    message match {
      case start: String => {
        visitedPageStore.add(start)
        getParser().tell(visitedPageStore.getNext(), getSelf())
      }
      case content: PageContent => {
        getIndexer().tell(content, getSelf())
        visitedPageStore.addAll(content.getLinksToFollow())
        if (visitedPageStore.isFinished()) {
          getIndexer().tell(new COMMIT_MESSAGE(), getSelf())
        } else {
          // TODO
          //				for (String page : visitedPageStore.getNextBatch()) {
          //					getParser().tell(page, getSelf())
          //				}
        }

      }
      case indexedMessage: IndexedMessage => {
        visitedPageStore.finished(indexedMessage.getPath)
        if (visitedPageStore.isFinished())
          getIndexer().tell(new COMMIT_MESSAGE(), getSelf())

      }
      case _: COMMITTED_MESSAGE => {
        logger.info("Shutting down, finished")
        getContext().system.shutdown()
        latch.countDown()
      }
    }

  }

  protected def getIndexer(): ActorRef

  protected def getParser(): ActorRef
}
