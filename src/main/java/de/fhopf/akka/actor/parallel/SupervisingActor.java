package de.fhopf.akka.actor.parallel;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;
import akka.routing.RoundRobinRouter;
import de.fhopf.akka.Indexer;
import de.fhopf.akka.PageRetriever;
import de.fhopf.akka.RetrievalException;
import de.fhopf.akka.actor.IndexingActor;
import de.fhopf.akka.actor.Master;
import scala.concurrent.duration.Duration;

/**
 * A parallel actor that also supervises its children.
 * @author Florian Hopf, www.florian-hopf.de
 */
class SupervisingActor extends Master {
    
    private final ActorRef pageParsingActor;
    private final ActorRef indexingActor;
    
    public SupervisingActor(final Indexer indexer, final PageRetriever pageRetriever) {
        this.pageParsingActor = getContext().actorOf(Props.create(ResendingPageParsingActor.class, pageRetriever)
                .withRouter(new RoundRobinRouter(10)).withDispatcher("worker-dispatcher"));
        this.indexingActor = getContext().actorOf(Props.create(IndexingActor.class, indexer));
    }
    
    // allow 100 restarts in 1 minute ... this is a lot but we the chaos monkey is rather busy
    private SupervisorStrategy supervisorStrategy = new OneForOneStrategy(100, Duration.create("1 minute"), new Function<Throwable, Directive>() {

        @Override
        public Directive apply(Throwable t) throws Exception {
            if (t instanceof RetrievalException) {
                return SupervisorStrategy.restart();
            } 
            // it would be best to model the default behaviour in other cases
            return SupervisorStrategy.escalate();
        }
        
    });

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return supervisorStrategy;
    }

    @Override
    protected ActorRef getParser() {
        return pageParsingActor;
    }

    @Override
    protected ActorRef getIndexer() {
        return indexingActor;
    }
}
