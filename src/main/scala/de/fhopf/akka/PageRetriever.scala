package de.fhopf.akka

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
abstract class PageRetriever {
  def fetchPageContent(url: String): PageContent
}
