package de.fhopf.akka;

import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public interface Execution {

    public void downloadAndIndex(String path, IndexWriter writer);
    
}
