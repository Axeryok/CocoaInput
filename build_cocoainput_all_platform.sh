#!/bin/bash
./build_lib_all.sh
cd forge
./wslgradlew build
cd ../fabric
./wslgradlew build