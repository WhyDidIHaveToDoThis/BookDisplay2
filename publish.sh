#!/usr/bin/env bash
var=$1

if [ "$var" == "" ]; then
	echo 'What version variable should be increased? (major, api, minor, patch)'
	echo -n '> '
	read var
fi

./gradlew increaseVersion "-Pvariable=${var,,}"
./gradlew pushMaven
