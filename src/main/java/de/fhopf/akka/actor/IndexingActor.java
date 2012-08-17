package de.fhopf.akka.actor;

import akka.actor.UntypedActor;
import de.fhopf.akka.Indexer;
import de.fhopf.akka.PageContent;

/**
 * Indexes pages in Lucene.
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class IndexingActor extends UntypedActor {

    public static final Object COMMIT_MESSAGE = new Object();
    public static final Object COMMITTED_MESSAGE = new Object();
    
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
