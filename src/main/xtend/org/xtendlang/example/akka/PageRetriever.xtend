package org.xtendlang.example.akka

import org.htmlparser.visitors.NodeVisitor
import java.util.List
import org.htmlparser.Tag
import org.htmlparser.Parser
import org.htmlparser.tags.LinkTag
import org.htmlparser.tags.TitleTag
import org.htmlparser.tags.BodyTag

interface PageRetriever {
	def PageContent fetchPageContent(String url)
}

@Data
class HtmlParserPageRetriever implements PageRetriever {

    val String baseUrl

    override fetchPageContent(String url) {
        val parser = new Parser(url);
        val visitor = new PageContentVisitor(baseUrl, url);
        parser.visitAllNodesWith(visitor);
        
        return visitor.getContent();
    }
}

package class PageContentVisitor extends NodeVisitor {

        private extension List<String> linksToVisit = newArrayList
        private String content;
        private String title;

        val String baseUrl;
        val String currentUrl;
        
        new(String baseUrl, String currentUrl) {
            super(true);
            this.baseUrl = baseUrl;
            this.currentUrl = currentUrl;
            
        }

        override visitTag(Tag it) {
        	switch(it) {
        		LinkTag: {
        			if (link.startsWith(baseUrl) && link.probablyHtml) {
        				link.add
        			}
        		}
        		TitleTag: {
        			title = it.title
        		}
        		BodyTag: {
        			content = toPlainTextString
        		}
        	}
        }
        
       	def getContent() {
            return new PageContent(currentUrl, linksToVisit, title, content);
        }

        def private boolean isProbablyHtml(String link) {
            return link.endsWith(".html") || link.endsWith("/");
        }
    }