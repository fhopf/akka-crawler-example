package de.fhopf.akka
import java.util.ArrayList
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index._
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.util.Version
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class IndexerImplTest {

  @Test
  def pageContentIsFoundAfterCommit() = {

    val index: Directory = new RAMDirectory()
    val config: IndexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35))
    val writer: IndexWriter = new IndexWriter(index, config)
    val indexerImpl: IndexerImpl = new IndexerImpl(writer)
    val content: PageContent = new PageContent("http://path", new ArrayList[String](), "This is the title", "This is the content")
    indexerImpl.index(content)
    indexerImpl.commit()
    val reader: IndexReader = IndexReader.open(index)
    val searcher: IndexSearcher = new IndexSearcher(reader)
    val query: TermQuery = new TermQuery(new Term("content", "content"))
    val result: TopDocs = searcher.search(query, 10)
    assertEquals(1, result.totalHits)

  }

}
