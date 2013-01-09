# Simple Producer Consumer example for Akka in scala

This is a rewrite in scala of the original project by Florian Hopf:
http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html
https://github.com/fhopf/akka-crawler-example


This repository contains 3 examples of a simple web crawler:
* A sequential example
* An example where the logic is split in 3 Actors
* An example where the retrieval of the pages is handled by multiple Actors in parallel.

To start the sequential execution run gradle runSequential

To start the simple actor execution run gradle runActors

To start the parallel page fetching run gradle runParallelActors

The code is only meant as an example on how to implement a producer consumer example in Akka.
For more information visit http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html


TODO (SCALA): refactoring code in order to use the best scala constructs.