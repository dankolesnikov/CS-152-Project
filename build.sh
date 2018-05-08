#/bin/bash

echo "CS 152 Cascalog Project"
echo "Starting to build..."
# echo "Cloning repo"

repo="https://github.com/dankolesnikov/CS-152-Project.git"
git clone $repo
echo "Done cloning."
cd "CS-152-Project"
# less "project-proposal.txt"
echo "Installing Leiningen - build tool for Clojrue projects."


echo "Opening Slides..."
