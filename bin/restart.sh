#!/bin/bash

[  -e $(dirname "$0")/stop.sh ] && . $(dirname "$0")/stop.sh
[  -e $(dirname "$0")/start.sh ] && . $(dirname "$0")/start.sh