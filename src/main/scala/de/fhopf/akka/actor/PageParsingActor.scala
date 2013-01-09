package de.fhopf.akka.actor

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import akka.actor.UntypedActor
import akka.actor.actorRef2Scala
import de.fhopf.akka.PageContent
import de.fhopf.akka.PageRetriever

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class PageParsingActor(pageRetriever: PageRetriever) extends UntypedActor {

  private val logger: Logger = LoggerFactory.getLogger(classOf[PageParsingActor])

  override def onReceive(message: Any) = {
    message match {
      case msg: String => {
        val content: PageContent = pageRetriever.fetchPageContent(msg)
        sender ! (content, self)
      }
      case _ => unhandled(message)
    }
  }
}
