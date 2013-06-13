package org.xtendlang.example.akka

import org.apache.lucene.index.IndexWriter
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field

interface Indexer {
	def void commit();

    def void index(PageContent pageContent);
    
    def void close();
}

class IndexerImpl implements Indexer {
	val IndexWriter indexWriter;

    new(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }
    
    override index(PageContent pageContent) {
        indexWriter.addDocument(pageContent.toDocument);
    }
    
    def private toDocument(PageContent page) {
        return new Document => [
        	add(new Field("id", page.path?:'', Field.Store.YES, Field.Index.NOT_ANALYZED));
        	add(new Field("title", page.path?:'', Field.Store.YES, Field.Index.ANALYZED));
        	add(new Field("content", page.content?:'', Field.Store.NO, Field.Index.ANALYZED));	
        ]
    }
    
    override void commit() {
        indexWriter.commit();
    }

    override close() {
        indexWriter.close(true)
    }
}