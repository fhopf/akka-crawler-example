package de.fhopf.akka.actor.parallel;

import de.fhopf.akka.PageRetriever;
import de.fhopf.akka.actor.PageParsingActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

/**
 * Resends the failing message on restart. Careful: This can lead to livelock.
 * @author Florian Hopf, www.florian-hopf.de
 */
public class ResendingPageParsingActor extends PageParsingActor {
    
    private final Logger logger = LoggerFactory.getLogger(ResendingPageParsingActor.class);
    
    public ResendingPageParsingActor(PageRetriever pageRetriever) {
        super(pageRetriever);
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        logger.info("Restarting PageParsingActor and resending message '{}'", message);
        if (message.nonEmpty()) {
            getSelf().forward(message.get(), getContext());
        }
        super.preRestart(reason, message);
    }
    
    
    
}
