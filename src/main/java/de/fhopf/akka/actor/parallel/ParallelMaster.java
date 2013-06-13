package de.fhopf.akka.actor.parallel;

import org.apache.lucene.index.IndexWriter;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;
import de.fhopf.akka.IndexerImpl;
import de.fhopf.akka.PageRetriever;
import de.fhopf.akka.actor.IndexingActor;
import de.fhopf.akka.actor.Master;
import de.fhopf.akka.actor.PageParsingActor;

/**
 * Fetches and parses pages in parallel.
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
class ParallelMaster extends Master {

    private final ActorRef parser;
    private final ActorRef indexer;

    public ParallelMaster(final IndexWriter indexWriter, final PageRetriever pageRetriever) {
        parser = getContext().actorOf(Props.create(PageParsingActor.class, pageRetriever).withRouter(new RoundRobinRouter(10)));
        indexer = getContext().actorOf(Props.create(IndexingActor.class, new IndexerImpl(indexWriter)));
    }

    @Override
    protected ActorRef getIndexer() {
        return indexer;
    }

    @Override
    protected ActorRef getParser() {
        return parser;
    }
}
