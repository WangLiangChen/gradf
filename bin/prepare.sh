#!/bin/bash
cd `dirname $0`
cd ..
APP_HOME=`pwd`
APP_LAUNCHER_JAR=`ls $APP_HOME | grep .jar`
if [ -z "$APP_LAUNCHER_JAR" ]; then
    echo "ERROR: APP_LAUNCHER_JAR does not exist"
    exit 1
fi
APP_LAUNCHER_JAR="$APP_HOME/$APP_LAUNCHER_JAR"
PID=`ps -ef | grep java | grep "$APP_LAUNCHER_JAR" | awk '{print $2}'`
STDOUT_FILE=console.log
export APP_HOME
export STDOUT_FILE
export PID
echo "APP_HOME: $APP_HOME"
echo "APP_LAUNCHER_JAR:$APP_LAUNCHER_JAR"
echo "STDOUT_FILE: $STDOUT_FILE"