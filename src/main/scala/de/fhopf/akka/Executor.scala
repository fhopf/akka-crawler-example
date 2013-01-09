package de.fhopf.akka

import java.io.File
import java.io.IOException
import java.util.logging.Level
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.CorruptIndexException
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.MatchAllDocsQuery
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.LockObtainFailedException
import org.apache.lucene.util.Version
import org.perf4j.LoggingStopWatch
import org.perf4j.StopWatch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class Executor(execution: Execution) {

  private var logger: Logger = LoggerFactory.getLogger(classOf[Executor])

  def execute(path: String) = {
    var writer: IndexWriter = null
    var searcher: IndexSearcher = null
    try {
      var indexDir: File = new File(System.getProperty("java.io.tmpdir"), "index-" + System.currentTimeMillis())
      writer = openWriter(indexDir)

      val stopWatch: StopWatch = new LoggingStopWatch()
      execution.downloadAndIndex(path, writer)
      stopWatch.stop(execution.getClass().getSimpleName())

      searcher = openSearcher(indexDir)
      val result: TopDocs = searcher.search(new MatchAllDocsQuery(), 100)

      logger.info("Found {} results", result.totalHits)

      for (scoreDoc <- result.scoreDocs) {
        val doc: Document = searcher.doc(scoreDoc.doc)
        logger.debug(doc.get("id"))
      }

      searcher.close()

    } catch {
      case ex =>
        logger.error(ex.getMessage(), ex)
        if (writer != null) {
          try {
            writer.rollback()
          } catch {
            case ex1: IOException =>
              logger.error(ex1.getMessage(), ex1)
          }
        }
    } finally {
      if (writer != null) {
        try {
          writer.close()
        } catch {
          case ex: CorruptIndexException => logger.error(ex.getMessage(), ex)
          case ex: IOException => logger.error(ex.getMessage(), ex)
        }
      }
      if (searcher != null) {
        try {
          searcher.close()
        } catch {
          case ex: IOException => logger.error(ex.getMessage(), ex)
        }
      }
    }

  }

  private def openWriter(indexDir: File): IndexWriter = {
    val dir: Directory = FSDirectory.open(indexDir)
    val config: IndexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35))
    new IndexWriter(dir, config)
  }

  private def openSearcher(indexDir: File): IndexSearcher = {
    val dir: Directory = FSDirectory.open(indexDir)
    val reader: IndexReader = IndexReader.open(dir)
    new IndexSearcher(reader)
  }
}
