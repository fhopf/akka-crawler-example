/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
