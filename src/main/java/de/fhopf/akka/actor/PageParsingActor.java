package de.fhopf.akka.actor;

import akka.actor.UntypedActor;
import de.fhopf.akka.PageContent;
import de.fhopf.akka.PageRetriever;

/**
 * Downloads pages and passes these on to the next actor.
 * @author Florian Hopf, http://www.florian-hopf.de 
 */
public class PageParsingActor extends UntypedActor {

    private final PageRetriever pageRetriever;
    
    public PageParsingActor(PageRetriever pageRetriever) {
        this.pageRetriever = pageRetriever;
    }
    
    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof String) {
            PageContent content = pageRetriever.fetchPageContent((String) o);
            getSender().tell(content, getSelf());
        } else {
            // fail on any message we don't expect
            unhandled(o);
        }
    }
}
