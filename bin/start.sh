#!/bin/bash

[  -e `dirname $0`/app.sh ] && . `dirname $0`/app.sh
[  -e `dirname $0`/env.sh ] && . `dirname $0`/env.sh
[  -e `dirname $0`/prepare.sh ] && . `dirname $0`/prepare.sh

if [ -n "$PID" ]; then
    echo "ERROR: The $APP_LAUNCHER_JAR already started!"
    echo "PID: $PID"
    exit 1
fi

echo "Starting the $APP_NAME ..."

if [ -n "$FOREGROUND_MODE" ] ;then
    $JAVA_HOME/bin/java -jar $JAVA_OPTS  $APP_LAUNCHER_JAR
    echo "$JAVA_HOME/bin/java -jar $JAVA_OPTS  $APP_LAUNCHER_JAR"
else
    nohup $JAVA_HOME/bin/java -jar $JAVA_OPTS $APP_LAUNCHER_JAR > $STDOUT_FILE 2>&1 &
    echo "nohup $JAVA_HOME/bin/java -jar $JAVA_OPTS $APP_LAUNCHER_JAR > $STDOUT_FILE 2>&1 &"
fi


COUNT=0
while [ $COUNT -lt 1 ]; do    
    sleep 1
    COUNT=`ps -ef | grep java | grep "$APP_HOME" | awk '{print $2}' | wc -l`
    echo "ps check [$COUNT]"
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PID=`ps -ef | grep java | grep "$APP_HOME" | awk '{print $2}'`
echo "PID: $PID"