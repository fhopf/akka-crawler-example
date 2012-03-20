package de.fhopf.akka;

import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author flo
 */
public interface Execution {

    public void downloadAndIndex(String path, IndexWriter writer);
    
}
