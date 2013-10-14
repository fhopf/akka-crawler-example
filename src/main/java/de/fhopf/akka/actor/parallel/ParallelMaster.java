package de.fhopf.akka.actor.parallel;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;
import de.fhopf.akka.Indexer;
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
    private final ActorRef indexingActor;

    public ParallelMaster(final Indexer indexer, final PageRetriever pageRetriever) {
        parser = getContext().actorOf(Props.create(PageParsingActor.class, pageRetriever)
                .withRouter(new RoundRobinRouter(10)).withDispatcher("worker-dispatcher"));
        indexingActor = getContext().actorOf(Props.create(IndexingActor.class, indexer));
    }

    @Override
    protected ActorRef getIndexer() {
        return indexingActor;
    }

    @Override
    protected ActorRef getParser() {
        return parser;
    }
}
