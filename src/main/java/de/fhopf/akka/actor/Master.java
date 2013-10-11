package de.fhopf.akka.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import de.fhopf.akka.PageContent;
import de.fhopf.akka.VisitedPageStore;


/**
 * Master actor that coordinates the two actors.
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public abstract class Master extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(Master.class);
    private final VisitedPageStore visitedPageStore = new VisitedPageStore();

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

            logger.info(visitedPageStore.toString());

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
        }
    }


    protected abstract ActorRef getIndexer();


    protected abstract ActorRef getParser();
}
