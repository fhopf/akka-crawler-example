package org.xtendlang.example.akka

import akka.actor.UntypedActor
import akka.actor.ActorRef
import org.apache.lucene.index.IndexWriter
import static extension akka.actor.Props.*
import akka.routing.RoundRobinRouter

@Data
class IndexingActor extends UntypedActor {
	
	val extension Indexer indexer
	
	def dispatch onReceive(Object it) throws Exception {
		unhandled
	}
	def dispatch onReceive(PageContent it) throws Exception {
		index
		sender.tell(new IndexedMessage(path), self)
	}
	def dispatch onReceive(CommitMessage it) throws Exception {
		commit
		sender.tell(new CommittedMessage, self)
	}
}

@Data
class PageParsingActor extends UntypedActor {
	val extension PageRetriever pageRetriever
	
	override onReceive(Object it) throws Exception {
		switch(it) {
			String: sender.tell(fetchPageContent, self)
            default: unhandled
        }
	}
}

abstract class Master extends UntypedActor {

    private final VisitedPageStore visitedPageStore = new VisitedPageStore();

    override onReceive(Object it) throws Exception {
    	switch it {
    		String: {
    			visitedPageStore.add(it)
    			parser.tell(visitedPageStore.next, self)
    		}
    		PageContent: {
    			indexer.tell(it, self)
    			visitedPageStore.addAll(linksToFollow)
    			if (visitedPageStore.finished) {
    				indexer.tell(new CommitMessage, self)
    			} else {
    				visitedPageStore.nextBatch.forEach [
    					parser.tell(it, getSelf)
    				]
    			}
    		}
    		IndexedMessage: {
    			visitedPageStore.finished(path)
    			if (visitedPageStore.finished) {
    				indexer.tell(new CommitMessage, self)
    			}
    		}
    		CommittedMessage: {
    			context.system.shutdown
    		}
    		default:
    			unhandled
    	}
    }

    def protected abstract ActorRef getIndexer();
    def protected abstract ActorRef getParser();
}

class SimpleMaster extends Master {
	
	val ActorRef parser
	val ActorRef indexer
	
	new(IndexWriter writer, PageRetriever pageRetriever) {
		parser = context.actorOf(PageParsingActor.create(pageRetriever))
		indexer = context.actorOf(IndexingActor.create(new IndexerImpl(writer)))
	}
	
	override protected getIndexer() {
		return indexer
	}
	
	override protected getParser() {
		return parser
	}
	
}

class ParallelMaster extends Master {
	
	val ActorRef parser
	val ActorRef indexer
	
	new(IndexWriter writer, PageRetriever pageRetriever) {
		parser = context.actorOf(PageParsingActor.create(pageRetriever).withRouter(new RoundRobinRouter(10)))
		indexer = context.actorOf(IndexingActor.create(new IndexerImpl(writer)))
	}
	
	override protected getIndexer() {
		return indexer
	}
	
	override protected getParser() {
		return parser
	}
	
}