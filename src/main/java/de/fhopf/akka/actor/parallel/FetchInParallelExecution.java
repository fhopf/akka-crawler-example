package de.fhopf.akka.actor.parallel;

import akka.actor.*;
import de.fhopf.akka.Execution;
import de.fhopf.akka.Executor;
import de.fhopf.akka.HtmlParserPageRetriever;
import java.util.concurrent.CountDownLatch;
import org.apache.lucene.index.IndexWriter;

/**
 * Uses multiple actors for fetching and parsing pages.
 * @author flo
 */
public class FetchInParallelExecution implements Execution {

    @Override
    public void downloadAndIndex(final String path, final IndexWriter writer) {
        ActorSystem actorSystem = ActorSystem.create();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ActorRef master = actorSystem.actorOf(new Props(new UntypedActorFactory() {

            @Override
            public Actor create() {
                return new ParallelMaster(writer, new HtmlParserPageRetriever(path), countDownLatch);
            }
        }));
        master.tell(path);
        try {
            countDownLatch.await();
            actorSystem.shutdown();
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static void main(String[] args) {
        FetchInParallelExecution execution = new FetchInParallelExecution();
        Executor exec = new Executor(execution);
        exec.execute("http://www.synyx.de/");
    }
    
}
