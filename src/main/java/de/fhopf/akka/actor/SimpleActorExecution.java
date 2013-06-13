package de.fhopf.akka.actor;

import org.apache.lucene.index.IndexWriter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import de.fhopf.akka.Execution;
import de.fhopf.akka.Executor;
import de.fhopf.akka.HtmlParserPageRetriever;

/**
 * Runs the example with one actor of each.
 *
 * @author Florian Hopf, http://www.florian-hopf.de 
 */
public class SimpleActorExecution implements Execution {

    @Override
    public void downloadAndIndex(final String path, final IndexWriter writer) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef master = actorSystem.actorOf(Props.create(SimpleActorMaster.class, new HtmlParserPageRetriever(path), writer));

        master.tell(path, actorSystem.guardian());
        actorSystem.awaitTermination();
    }
    
    public static void main(String[] args) {
        SimpleActorExecution execution = new SimpleActorExecution();
        Executor exec = new Executor(execution);
        exec.execute("http://www.synyx.de/");
    }
}
