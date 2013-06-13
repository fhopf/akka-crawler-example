package org.xtendlang.example.akka

import java.util.Collection

class VisitedPageStore {
	val pagesToVisit = <String>newHashSet
    val allPages = <String>newHashSet
    val inProgress = <String>newHashSet
    
    def void add(String page) {
        if (!allPages.contains(page)) {
            pagesToVisit.add(page);
            allPages.add(page);
        }
    }
    
    def addAll(Collection<String> pages) {
    	pages.forEach [ add ]
    }
    
    def void finished(String page) {
        inProgress.remove(page);
    }
    
    def getNext() {
        if (pagesToVisit.isEmpty()) {
            return null;
        } else {
            val next = pagesToVisit.iterator().next();
            pagesToVisit.remove(next);
            inProgress.add(next);
            return next;
        }
    }
    
    def Collection<String> getNextBatch() {
        val pages = <String>newHashSet
        pages.addAll(pagesToVisit);
        pagesToVisit.clear();
        inProgress.addAll(pages);
        return pages;
    }
    
    def boolean isFinished() {
        return pagesToVisit.isEmpty() && inProgress.isEmpty();
    }
}