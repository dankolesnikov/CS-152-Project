#/bin/bash	
# Linux only	
# Clones repo	
# Installs leiningen	
# Creates Hadoop jar file
# Opens slides in the browser		
 	 
echo "CS 152 Cascalog Project"
echo "Starting to build..." 
echo "Cloning repo"
 	 
repo="https://github.com/dankolesnikov/CS-152-Project.git"	+repo = "https://github.com/dankolesnikov/CS-152-Project.git"
git clone $repo
echo "Done cloning."	
cd "CS-152-Project"	
# less "project-proposal.txt"	
# echo "Installing Leiningen - build tool for Clojrue projects."	
	
# This doesn't get the latest version" sudo apt-get install clojure	
# Below is the preffered way to install lein	
# cp leiningen-build-script.sh /usr/local/bin	
# sudo chmod +x /usr/local/bin/lein/leiningen-build-script	
# lein -version	
	
# echo "Creating a Hadoop JAR"	
# lein uberjar	
# echo "Uploading JAR to Google Cloud"	
# echo "Downloading CSV files from Google Cloud"
# echo "CSV files generated from Google Cloud Hadoop cluster reside in hadoop_output folder"
echo "Opening slides.."
xdg-open "slides/reveal.js-3.6.0/finalPresentation.html"