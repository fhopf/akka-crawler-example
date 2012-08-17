package de.fhopf.akka;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.ObjectFindingVisitor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Experiment with some of the html parser functionality.
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class HtmlParserTest {

    @Test
    public void testLinkExtraction() throws ParserException {
        Parser parser = new Parser("http://synyx.de");
        ObjectFindingVisitor visitor = new ObjectFindingVisitor(LinkTag.class);
        parser.visitAllNodesWith(visitor);
        Node[] links = visitor.getTags();
        // TODO this could use some more meaningful assertions
        assertTrue(links.length > 0);
        for (int i = 0; i < links.length; i++) {
            LinkTag linkTag = (LinkTag) links[i];
            System.out.print("\"" + linkTag.getLinkText() + "\" => ");
            System.out.println(linkTag.getLink());
        }
    }
}
