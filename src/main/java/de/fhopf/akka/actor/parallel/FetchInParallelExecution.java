package de.fhopf.akka.actor.parallel;

import org.apache.lucene.index.IndexWriter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import de.fhopf.akka.Execution;
import de.fhopf.akka.Executor;
import de.fhopf.akka.HtmlParserPageRetriever;
import de.fhopf.akka.IndexerImpl;

/**
 * Uses multiple actors for fetching and parsing pages.
 * 
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class FetchInParallelExecution implements Execution {

	@Override
	public void downloadAndIndex(final String path, final IndexWriter writer) {
		ActorSystem actorSystem = ActorSystem.create();
		ActorRef master = actorSystem.actorOf(Props.create(ParallelMaster.class, new IndexerImpl(writer), new HtmlParserPageRetriever(path)));
		master.tell(path, actorSystem.guardian());
		actorSystem.awaitTermination();
	}

	public static void main(String[] args) {
		FetchInParallelExecution execution = new FetchInParallelExecution();
		Executor exec = new Executor(execution);
		exec.execute("http://www.synyx.de/");
	}

}
