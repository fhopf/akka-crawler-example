package de.fhopf.akka

import org.junit.Test

import org.junit.Assert.assertNotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class HtmlParserPageRetrieverTest {

  private val pageRetriever: HtmlParserPageRetriever = new HtmlParserPageRetriever("http://www.synyx.de")
  private val logger: Logger = LoggerFactory.getLogger(classOf[HtmlParserPageRetrieverTest])

  @Test def contentIsExtracted() {
    val content: PageContent = pageRetriever.fetchPageContent("http://www.synyx.de/unternehmen/")
    assertNotNull(content)
    assertNotNull(content.getContent())
    assertNotNull(content.getTitle())
    logger.info(content.toString())
    println(content)
  }

}
