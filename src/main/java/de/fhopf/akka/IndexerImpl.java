package de.fhopf.akka;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

/**
 * Indexer Impl, contaijns writer state.
 * @author flo
 */
class IndexerImpl {
    
    private final IndexWriter indexWriter;

    public IndexerImpl(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }
    
    public void index(PageContent pageContent) {
        try {
            indexWriter.addDocument(toDocument(pageContent));
        } catch (CorruptIndexException ex) {
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private Document toDocument(PageContent content) {
        Document doc = new Document();
        doc.add(new Field("title", content.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("content", content.getContent(), Field.Store.NO, Field.Index.ANALYZED));
        return doc;
    }
    
    public void commit() {
        try {
            indexWriter.commit();
        } catch (CorruptIndexException ex) {
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
}
