#!/bin/bash
echo "Build libcocoainput for macOS"
mkdir src/main/resources/darwin
cd libcocoainput/darwin/libcocoainput
make && make install
