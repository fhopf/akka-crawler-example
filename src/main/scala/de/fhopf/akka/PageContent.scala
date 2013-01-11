package de.fhopf.akka

import java.util.List

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class PageContent(path: String, linksToFollow: List[String], title: String, content: String) {

  def getContent(): String = content

  def getLinksToFollow(): List[String] = linksToFollow

  def getTitle(): String = title

  def getPath(): String = path

  override def toString() = "PageContent{title=" + title + ", content=" + content + ", linksToFollow=" + linksToFollow + "}"

}
