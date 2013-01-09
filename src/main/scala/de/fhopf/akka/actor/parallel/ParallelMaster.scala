package de.fhopf.akka.actor.parallel

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActorFactory

import akka.routing.RoundRobinRouter

import de.fhopf.akka.IndexerImpl
import de.fhopf.akka.PageRetriever
import de.fhopf.akka.actor.IndexingActor
import de.fhopf.akka.actor.Master
import de.fhopf.akka.actor.PageParsingActor

import org.apache.lucene.index.IndexWriter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.CountDownLatch

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class ParallelMaster(latch: CountDownLatch) extends Master(latch) {

  private val logger: Logger = LoggerFactory.getLogger(classOf[ParallelMaster])

  private var parser: ActorRef = null
  private var indexer: ActorRef = null

  def this(indexWriter: IndexWriter, pageRetriever: PageRetriever, latch: CountDownLatch) = {

    this(latch)
    parser = getContext().actorOf(new Props(new UntypedActorFactory() {
      override def create(): Actor = new PageParsingActor(pageRetriever)
    }).withRouter(new RoundRobinRouter(10)))
    indexer = getContext().actorOf(new Props(new UntypedActorFactory() {
      override def create(): Actor = new IndexingActor(new IndexerImpl(indexWriter))
    }))
  }

  protected override def getIndexer(): ActorRef = indexer

  protected override def getParser(): ActorRef = return parser

}
