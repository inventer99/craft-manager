#!/bin/sh

SESSION_NAME="minecraft"
SERVER_COMMAND='"stop" Enter'

tmux send-keys -t "$SESSION_NAME" "$SERVER_COMMAND"
exit "$?"
