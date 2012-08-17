package de.fhopf.akka.sequential;

import de.fhopf.akka.*;
import de.fhopf.akka.Execution;
import de.fhopf.akka.Executor;
import org.apache.lucene.index.IndexWriter;

/**
 * Indexes pages sequentially.
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class SequentialExecution implements Execution {

    @Override
    public void downloadAndIndex(String path, IndexWriter writer) {
        VisitedPageStore pageStore = new VisitedPageStore();
        pageStore.add(path);
        
        Indexer indexer = new IndexerImpl(writer);
        PageRetriever retriever = new HtmlParserPageRetriever(path);
        
        String page;
        while ((page = pageStore.getNext()) != null) {
            PageContent pageContent = retriever.fetchPageContent(page);
            pageStore.addAll(pageContent.getLinksToFollow());
            indexer.index(pageContent);
            pageStore.finished(page);
        }
        
        indexer.commit();
    }
    
    public static void main(String[] args) {
        SequentialExecution execution = new SequentialExecution();
        Executor exec = new Executor(execution);
        exec.execute("http://www.synyx.de/");
    }
    
}
