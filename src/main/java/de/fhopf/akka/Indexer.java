package de.fhopf.akka;

/**
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public interface Indexer {

    void commit();

    void index(PageContent pageContent);
    
    void close();
}
