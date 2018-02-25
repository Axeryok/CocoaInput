#!/bin/bash
echo "Build libcocoainput for macOS"
cd cocoainput/cocoainput

#Build
gcc -shared -o libcocoainput.dylib cocoainput.m Logger.m MinecraftView.m DataManager.m -framework Foundation -framework Cocoa -fobjc-arc

#ErrorCheck
if [ "$?" -eq 0 ]
then
	echo 'Succeeded in build.'
	cd -
	mkdir -p src/main/resources/darwin
	mv cocoainput/cocoainput/libcocoainput.dylib src/main/resources/darwin/
	echo 'Moved library to resources path.'
else
	echo 'Failed to build library.'
fi

