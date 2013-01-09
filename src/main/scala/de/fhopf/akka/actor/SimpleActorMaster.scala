package de.fhopf.akka.actor

import java.util.concurrent.CountDownLatch
import akka.actor.ActorRef
import de.fhopf.akka.PageRetriever
import org.apache.lucene.index.IndexWriter
import akka.actor.Props
import akka.actor.UntypedActorFactory
import scala.actors.Actor
import de.fhopf.akka.IndexerImpl

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 * @param pageRetriever
 * @param indexWriter
 * @param latch
 */
class SimpleActorMaster(latch: CountDownLatch) extends Master(latch) {

  private var indexer: ActorRef = null
  private var parser: ActorRef = null

  def this() { this(null) }

  def this(pageRetriever: PageRetriever, indexWriter: IndexWriter, latch: CountDownLatch) = {

    this(latch)

    this.indexer = getContext().actorOf(new Props(new UntypedActorFactory() {
      override def create() = new IndexingActor(new IndexerImpl(indexWriter))
    }))

    this.parser = getContext().actorOf(new Props(new UntypedActorFactory() {
      override def create() = new PageParsingActor(pageRetriever)
    }))

  }

  protected def getIndexer(): ActorRef = indexer

  protected def getParser(): ActorRef = parser

  //    def onReceive(arg0: Object): Unit = {  }

}