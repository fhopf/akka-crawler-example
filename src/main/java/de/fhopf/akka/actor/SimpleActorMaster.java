package de.fhopf.akka.actor;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;

import de.fhopf.akka.IndexerImpl;
import de.fhopf.akka.PageRetriever;

import org.apache.lucene.index.IndexWriter;

import java.util.concurrent.CountDownLatch;


/**
 * Works with one indexer and one page parser.
 * @author  Florian Hopf, http://www.florian-hopf.de
 */
class SimpleActorMaster extends Master {

    private final ActorRef indexer;
    private final ActorRef parser;

    public SimpleActorMaster(final PageRetriever pageRetriever, final IndexWriter indexWriter,
        final CountDownLatch latch) {

        super(latch);
        this.indexer = getContext().actorOf(new Props(new UntypedActorFactory() {

                        @Override
                        public Actor create() {

                            return new IndexingActor(new IndexerImpl(indexWriter));
                        }
                    }));

        this.parser = getContext().actorOf(new Props(new UntypedActorFactory() {

                        @Override
                        public Actor create() {

                            return new PageParsingActor(pageRetriever);
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
