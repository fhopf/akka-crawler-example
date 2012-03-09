package de.fhopf.akka;

import java.util.List;

/**
 * The content for a certain page.
 * @author Florian Hopf
 */
public class PageContent {
    
    private final List<String> linksToFollow;
    private final String title;
    private final String content;

    public PageContent(List<String> linksToFollow, String title, String content) {
        this.linksToFollow = linksToFollow;
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public List<String> getLinksToFollow() {
        return linksToFollow;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "PageContent{title=" + title + ", content=" + content + ", linksToFollow=" + linksToFollow + "}";
    }

    
}
