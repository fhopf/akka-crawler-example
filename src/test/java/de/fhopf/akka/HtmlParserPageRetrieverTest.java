package de.fhopf.akka;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Florian Hopf
 */
public class HtmlParserPageRetrieverTest {
    
    private HtmlParserPageRetriever pageRetriever = new HtmlParserPageRetriever("http://www.synyx.de");
    
    private final Logger logger = LoggerFactory.getLogger(HtmlParserPageRetrieverTest.class);
    
    @Test
    public void contentIsExtracted() {
        PageContent content = pageRetriever.fetchPageContent("http://www.synyx.de/unternehmen/");
        
        assertNotNull(content);
        assertNotNull(content.getContent());
        assertNotNull(content.getTitle());
        
        logger.info(content.toString());
        //System.out.println(content);
    }
    
}
