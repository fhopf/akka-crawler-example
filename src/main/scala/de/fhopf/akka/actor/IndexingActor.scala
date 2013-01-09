package de.fhopf.akka.actor

import akka.actor.UntypedActor
import de.fhopf.akka.Indexer
import de.fhopf.akka.PageContent

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class IndexingActor(indexer: Indexer) extends UntypedActor {

  override def onReceive(message: Any) = {
    message match {
      case content: PageContent => {
        indexer.index(content)
        this.getSender().tell(new IndexedMessage(content.getPath()), this.getSelf())
      }
      case _: COMMIT_MESSAGE => {
        indexer.commit()
        this.getSender().tell(new COMMITTED_MESSAGE, this.getSelf())
      }
      case _ => {
        this.unhandled(message)
      }
    }
  }
}
