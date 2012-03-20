/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhopf.akka;

/**
 *
 * @author flo
 */
public interface Indexer {

    void commit();

    void index(PageContent pageContent);
    
    void close();
}
