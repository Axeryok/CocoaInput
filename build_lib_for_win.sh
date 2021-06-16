#!/bin/bash
echo "Build libcocoainput for Windows"
mkdir -p src/main/resources/win
cd libcocoainput/win
make && make install
