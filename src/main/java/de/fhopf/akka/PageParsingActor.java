package de.fhopf.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.ObjectFindingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Downloads pages and passes these on to the next actor.
 * @author flo
 */
public class PageParsingActor extends UntypedActor {

    private final String baseUrl;
    private final ActorRef nextActor;
    
    private final Logger logger = LoggerFactory.getLogger(PageParsingActor.class);
    
    public PageParsingActor(String baseUrl, ActorRef nextActor) {
        this.baseUrl = baseUrl;
        this.nextActor = nextActor;
    }
    
    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof String) {
            // TODO handle Exception
            retrieveContent((String) o);
        }
    }

    private String retrieveContent(String url) throws ParserException {
        
        return "";
    }
    
}
