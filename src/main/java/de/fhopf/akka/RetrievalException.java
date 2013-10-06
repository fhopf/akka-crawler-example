package de.fhopf.akka;

/**
 * Indicates a failing retrieval of a url.
 * @author Florian Hopf, www.florian-hopf.de
 */
public class RetrievalException extends RuntimeException {
    
    public RetrievalException(String message) {
        super(message);
    }
    
}
