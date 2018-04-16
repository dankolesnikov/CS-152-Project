# CS 152 Project: Cascalog

## Overview

Compare 4 datasets of flight data over 4 years. Analyze the data by finding the top 5 airline carriers that had the biggest delay times in 2004 and compare it to the performance in 2005, 2006, 2007, 2008. See if those airlines have improved. 

Metrics we are looking at:
Average delay Time
Average Number of bounced flights
Response to increased load during holiday flights i.e Summer and Winter Break
Average Delay

### Dataset

[Flight Data](http://stat-computing.org/dataexpo/2009/the-data.html)

## Week 1 Report

### Kushal
04/14/18 12:00AM - 1PM Hashed out project details in group chat. 

Comments: Making specifications and delegating responsibilities. Much more progress will be made next week.

Total hours this week:1
Total hours on project:1

### Danil

* I've researched Clojure programming language and installation guide for the kernel for Jupyter Notebooks. I suggest using it if it works well with the Cascalog framework. It will be easier to collaborate on the code. 
* Installed Clojure IDE: Nightcode (no feedback yet)
* Installed Cursive clojure plugin for IntelliJ
* [Kernel for Notebooks](https://github.com/clojupyter/clojupyter)

Useful Learning Materials:

* [Learn Clojure in Y minutes](https://learnxinyminutes.com/docs/clojure/)


**Terminal commands for setting up Clojure Kernel for Jupyter Notebooks(Mac):**

	brew install clojure
	brew install leiningen
	git clone https://github.com/clojupyter/clojupyter
	cd clojupyter
	make
	make install

### Avi
TBU

### Jason
TBU 


## Week 2 Report

To Do:

* Need to figure out the architecture:
	* How do we connect Hadoop / Cascalog ?
	* How to connect cascalog with web server for data visualization?
	* Should we use web front end for data visualization or some java plotting library?
		* If going with web, how do we connect cascalog and webserver? or ust use .csv? 
* Start coding!
