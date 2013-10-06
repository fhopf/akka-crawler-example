package de.fhopf.akka;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class that provides the index writer, logs the duration and checks the
 * document count. Concrete
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class Executor {

    private final Execution execution;
    private final Logger logger = LoggerFactory.getLogger(Executor.class);

    public Executor(Execution execution) {
        this.execution = execution;
    }

    public void execute(String path) {
        IndexWriter writer = null;
        IndexSearcher searcher = null;
        try {
            File indexDir = new File(System.getProperty("java.io.tmpdir"), "akka-index");
            writer = openWriter(indexDir);
            
            StopWatch stopWatch = new LoggingStopWatch();
            execution.downloadAndIndex(path, writer);
            stopWatch.stop(execution.getClass().getSimpleName());
            
            searcher = openSearcher(indexDir);
            TopDocs result = searcher.search(new MatchAllDocsQuery(), 100);

            logger.info("Found {} results", result.totalHits);
            
            for(ScoreDoc scoreDoc: result.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.debug(doc.get("id"));
            }

            searcher.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (writer != null) {
                try {
                    writer.rollback();
                } catch (IOException ex1) {
                    logger.error(ex1.getMessage(), ex1);
                }
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (CorruptIndexException ex) {
                    logger.error(ex.getMessage(), ex);
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }


    }

    private IndexWriter openWriter(File indexDir) throws CorruptIndexException, LockObtainFailedException, IOException {

        Directory dir = FSDirectory.open(indexDir);

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        return new IndexWriter(dir, config);
    }

    private IndexSearcher openSearcher(File indexDir) throws CorruptIndexException, IOException {

        Directory dir = FSDirectory.open(indexDir);
        IndexReader reader = IndexReader.open(dir);

        return new IndexSearcher(reader);
    }
}
