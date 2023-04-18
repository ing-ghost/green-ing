#!/bin/bash
./gradlew build

RESULT=$?
if [ $RESULT -eq 0 ]; then
        echo success
  else
        gradle build
fi