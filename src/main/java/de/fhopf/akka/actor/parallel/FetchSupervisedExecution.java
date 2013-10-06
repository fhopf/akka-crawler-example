package de.fhopf.akka.actor.parallel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import de.fhopf.akka.Execution;
import de.fhopf.akka.Executor;
import de.fhopf.akka.IndexerImpl;
import org.apache.lucene.index.IndexWriter;

/**
 * Runs the example with a supervisor.
 * @author Florian Hopf, www.florian-hopf.de
 */
public class FetchSupervisedExecution implements Execution {

    @Override
    public void downloadAndIndex(String path, IndexWriter writer) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef master = actorSystem.actorOf(Props.create(SupervisingActor.class, new IndexerImpl(writer), new ChaosMonkeyPageRetriever(path)));
        master.tell(path, actorSystem.guardian());
        actorSystem.awaitTermination();
    }

    public static void main(String[] args) {
        FetchSupervisedExecution execution = new FetchSupervisedExecution();
        Executor exec = new Executor(execution);
        exec.execute("http://www.synyx.de/");
    }
    
}
