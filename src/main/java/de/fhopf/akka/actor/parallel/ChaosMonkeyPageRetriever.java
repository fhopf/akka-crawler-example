package de.fhopf.akka.actor.parallel;

import de.fhopf.akka.HtmlParserPageRetriever;
import de.fhopf.akka.PageContent;
import de.fhopf.akka.RetrievalException;

/**
 * Randomly failing page retriever.
 * @author Florian Hopf, www.florian-hopf.de
 */
class ChaosMonkeyPageRetriever extends HtmlParserPageRetriever {
    
    public ChaosMonkeyPageRetriever(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public PageContent fetchPageContent(String url) {
        // this error rate is derived from scientific measurements
        if (System.currentTimeMillis() % 20 == 0) {
            throw new RetrievalException("Something went horribly wrong when fetching the page.");
        }
        return super.fetchPageContent(url);
    }
    
    
}
