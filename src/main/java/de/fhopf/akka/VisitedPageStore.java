package de.fhopf.akka;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores the information on which pages have already been retrieved.
 * @author flo
 */
public class VisitedPageStore {

    private Set<String> pagesToVisit = new HashSet<String>();
    private Set<String> allPages = new HashSet<String>();
    
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
        pagesToVisit.remove(page);
    }
    
    public String getNext() {
        if (pagesToVisit.isEmpty()) {
            return null;
        } else {
            return pagesToVisit.iterator().next();
        }
    }
    
}
