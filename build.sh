#/bin/bash	
# Linux only	
# Clones repo	
# Installs leiningen	
# Creates Hadoop jar file
# Opens slides in the browser		
 	 
echo "CS 152 Cascalog Project"
echo "Starting to build..." 
echo "Cloning repo"

cd /tmp
 	 
git clone "https://github.com/dankolesnikov/CS-152-Project.git"
echo "Done cloning."	
cd "CS-152-Project"	
# less "project-proposal.txt"	
# echo "Installing Leiningen - build tool for Clojrue projects."	
	
# This doesn't get the latest version" sudo apt-get install clojure	
# Below is the preffered way to install lein	
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
chmod +x lein	
sudo mv lein /usr/local/bin
	
lein -version	
	
echo "Creating a Hadoop JAR"	
lein uberjar	
# echo "Uploading JAR to Google Cloud"	
# echo "Downloading CSV files from Google Cloud"
# echo "CSV files generated from Google Cloud Hadoop cluster reside in hadoop_output folder"

java -jar project-0.1.0-SNAPSHOT-standalone.jar

echo "Opening results of project"
xdg-open "csv_output"
