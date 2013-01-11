package de.fhopf.akka;

import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retrieves the content using HtmlParser.
 *
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class HtmlParserPageRetriever implements PageRetriever {

    private final String baseUrl;
    private static final Logger logger = LoggerFactory.getLogger(HtmlParserPageRetriever.class);

    public HtmlParserPageRetriever(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public PageContent fetchPageContent(String url) {
        logger.debug("Fetching {}", url);
        try {
            Parser parser = new Parser(url);
            PageContentVisitor visitor = new PageContentVisitor(baseUrl, url);
            parser.visitAllNodesWith(visitor);
            
            return visitor.getContent();
        } catch (ParserException ex) {
            throw new IllegalStateException(ex);
        }
    }


    private static class PageContentVisitor extends NodeVisitor {

        private List<String> linksToVisit = new ArrayList<String>();
        private String content;
        private String title;

        private final String baseUrl;
        private final String currentUrl;
        
        public PageContentVisitor(String baseUrl, String currentUrl) {
            super(true);
            this.baseUrl = baseUrl;
            this.currentUrl = currentUrl;
            
        }

        @Override
        public void visitTag(Tag tag) {
            if (tag instanceof LinkTag) {
                LinkTag linkTag = (LinkTag) tag;
                if (linkTag.getLink().startsWith(baseUrl) && isProbablyHtml(linkTag.getLink())) {
                    logger.debug("Using link pointing to {}", linkTag.getLink());
                    linksToVisit.add(linkTag.getLink());
                } else {
                    logger.debug("Skipping link pointing to {}", linkTag.getLink());
                }
            } else if (tag instanceof TitleTag) {
                TitleTag titleTag = (TitleTag) tag;
                title = titleTag.getTitle();
            } else if (tag instanceof BodyTag) {
                BodyTag bodyTag = (BodyTag) tag;
                content = bodyTag.toPlainTextString();
            }
        }
        
        public PageContent getContent() {
            return new PageContent(currentUrl, linksToVisit, title, content);
        }

        private boolean isProbablyHtml(String link) {
            return link.endsWith(".html") || link.endsWith("/");
        }
    }
}
