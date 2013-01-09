package de.fhopf.akka

import java.util.Collection
import java.util.HashSet
import java.util.Set
import collection.JavaConversions._

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class VisitedPageStore {

  private var pagesToVisit: Set[String] = new HashSet[String]()
  private var allPages: Set[String] = new HashSet[String]()
  private var inProgress: Set[String] = new HashSet[String]()

  def add(page: String) = {
    if (!allPages.contains(page)) {
      pagesToVisit.add(page)
      allPages.add(page)
    }
  }

  def addAll(pages: Collection[String]) = {
    for (page <- pages) {
      add(page)
    }
  }

  def finished(page: String) = {
    inProgress.remove(page)
  }

  def getNext() = {
    if (pagesToVisit.isEmpty()) {
      null
    } else {
      val next: String = pagesToVisit.iterator().next()
      pagesToVisit.remove(next)
      inProgress.add(next)
      next
    }
  }

  def getNextBatch(): Collection[String] = {
    val pages: Set[String] = new HashSet[String]()
    pages.addAll(pagesToVisit)
    pagesToVisit.clear()
    inProgress.addAll(pages)
    pages
  }

  def isFinished(): Boolean = pagesToVisit.isEmpty() && inProgress.isEmpty()

}
