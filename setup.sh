#!/bin/bash

# Installing the JVM
sudo apt-get update
sudo apt-get install -y default-jre
sudo apt-get install -y default-jdk\

# Compile java source files
javac ./src/*.java