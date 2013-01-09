package de.fhopf.akka

import org.htmlparser.Node
import org.htmlparser.Parser
import org.htmlparser.tags.LinkTag
import org.htmlparser.util.ParserException
import org.htmlparser.visitors.ObjectFindingVisitor
import org.junit.Test
import org.junit.Assert.assertTrue

/**
 * @author Florian Hopf, http://www.florian-hopf.de
 * @author Alfredo Serafini, http://www.seralf.it
 */
class HtmlParserTest {

  @Test
  def testLinkExtraction() = {
    val parser: Parser = new Parser("http://synyx.de")
    val visitor: ObjectFindingVisitor = new ObjectFindingVisitor(classOf[LinkTag])
    parser.visitAllNodesWith(visitor)
    val links: Array[Node] = visitor.getTags()
    assertTrue(links.length > 0)

    for (i <- 0 until links.length) {
      val linkTag = links { i }.asInstanceOf[LinkTag]
      print("\"" + linkTag.getLinkText() + "\" => ")
      println(linkTag.getLink())
    }

  }

}
