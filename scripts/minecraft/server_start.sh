#!/bin/sh

SESSION_NAME="minecraft"
SERVER_COMMAND='/bin/sh LaunchServer.sh'

tmux new-session -d -s "$SESSION_NAME" -c "$SERVER_DIRECTORY" "$SERVER_COMMAND"
tmux list-sessions
exit "$?"
