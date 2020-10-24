#!/bin/bash

JAVA_HOME=$(ls -rd /JavaSoft/jdk1.8* | head -1)
if [ ! -d "$JAVA_HOME" ] ;then
    echo "ERROR: cannot found Java running environment "
    exit 1
fi
MAX_JAVA_HEAP=4096m
PROFILE_ACTIVE=
CONFIG_PATH=/JavaSoft/server/config/prod

if [[ -n "$MAX_JAVA_HEAP" ]] ;then
JAVA_HEAP_OPTS="-Xms$MAX_JAVA_HEAP -Xmx$MAX_JAVA_HEAP"
fi
if [[ -n "$APP_NAME" ]] ;then
SPRINGBOOT_OPTS="-Dspring.application.name=$APP_NAME"
fi
if [[ -n "$PROFILE_ACTIVE" ]] ;then
SPRINGBOOT_OPTS="$SPRINGBOOT_OPTS -Dspring.profiles.active=$PROFILE_ACTIVE"
fi
if [[ -n "$CONFIG_PATH" ]] ;then
CONFIG_PATH_OPTS="-DconfigPath=$CONFIG_PATH"
fi

JAVA_OPTS="$JAVA_HEAP_OPTS -server $CONFIG_PATH_OPTS $SPRINGBOOT_OPTS -Djava.security.egd=file:/dev/./urandom"
STDOUT_FILE=stdout.log
export JAVA_HOME
export JAVA_OPTS
export STDOUT_FILE
echo "JAVA_HOME: $JAVA_HOME"
echo "JAVA_OPTS: $JAVA_OPTS"
echo "STDOUT_FILE: $STDOUT_FILE"
