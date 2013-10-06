package de.fhopf.akka.actor;

import akka.actor.UntypedActor;
import de.fhopf.akka.PageContent;
import de.fhopf.akka.PageRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

/**
 * Downloads pages and passes these on to the next actor.
 * @author Florian Hopf, http://www.florian-hopf.de 
 */
public class PageParsingActor extends UntypedActor {

    private final PageRetriever pageRetriever;
    private final Logger logger = LoggerFactory.getLogger(PageParsingActor.class);
    
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

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        logger.info("Restarting PageParsingActor because of {}", reason.getClass());
        super.preRestart(reason, message);
    }
    
    
}
