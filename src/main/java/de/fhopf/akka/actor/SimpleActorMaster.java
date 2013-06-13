package de.fhopf.akka.actor;

import org.apache.lucene.index.IndexWriter;

import akka.actor.ActorRef;
import akka.actor.Props;
import de.fhopf.akka.IndexerImpl;
import de.fhopf.akka.PageRetriever;


/**
 * Works with one indexer and one page parser.
 * @author  Florian Hopf, http://www.florian-hopf.de
 */
class SimpleActorMaster extends Master {

    private final ActorRef indexer;
    private final ActorRef parser;

    public SimpleActorMaster(final PageRetriever pageRetriever, final IndexWriter indexWriter) {
        this.indexer = getContext().actorOf(Props.create(IndexingActor.class, new IndexerImpl(indexWriter)));
        this.parser = getContext().actorOf(Props.create(PageParsingActor.class, pageRetriever));
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
