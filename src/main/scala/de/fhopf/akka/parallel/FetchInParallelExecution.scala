package de.fhopf.akka.actor.parallel

import akka.actor._
import de.fhopf.akka.Execution
import de.fhopf.akka.Executor
import de.fhopf.akka.HtmlParserPageRetriever
import java.util.concurrent.CountDownLatch
import org.apache.lucene.index.IndexWriter

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class FetchInParallelExecution extends Execution {

  override def downloadAndIndex(path: String, writer: IndexWriter) = {
    val actorSystem: ActorSystem = ActorSystem.create()
    val countDownLatch: CountDownLatch = new CountDownLatch(1)
    val master: ActorRef = actorSystem.actorOf(new Props(new UntypedActorFactory() {
      override def create(): Actor = new ParallelMaster(writer, new HtmlParserPageRetriever(path), countDownLatch)
    }))
    master.tell(path)
    try {
      countDownLatch.await()
      actorSystem.shutdown()
    } catch {
      case ex: InterruptedException => throw new IllegalStateException(ex)
    }
  }

  def main(args: Array[String]) {
    val execution: FetchInParallelExecution = new FetchInParallelExecution()
    val exec: Executor = new Executor(execution)
    exec.execute("http://www.synyx.de/")
  }

}
