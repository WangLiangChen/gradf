#!/bin/bash
cd $(dirname "$0") || exit
cd ..
APP_HOME=$(pwd)
APP_JAR=$(ls "$APP_HOME" | grep .jar)
if [ -z "$APP_JAR" ]; then
    echo "ERROR: APP_JAR does not exist"
    exit 1
fi
APP_JAR="$APP_HOME/$APP_JAR"
PID=$(ps -ef | grep java | grep "$APP_JAR" | awk '{print $2}')

export APP_HOME
export PID
echo "APP_HOME: $APP_HOME"
echo "APP_JAR:$APP_JAR"