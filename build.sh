#/bin/bash

echo "CS 152 Cascalog Project"
echo "Starting to build..."
echo "Cloning repo"

repo = "https://github.com/dankolesnikov/CS-152-Project.git"
git clone $repo

echo "Done."
echo "Installing Leiningen - build tool for Clojure projects."
