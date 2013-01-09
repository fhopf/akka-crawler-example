package de.fhopf.akka;

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
abstract class Indexer {
  def commit()
  def index(pageContent: PageContent)
  def close()
}
