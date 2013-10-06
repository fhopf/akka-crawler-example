package de.fhopf.akka.actor.parallel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import de.fhopf.akka.Execution;
import de.fhopf.akka.Executor;
import de.fhopf.akka.IndexerImpl;
import org.apache.lucene.index.IndexWriter;

/**
 * This execution sometimes fails and will just stop because it is waiting on failed messages.
 * @author Florian Hopf, www.florian-hopf.de
 */
public class FailingExecution implements Execution {

    @Override
    public void downloadAndIndex(String path, IndexWriter writer) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef master = actorSystem.actorOf(Props.create(ParallelMaster.class, new IndexerImpl(writer), new ChaosMonkeyPageRetriever(path)));
        master.tell(path, actorSystem.guardian());
        actorSystem.awaitTermination();
    }
    
    public static void main(String [] args) {
        Execution execution = new FailingExecution();
        Executor executor = new Executor(execution);
        executor.execute("http://www.synyx.de");
    }
    
}
