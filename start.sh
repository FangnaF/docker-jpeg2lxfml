#!/bin/bash

wget $IMAGE_URL -O input.jpeg

# Add Java class to system Java & execute
javac Jpeg2Lxfml.java
java Jpeg2Lxfml input.jpeg

# Install gist gem to create gists from file
gem install gist
# Upload anonymous gist to GitHub
gist input.jpeg.lxfml
echo "Navigate to gist URL and save text as <name_of_image>.lxf file. Open in LDD. Enjoy!"
