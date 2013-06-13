package org.xtendlang.example.akka

import java.io.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.MatchAllDocsQuery
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.perf4j.LoggingStopWatch
import org.slf4j.LoggerFactory

class Executor {
	val (String, IndexWriter)=>void execution
	static val logger = LoggerFactory.getLogger(Executor)

    new((String, IndexWriter)=>void execution) {
        this.execution = execution
    }

    def void execute(String path) {
            val indexDir = new File(System.getProperty("java.io.tmpdir"), "index-" + System.currentTimeMillis());
            indexDir.openWriter => [ writer |
            	try {
		            new LoggingStopWatch => [
		            	execution.apply(path, writer)
		            	stop(execution.class.simpleName)
		            ]
		        } catch(Exception e) {
		        	writer.rollback
		        	throw e
		        } finally {
		        	writer.close
		        }
            ]
            indexDir.openSearcher => [
            	val result = search(new MatchAllDocsQuery(), 100)
            	logger.info("Found {} results", result.totalHits)
            	for(scoreDoc: result.scoreDocs) {
	                val doc = doc(scoreDoc.doc)
	                logger.debug(doc.get("id"));
	            }
	            close
            ]
    }

    def private IndexWriter openWriter(File indexDir) {
        val dir = FSDirectory.open(indexDir);
        val config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
        return new IndexWriter(dir, config);
    }

    def private IndexSearcher openSearcher(File indexDir) {
        val dir = FSDirectory.open(indexDir);
        val reader = IndexReader.open(dir);
        return new IndexSearcher(reader);
    }
}