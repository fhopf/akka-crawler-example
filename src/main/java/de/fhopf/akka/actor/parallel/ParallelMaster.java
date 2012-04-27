package de.fhopf.akka.actor.parallel;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;

import akka.routing.RoundRobinRouter;

import de.fhopf.akka.IndexerImpl;
import de.fhopf.akka.PageRetriever;
import de.fhopf.akka.actor.IndexingActor;
import de.fhopf.akka.actor.Master;
import de.fhopf.akka.actor.PageParsingActor;

import org.apache.lucene.index.IndexWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


/**
 * Fetches and parses pages in parallel.
 *
 * @author  flo
 */
class ParallelMaster extends Master {

    private final Logger logger = LoggerFactory.getLogger(ParallelMaster.class);

    private final ActorRef parser;
    private final ActorRef indexer;

    public ParallelMaster(final IndexWriter indexWriter, final PageRetriever pageRetriever,
        final CountDownLatch latch) {

        super(latch);
        parser = getContext().actorOf(new Props(new UntypedActorFactory() {

                        @Override
                        public Actor create() {

                            return new PageParsingActor(pageRetriever);
                        }
                    }).withRouter(new RoundRobinRouter(10)));
        indexer = getContext().actorOf(new Props(new UntypedActorFactory() {

                        @Override
                        public Actor create() {

                            return new IndexingActor(new IndexerImpl(indexWriter));
                        }
                    }));
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
