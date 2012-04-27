package de.fhopf.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import de.fhopf.akka.PageContent;
import de.fhopf.akka.VisitedPageStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


/**
 * Master actor that coordinates the two actors.
 *
 * @author  flo
 */
public abstract class Master extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(Master.class);
    private final VisitedPageStore visitedPageStore = new VisitedPageStore();
    private final CountDownLatch countDownLatch;

    protected Master(final CountDownLatch latch) {

        this.countDownLatch = latch;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            // start
            String start = (String) message;
            visitedPageStore.add(start);
            getParser().tell(visitedPageStore.getNext(), getSelf());
        } else if (message instanceof PageContent) {
            PageContent content = (PageContent) message;
            getIndexer().tell(content, getSelf());
            visitedPageStore.addAll(content.getLinksToFollow());

            if (visitedPageStore.isFinished()) {
                getIndexer().tell(IndexingActor.COMMIT_MESSAGE, getSelf());
            } else {
                for (String page : visitedPageStore.getNextBatch()) {
                    getParser().tell(page, getSelf());
                }
            }
        } else if (message instanceof IndexedMessage) {
            IndexedMessage indexedMessage = (IndexedMessage) message;
            visitedPageStore.finished(indexedMessage.path);

            if (visitedPageStore.isFinished()) {
                getIndexer().tell(IndexingActor.COMMIT_MESSAGE, getSelf());
            }
        } else if (message == IndexingActor.COMMITTED_MESSAGE) {
            logger.info("Shutting down, finished");
            getContext().system().shutdown();
            countDownLatch.countDown();
        }
    }


    protected abstract ActorRef getIndexer();


    protected abstract ActorRef getParser();
}
