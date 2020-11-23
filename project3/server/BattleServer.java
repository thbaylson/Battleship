/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import common.*;

/**
 * BattleServer is one of the classes that implement the server-side logic of
 * this client-server application. It is responsible for accepting incoming
 * connections, creating ConnectionAgents, and passing the ConnectionAgent off
 * to threads for processing. Implements the MessageListener interface (i.e., it
 * can "observe" objects that are MessageSources).
 */
public class BattleServer implements MessageListener {

    private int port;
    private ServerSocket server;
    private int current;
    private Game game;
    private int activePlayers;
    private boolean playing;
    private int boardSize;
    private ArrayList<ConnectionAgent> players;
    private HashMap<String, ArrayList<Object>> playerMap;
    private ArrayList<Grid> playerGrids;
    private ArrayList<String> playerNames;
    private ArrayList<Thread> threads;

    public BattleServer(int port, int boardSize) throws IOException {
        this.playerMap = new HashMap<>();
        this.port = port;
        this.boardSize = boardSize;
        this.server = new ServerSocket(port);
        this.game = new Game(boardSize);
        this.players = new ArrayList<>();
        this.playerNames = new ArrayList<String>();
        this.threads = new ArrayList<>();
        this.playing = false;
        this.activePlayers = 0;
    }

    /**
     * Constantly listening
     * 
     * @throws IOException
     */
    public void listen() throws IOException {
        Socket socket = this.server.accept();   
        
        ConnectionAgent ca = new ConnectionAgent(socket);
        ca.addMessageListener(this);
        
        Thread thread = new Thread(ca);
        thread.start();
        threads.add(thread);
        players.add(ca);

        broadcast("A player has connected");

        sendMessage("Hello Player: " + players.indexOf(ca), ca);

        if(this.server.isClosed()){
            for(Thread th : threads){
                th.interrupt();
            }
        }
        //((ConnectionAgent)playerMap.get(playerMap.values().toArray()[i]).get(2)).sendMessage(message);
    }

    public void broadcast(String message){
        for(ConnectionAgent player : players){
            player.sendMessage(message);
        }
    }

    public void sendMessage(String message, MessageSource source){
        for(ConnectionAgent ca : players){
            if(ca.equals(source)){
                ca.sendMessage(message);
            }
        }
    }

    public boolean isClosed(){
        return server.isClosed();
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        System.out.println(message + " msg");
        handleMessage(message, source);

    }

    public void handleMessage(String message, MessageSource source){
        System.out.println("I am user Number: " + players.indexOf(source));
        if(validCmd(message)){
            String[] cmdList = message.split(" ");
            if(cmdList[0].toLowerCase().equals("/join")){
                /**playerMap.put(cmdList[1], new ArrayList<>());
                ArrayList<Object> objs  = new ArrayList<>();
                objs.add(cmdList[1]);
                objs.add(source);
                playerMap.get(cmdList[1]).add(objs);
                ((ConnectionAgent) source).sendMessage(message);*/

                //playerNames.add(cmdList[1]);
                this.game.addPlayer();
            } else if(cmdList[0].toLowerCase().equals("/show")){//SHOW
                int request = playerNames.indexOf(cmdList[1]);
                int requester = players.indexOf(source);
                if(request == requester){
                    sendMessage(this.game.getActiveBoard(request), source);//SEND THIS 
                } else {
                    sendMessage(this.game.getInactiveBoard(requester), source);
                }
            } else if(cmdList[0].toLowerCase().equals("/attack")){//ATTACK
                int request = playerNames.indexOf(cmdList[1]);
                int requester = players.indexOf(source);
                if(request == requester){
                    //sendMessage(this.game.getActiveBoard(request), source);//SEND Error
                } else {
                    int row = Integer.parseInt(cmdList[2]);
                    int col = Integer.parseInt(cmdList[3]);
                    this.game.attack(request, row, col);
                    sendMessage(this.game.getInactiveBoard(request), source);
                }
            }
        } else {

        }
    }

