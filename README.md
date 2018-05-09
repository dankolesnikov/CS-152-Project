# CS 152 Project: Cascalog

> Group project for Programming Paradigms class with Professor Cay Horstmann.


## Build 
Linux: 
Ensure system has Hadoop and can run as a single-node cluster:
http://www.bogotobogo.com/Hadoop/BigData_hadoop_Install_on_ubuntu_single_node_cluster.

After, build script should work perfectly fine using the following lines:

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
Compare a datasets of flight data over 6 years. Analyze the data by finding the airline carriers that had the biggest delay times in 2012 and compare it to the performance in 2013, 2014, 2015, 2016, 2017. Use Hadoop through Cascalog to reduce the large dataset to a more comprehensible set of 5 csvs seperated by year for data visualization.

Metrics we are looking at:

* Total Number of Flights
* Total Number of Delayed Flights over 15 minutes
* Average Delay

### Dataset Link

[Flight Data](http://stat-computing.org/dataexpo/2009/the-data.html)
