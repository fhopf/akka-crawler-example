package de.fhopf.akka;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores the information on which pages have already been retrieved.
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class VisitedPageStore {

    private Set<String> pagesToVisit = new HashSet<String>();
    private Set<String> allPages = new HashSet<String>();
    private Set<String> inProgress = new HashSet<String>();
    
    public void add(String page) {
        if (!allPages.contains(page)) {
            pagesToVisit.add(page);
            allPages.add(page);
        }
    }
    
    public void addAll(Collection<String> pages) {
        for (String page: pages) {
            add(page);
        }
    }
    
    public void finished(String page) {
        inProgress.remove(page);
    }
    
    public String getNext() {
        if (pagesToVisit.isEmpty()) {
            return null;
        } else {
            String next = pagesToVisit.iterator().next();
            pagesToVisit.remove(next);
            inProgress.add(next);
            return next;
        }
    }
    
    public Collection<String> getNextBatch() {
        Set<String> pages = new HashSet<String>();
        pages.addAll(pagesToVisit);
        pagesToVisit.clear();
        inProgress.addAll(pages);
        return pages;
    }
    
    public boolean isFinished() {
        return pagesToVisit.isEmpty() && inProgress.isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("inProgress: %1$3s, allPages: %2$3s", inProgress.size(), allPages.size());
    }

}
