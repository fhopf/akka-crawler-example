package de.fhopf.akka;

import java.util.List;

/**
 * The content for a certain page.
 * @author Florian Hopf, http://www.florian-hopf.de
 */
public class PageContent {
    
    private final List<String> linksToFollow;
    private final String title;
    private final String content;
    private final String path;

    public PageContent(String path, List<String> linksToFollow, String title, String content) {
        this.path = path;
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

    public String getPath() {
        return path;
    }
    
    @Override
    public String toString() {
        return "PageContent{title=" + title + ", content=" + content + ", linksToFollow=" + linksToFollow + "}";
    }

    
}
