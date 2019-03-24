#!/bin/bash
echo "Build libcocoainput for macOS"
cd libcocoainput/libcocoainput
make && make install
