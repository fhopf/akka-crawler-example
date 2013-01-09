package de.fhopf.akka

import org.apache.lucene.index.IndexWriter

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
abstract class Execution {
  def downloadAndIndex(path: String, writer: IndexWriter)
}