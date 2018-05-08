# CS 152 Project: Cascalog

> Group project for Programming Paradigms class with Professor Cay Horstmann.


## Build 
Linux: 

```
chmod +x build.sh
./build.sh
```
 
 Viola!


## Overview

### Project Goals

* Learn a new functional programming language that isn't Scala - Clojure.
* Pick a framework in that language - Cascalog
* Determine whether it is a suitabe language for the problems that the chosen framework tackles. 

### Description 
Compare a datasets of flight data over 4 years. Analyze the data by finding the top 5 airline carriers that had the biggest delay times in 2004 and compare it to the performance in 2005, 2006, 2007, 2008. See if those airlines have improved. 

Metrics we are looking at:

* Average delay Time
* Average Number of bounced flights
* Response to increased load during holiday flights i.e Summer and Winter Break
* Average Delay

### Dataset Link

[Flight Data](http://stat-computing.org/dataexpo/2009/the-data.html)
