/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhopf.akka;

/**
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public interface PageRetriever {

    PageContent fetchPageContent(String url);
    
}
