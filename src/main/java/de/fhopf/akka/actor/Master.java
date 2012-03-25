package de.fhopf.akka.actor;

import akka.actor.*;
import de.fhopf.akka.IndexerImpl;
import de.fhopf.akka.PageContent;
import de.fhopf.akka.PageRetriever;
import de.fhopf.akka.VisitedPageStore;
import java.util.concurrent.CountDownLatch;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Master actor that coordinates the two actors.
 * @author flo
 */
public class Master extends UntypedActor{

    private final Logger logger = LoggerFactory.getLogger(Master.class);
    
    private ActorRef parser;
    private ActorRef indexer;
    
    private VisitedPageStore visitedPageStore = new VisitedPageStore();
    
    private CountDownLatch countDownLatch;
    
    public Master(final IndexWriter indexWriter, final PageRetriever pageRetriever, final CountDownLatch latch) {
        parser = getContext().actorOf(new Props(new UntypedActorFactory() {

            @Override
            public Actor create() {
                return new PageParsingActor(pageRetriever);
            }
        }));
        indexer = getContext().actorOf(new Props(new UntypedActorFactory() {

            @Override
            public Actor create() {
                return new IndexingActor(new IndexerImpl(indexWriter));
            }
        }));
        this.countDownLatch = latch;
    }
    
    @Override
    public void onReceive(Object message) throws Exception {
        handleMessage(parser, indexer, message);
    }

    protected void handleMessage(ActorRef parser, ActorRef indexer, Object message) {
        //logger.debug("Received message {}", message);
        if (message == IndexingActor.COMMITTED_MESSAGE) {
            logger.info("Shutting down, finished");
            countDownLatch.countDown();
        } else if (message instanceof IndexedMessage) {
            IndexedMessage indexedMessage = (IndexedMessage) message;
            visitedPageStore.finished(indexedMessage.path);
            if (visitedPageStore.isFinished()) {
                indexer.tell(IndexingActor.COMMIT_MESSAGE, getSelf());
            } 
        } else if (message instanceof String) {
            // start
            String start = (String) message;
            visitedPageStore.add(start);
            parser.tell(visitedPageStore.getNext(), getSelf());
        } else if (message instanceof PageContent) {
            PageContent content = (PageContent) message;
            indexer.tell(content, getSelf());
            visitedPageStore.addAll(content.getLinksToFollow());
            if (visitedPageStore.isFinished()) {
                indexer.tell(IndexingActor.COMMIT_MESSAGE, getSelf());
            } else {
                for (String page: visitedPageStore.getNextBatch()) {
                    parser.tell(page, getSelf());
                }
            }
        } 
    }
    
}
