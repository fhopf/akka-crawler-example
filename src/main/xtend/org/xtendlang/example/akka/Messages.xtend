package org.xtendlang.example.akka

import java.util.List

@Data
class PageContent {
    val String path;
	val List<String> linksToFollow;
    val String title;
    val String content;
}

@Data
class IndexedMessage {
	val String path
}

class CommitMessage {}
class CommittedMessage {}