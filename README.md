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

### General

#### Help
Shows help information  
Usage: `cm! help [command]`

#### List
Lists all know servers  
Usage: `cm! list`

#### Version
Shows all versions of Craft Manager  
Usage: `cm! version`

### Op

#### Ops
Shows all server operators  
Usage: `cm! <name> ops`

#### Op Groups
Shows all server operator groups  
Usage: `cm! <name> op-groups`

### Process

#### Start
Start a server  
Usage: `cm! <name> start`

#### Stop
Stop a server  
Usage: `cm! <name> stop`

#### Restart
Restarts a server  
Usage: `cm! <name> restart`

#### Status
Get a server's status  
Usage: `cm! <name> status`
