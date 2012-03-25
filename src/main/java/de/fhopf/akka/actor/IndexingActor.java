package de.fhopf.akka.actor;

import akka.actor.UntypedActor;
import de.fhopf.akka.Indexer;
import de.fhopf.akka.PageContent;

/**
 *
 * @author flo
 */
class IndexingActor extends UntypedActor {

    public static final String COMMIT_MESSAGE = "COMMIT";
    public static final String COMMITTED_MESSAGE = "COMMITTED";
    
    private final Indexer indexer;

    public IndexingActor(Indexer indexer) {
        this.indexer = indexer;
    }
    
    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof PageContent) {
            PageContent content = (PageContent) o;
            indexer.index(content);
            getSender().tell(new IndexedMessage(content.getPath()), getSelf());
        } else if (COMMIT_MESSAGE == o) {
            indexer.commit();
            getSender().tell(COMMITTED_MESSAGE, getSelf());
        } else {
            unhandled(o);
        }
    }
    
}
