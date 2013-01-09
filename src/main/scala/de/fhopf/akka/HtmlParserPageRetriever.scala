package de.fhopf.akka

import java.util.ArrayList
import java.util.List
import org.htmlparser.Parser
import org.htmlparser.Tag
import org.htmlparser.tags.BodyTag
import org.htmlparser.tags.LinkTag
import org.htmlparser.tags.TitleTag
import org.htmlparser.util.ParserException
import org.htmlparser.visitors.NodeVisitor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class HtmlParserPageRetriever(baseUrl: String) extends PageRetriever {

  private val logger: Logger = LoggerFactory.getLogger(classOf[HtmlParserPageRetriever])

  override def fetchPageContent(url: String): PageContent = {
    logger.debug("Fetching {}", url)
    try {
      val parser: Parser = new Parser(url)
      val visitor: PageContentVisitor = new PageContentVisitor(baseUrl, url)
      parser.visitAllNodesWith(visitor)
      visitor.getContent()
    } catch {
      case ex: ParserException => throw new IllegalStateException(ex)
    }
  }

  class PageContentVisitor(recursive: Boolean) extends NodeVisitor(recursive) {

    private val linksToVisit: List[String] = new ArrayList[String]()
    private var content: String = ""
    private var title: String = ""

    private var baseUrl: String = ""
    private var currentUrl: String = ""

    def this(baseUrl: String, currentUrl: String) {
      this(true)
      this.baseUrl = baseUrl
      this.currentUrl = currentUrl

    }

    override def visitTag(tag: Tag) = {
      tag match {
        case linkTag: LinkTag => {
          if (linkTag.getLink().startsWith(baseUrl) && isProbablyHtml(linkTag.getLink())) {
            logger.debug("Using link pointing to {}", linkTag.getLink())
            linksToVisit.add(linkTag.getLink())
          } else {
            logger.debug("Skipping link pointing to {}", linkTag.getLink())
          }
        }
        case titleTag: TitleTag => {
          title = titleTag.getTitle()
        }
        case bodyTag: BodyTag => {
          content = bodyTag.toPlainTextString()
        }
      }
    }

    def getContent(): PageContent = new PageContent(currentUrl, linksToVisit, title, content)

    private def isProbablyHtml(link: String): Boolean = link.endsWith(".html") || link.endsWith("/")
  }
}