    public boolean validCmd(String command){
        String[] cmdList = command.split(" ");
        //cmdList[0] is user asking
        //cmdList[1] is command they wish to use
        //if cmdList[3] its the target they want to use it on
        //If cmdList[3] == /attack, cmdList[4] & cmdList[5] are coords
        if(cmdList.length < 1){//Because /play is length 1
            return false;
        }
        if(cmdList[0].toLowerCase().equals("/join")){//Join
            return joinCmd(cmdList);
        } else if(cmdList[0].toLowerCase().equals("/quit")){//QUIT
            return quitCmd(cmdList);
        } else if(cmdList[0].toLowerCase().equals("/play")){//PLAY
            return playCmd(cmdList);
        } else if(cmdList[0].toLowerCase().equals("/show")){//SHOW
            return showCmd(cmdList);
        } else if(cmdList[0].toLowerCase().equals("/attack")){//ATTACK
            return attackCmd(cmdList);
        }
        return false;
    }

    public boolean playCmd(String[] cmds){
        if(!playing){
            if(activePlayers >= 2){
                System.out.println("Game can begin.");
                this.playing = true;
            } else {
                System.out.println("Error: Not enough players.");
            }
        } else {
            System.out.println("Error: Game already started.");
        }
        return true;
    }

    public boolean quitCmd(String[] cmds){
        if(activePlayers == 0){
            return true;
        }
        //NEED TO GET THE MESSAGESOURCE OF THE PERSON QUITTING INSTEAD OF BY PLAYER NAMES
        //BECAUSE THEY ONLY DO /QUIT NOW INSTEAD OF /QUIT <NAME>

        //ALSO CLOSE THE SOURCE
        for(String name : playerNames){
            if(name.equals(cmds[1])){
                System.out.println("Player: " + cmds[1] + " has surrendered.");
                int index = playerNames.indexOf(cmds[1]);
                playerNames.remove(cmds[1]);
                activePlayers--;
                this.game.removePlayerAt(index);
                if(activePlayers == 1){
                    playing = false;
                }
                return true;
            }
        }
        System.out.println("Error: " + cmds[1] + " is not a player. Please retry.");
        return false;
    }

    public boolean joinCmd(String[] cmds){
        if(!playing){
            if(cmds.length > 1){
                for(String name : playerNames){
                    if(name.equals(cmds[1])){
                        System.out.println("Error: " + cmds[1] + " is already in use. Please enter new name.");
                        return false;
                    }
                }
                activePlayers++;
                playerNames.add(cmds[1]);
            } else {
                System.out.println("No player name given");
                return false;
            }
        } else { 
            System.out.println("Error: Game already in progress. Can't join");
        }
        return true;
    }

    public boolean showCmd(String[] cmds){
        boolean nameCheck = false;
        if(cmds.length == 2){
            //Checking first name exists
            for(String person : playerNames){
                if(cmds[1].equals(person)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[1] + " Player name not found. Please retry.");
                return false;
            }
            return true;
        } else {
            System.out.println("Error: not enough arguments for show.");
            return false;
        }
    }

    public boolean attackCmd(String[] cmds){
        boolean nameCheck = false;
        if(cmds.length < 4){//TEST THIS FOR MORE THAN 4 ARGUMENTS OR BAD ONES
            System.out.println("Error: Not enough arguments given for attack command.");
            return false;
        } else {
            //Checking first name exists
            for(String person : playerNames){
                if(cmds[1].equals(person)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[1] + " Player name not found. Please retry.");
                return false;
            }
            try{
                int row = Integer.parseInt(cmds[2]);
                int col = Integer.parseInt(cmds[3]);
                if(!(row <= this.boardSize)){
                    nameCheck = false;
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmds[2] + " Is not on the board.");
                    return false;
                }

                if(!(col <= this.boardSize)){
                    nameCheck = false;
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmds[3] + " Is not on the board.");
                    return false;
                }
                return true;
            } catch(NumberFormatException e){
                System.out.println("Error: An improper value given for row or column.");
                return false;
            }
        }
    }

    @Override
    public void sourceClosed(MessageSource source) {
        // TODO Auto-generated method stub

    }


    /**
     * Connection agent is key
     * Client side -> Port param
     * & set up server side (on its own thread)
     * 
     * Each client gets a connection agent
     */

}
