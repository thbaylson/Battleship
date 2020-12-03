# Battleship
Author: Tyler Baylson

Author: Dillon Gorlesky

Date: 11/24/2020

## Purpose:
------------------------------------------------------------
The purpose of this project is to use TCP and threads in order to handle 
multiple clients playing BattleShip. They communicate using Sockets, Connection Agents,
and PrintStreams.

## How to Compile:
------------------------------------------------------------
javac common/*.java client/*.java server/*.java

## How to Run Server:
------------------------------------------------------------
java server.BattleShipDriver <port> <boardSize>

## How to Run Client:
------------------------------------------------------------
java client.BattleDriver <hostname> <port> <name>

## Bugs:
------------------------------------------------------------
1. When a client is interrupted, the program does not remove the player information 
from the server and the server continues to wait on their input.
2. When the server is interrupted, the clients hang instead of quitting.
3. If there are 3 players and if a player (Player A) defeats the player who just 
finished their turn (Player B), the server gives player A a second turn.
