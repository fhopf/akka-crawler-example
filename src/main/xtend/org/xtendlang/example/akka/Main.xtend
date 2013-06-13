package org.xtendlang.example.akka

import static extension akka.actor.Props.*
import akka.actor.ActorSystem

class ParallelMain {
	
	def static void main(String[] args) {
		new Executor [ path, writer |
			val it = ActorSystem.create
			val master = actorOf(ParallelMaster.create(writer, new HtmlParserPageRetriever(path)))
			master.tell(path, guardian());
			awaitTermination();
		].execute("http://www.eclipse.org/xtend/")
	}
	
}

class SequentialAkkaMain {
	
	def static void main(String[] args) {
		new Executor [ path, writer |
			val it = ActorSystem.create
			val master = actorOf(SimpleMaster.create(writer, new HtmlParserPageRetriever(path)))
			master.tell(path, guardian());
			awaitTermination();
		].execute("http://www.eclipse.org/xtend/")
	}
	
}

class SequentialMain {
	def static void main(String[] args) {
		new Executor [ path, writer |
			val pageStore = new VisitedPageStore();
        	pageStore.add(path);
        
        	val indexer = new IndexerImpl(writer);
       	 	val retriever = new HtmlParserPageRetriever(path);
        
	        var String page;
	        while ((page = pageStore.getNext()) != null) {
	            val pageContent = retriever.fetchPageContent(page);
	            pageStore.addAll(pageContent.getLinksToFollow());
	            indexer.index(pageContent);
	            pageStore.finished(page);
	        }
	        
	        indexer.commit();
		].execute("http://www.eclipse.org/xtend/")
	}
	
}