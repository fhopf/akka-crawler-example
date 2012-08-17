package de.fhopf.akka;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class IndexerImplTest {

    @Test
    public void pageContentIsFoundAfterCommit() throws CorruptIndexException, LockObtainFailedException, IOException {
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
        IndexWriter writer = new IndexWriter(index, config);
        IndexerImpl indexerImpl = new IndexerImpl(writer);
        PageContent content = new PageContent("http://path", new ArrayList<String>(), "This is the title", "This is the content");
        indexerImpl.index(content);
        indexerImpl.commit();
        
        IndexReader reader = IndexReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        TermQuery query = new TermQuery(new Term("content", "content"));
        TopDocs result = searcher.search(query, 10);
        
        assertEquals(1, result.totalHits);
    }
    
}
