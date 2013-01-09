package de.fhopf.akka

import java.io.IOException
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.CorruptIndexException
import org.apache.lucene.index.IndexWriter

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class IndexerImpl(indexWriter: IndexWriter) extends Indexer {

  override def index(pageContent: PageContent) = {
    try {
      indexWriter.addDocument(toDocument(pageContent))
    } catch {
      case ex: CorruptIndexException => throw new IllegalStateException(ex)
      case ex: IOException => throw new IllegalStateException(ex)
    }
  }

  private def toDocument(content: PageContent): Document = {
    val doc: Document = new Document()
    doc.add(new Field("id", content.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED))
    doc.add(new Field("title", content.getTitle(), Field.Store.YES, Field.Index.ANALYZED))
    doc.add(new Field("content", content.getContent(), Field.Store.NO, Field.Index.ANALYZED))
    doc
  }

  override def commit() {
    try {
      indexWriter.commit()
    } catch {
      case ex: CorruptIndexException => throw new IllegalStateException(ex)
      case ex: IOException => throw new IllegalStateException(ex)
    }
  }

  override def close() {
    try {
      indexWriter.close(true)
    } catch {
      case ex: CorruptIndexException => throw new IllegalStateException(ex)
      case ex: IOException => throw new IllegalStateException(ex)
    }
  }

}
