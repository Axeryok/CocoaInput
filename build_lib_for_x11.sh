#!/bin/bash
echo "Build libcocoainput for X11"
mkdir -p src/main/resources/x11
cd libcocoainput/x11
make && make install
