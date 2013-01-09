package de.fhopf.akka.sequential

import de.fhopf.akka._
import de.fhopf.akka.Execution
import de.fhopf.akka.Executor
import org.apache.lucene.index.IndexWriter

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class SequentialExecution extends Execution {

  override def downloadAndIndex(path: String, writer: IndexWriter) = {

    val pageStore: VisitedPageStore = new VisitedPageStore()
    pageStore.add(path)
    val indexer: Indexer = new IndexerImpl(writer)
    val retriever: PageRetriever = new HtmlParserPageRetriever(path)

    var page = ""
    // TODO: check the correct values here
    while ((page = pageStore.getNext()) != None) {
      val pageContent: PageContent = retriever.fetchPageContent(page)
      pageStore.addAll(pageContent.getLinksToFollow())
      indexer.index(pageContent)
      pageStore.finished(page)
    }

    indexer.commit()
  }

  def main(args: Array[String]) {
    val execution: SequentialExecution = new SequentialExecution()
    val exec: Executor = new Executor(execution)
    exec.execute("http://www.synyx.de/")
  }

}
