#!/bin/sh

UNKNOWN=0
RUNNING=10
STOPPED=11
FROZEN=12
STARTING=20
STOPPING=21

SESSION_NAME="minecraft"

tmux has-session -t $SESSION_NAME 2> /dev/null

if [ "$?" -eq 0 ]; then
  exit $RUNNING
else
  exit $STOPPED
fi

exit $UNKNOWN
