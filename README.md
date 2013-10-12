# Simple Producer Consumer example for Akka in Java

This repository contains 3 examples of a simple web crawler:
* A sequential example
* An example where the logic is split in 3 Actors
* An example where the retrieval of the pages is handled by multiple Actors in parallel.
* An example where retrieval fails and the application hangs
* A supervised example where failing messages are resend

To start the sequential execution run gradle runSequential

To start the simple actor execution run gradle runActors

To start the parallel page fetching run gradle runParallelActors

To start the failing or supervised examples run gradle runFA or gradle runSA

The code is only meant as an example on how to implement a producer consumer example in Akka. More information: 

* http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html
* http://blog.florian-hopf.de/2013/10/cope-with-failure-actor-supervision-in.html

*Please don't use the crawler as it is on sites where you didn't contact the site owner*
