#!/bin/bash

[  -e $(dirname "$0")/app.sh ] && . $(dirname "$0")/app.sh
[  -e $(dirname "$0")/process.sh ] && . $(dirname "$0")/process.sh

if [ -z "$PID" ]; then
    echo "ERROR: $APP_JAR does not started!"
    exit 1
fi

for SINGLEPID in $PID ; do
    kill "$SINGLEPID"
    echo "$APP_NAME is stopping ..."
done

MAX_WAIT=20
COUNT=0
while [ $COUNT -le $MAX_WAIT ]; do
    sleep 1
    ((COUNT=COUNT+1))
    for SINGLEPID in $PID ; do
        PID_EXIST=$(ps -f -p "$SINGLEPID" | grep java)
        if [ -n "$PID_EXIST" ]; then
            echo "waiting for $APP_NAME stop ..."
            if [ $COUNT -ge $MAX_WAIT ]; then
              echo "Force to terminate the $APP_JAR [PID: ${SINGLEPID}] ..."
              kill -9 "$SINGLEPID"
            fi
            break
        else
            ((COUNT=MAX_WAIT+1))
        fi
    done
done

echo "PID: $PID"
echo "$APP_JAR has been stopped!"