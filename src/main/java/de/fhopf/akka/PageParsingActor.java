package de.fhopf.akka;

import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Downloads pages and passes these on to the next actor.
 * @author flo
 */
class PageParsingActor extends UntypedActor {

    private final HtmlParserPageRetriever pageRetriever;
    
    private final Logger logger = LoggerFactory.getLogger(PageParsingActor.class);
    
    public PageParsingActor(HtmlParserPageRetriever pageRetriever) {
        this.pageRetriever = pageRetriever;
    }
    
    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof String) {
            PageContent content = pageRetriever.fetchPageContent((String) o);
        } else {
            unhandled(o);
        }
    }
}
