#!/bin/bash
rm -r src/autogen
mkdir -p src/autogen/
cp -r ../src/main/* src/autogen/
mv src/autogen/resources/cocoainput.mixins.json src/autogen/java
cd src/autogen/java
find . -type f -print0 | xargs -0 sed -i -e 's/"refmap":"cocoainput.refmap.json",//g'
mv cocoainput.mixins.json ../resources


