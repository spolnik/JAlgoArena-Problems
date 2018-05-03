#!/usr/bin/env bash
pm2 stop problems
pm2 delete problems
./gradlew clean
./gradlew stage
pm2 start pm2.config.js