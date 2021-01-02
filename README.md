# Craft Manager

Craft Manager is a tool for remotely controlling video game servers from Discord.

# About

Craft Manager was created to assist with the management of a modded Minecraft server.
Modded Minecraft is prone to crashing. Craft Manager allows for members of the server
rather than just the \[Minecraft\] server owner to
start the server, stop/restart the server, get the server's status, & receive crash reports.

## Features

**All are currently WIP**

* Server Management
  * Start, Stop, Restart servers
  * Pull server status
  * Receive crash reports

# Installing

Currently supported Operating Systems

  * FreeBSD

1. Ensure that Java 1.8 is installed.
2. Ensure that Tmux is installed.
3. TODO

# Using

Discord commands are prefixed with `cm!`.

#### Start
Start a server  
Usage: `cm! start <name>`  
Permissions: CmAdmin

#### Stop
Stop a server\
Usage: `cm! stop <name>`  
Permissions: CmAdmin

#### Status
Get a server's status\
Usage: `cm! status [<name>]`

Get all server statuses\
Usage: `cm! status`