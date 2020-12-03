# Battleship
Author: Tyler Baylson

Author: Dillon Gorlesky

Date: 11/24/2020

## Purpose:
------------------------------------------------------------
The purpose of this project is to use TCP and threads in order to handle 
multiple clients playing BattleShip. The communicate using connection Agents,
PrintStreams, and Sockets.

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
1. When the client does Ctrl-C, it doesn't remove the player information 
  from the server and continues to wait on their input
2. When the server does Ctrl-C, the clients don't even quit and just hang
3. If there are 3 players, if someone beats the person behind them, it gives  
  that person a second turn
