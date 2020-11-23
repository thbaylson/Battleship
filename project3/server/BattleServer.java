/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import common.*;

/**
 * BattleServer is one of the classes that implement the server-side logic of
 * this client-server application. It is responsible for accepting incoming
 * connections, creating ConnectionAgents, and passing the ConnectionAgent off
 * to threads for processing. Implements the MessageListener interface (i.e., it
 * can "observe" objects that are MessageSources).
 */
public class BattleServer implements MessageListener {

    private ServerSocket server;
    private Game game;
    private int activePlayers;
    private boolean playing;
    private int boardSize;
    private ArrayList<ConnectionAgent> players;
    private ArrayList<String> playerNames;
    private ArrayList<Thread> threads;

    public BattleServer(int port, int boardSize) throws IOException {
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

        broadcast("A player has connected!");

        sendMessage("Hello Player " + players.indexOf(ca)+1, ca);

        if(this.server.isClosed()){
            for(Thread th : threads){
                th.interrupt();
            }
        }
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
        handleMessage(message, source);
    }

    public void handleMessage(String message, MessageSource source){
        String[] cmdList = message.split(" ");
        //Because /play is length 1
        if(cmdList.length > 1){
            if(cmdList[0].toLowerCase().equals("/join")){
                //JOIN
                joinCmd(cmdList, source);
            } else if(cmdList[0].toLowerCase().equals("/quit")){
                //QUIT
                quitCmd(cmdList, source);
            } else if(cmdList[0].toLowerCase().equals("/play")){
                //PLAY
                playCmd(cmdList, source);
            } else if(cmdList[0].toLowerCase().equals("/show")){
                //SHOW
                showCmd(cmdList, source);
            } else if(playing && cmdList[0].toLowerCase().equals("/attack")){
                //ATTACK
                attackCmd(cmdList, source);
            }
        }
    }

    public void playCmd(String[] cmds, MessageSource source){
        if(!playing){
            if(activePlayers >= 2){
                System.out.println("Game start!");
                this.playing = true;
            } else {
                sendMessage("There are not enough players to start the game.", source);
            }
        } else {
            sendMessage("The game has already started.", source);
        }
    }

    public void quitCmd(String[] cmds, MessageSource source){
        //TODO: CLOSE THE SOURCE
        for(String name : playerNames){
            if(name.equals(cmds[1])){
                int player = playerNames.indexOf(cmds[1]);
                this.playing = activePlayers != 1;
                broadcast(" has surrendered!");
                removePlayer(player);
            }
        }
    }

    public void joinCmd(String[] cmds, MessageSource source){
        if(!playing){
            boolean validName = true;
            for(String name : playerNames){
                if(name.equals(cmds[1])){
                    validName = false;
                    sendMessage("The name " + cmds[1] + " is already in use. Please enter a new name.", source);
                }
            }
            if(validName){
                this.activePlayers++;
                this.game.addPlayer();
                this.playerNames.add(cmds[1]);
            }
        } else { 
            sendMessage("The game is already in progress. You cannot join.", source);
        }
    }

    public void showCmd(String[] cmds, MessageSource source){
        boolean validName = false;
        if(cmds.length == 2){
            //Checking first name exists
            for(String person : playerNames){
                if(cmds[1].equals(person)){
                    validName = true;
                }
            }

            if(validName){
                int request = playerNames.indexOf(cmds[1]);
                int requester = players.indexOf(source);
                if(request == requester){
                    sendMessage(this.game.getActiveBoard(request), source);
                } else {
                    sendMessage(this.game.getInactiveBoard(request), source);
                }
            } else{
                sendMessage(cmds[1] + " could not be found. Please retry.", source);
            }
        } else {
            sendMessage("Usage: \\show [player_name]", source);
        }    
    }

    public void attackCmd(String[] cmds, MessageSource source){
        boolean validName = false;
        if(cmds.length == 4){
            
            //Checking first name exists
            for(String person : playerNames){
                if(cmds[1].equals(person)){
                    validName = true;
                }
            }
            
            //Check the dimensions
            if(validName){
                try{
                    int request = playerNames.indexOf(cmds[1]);
                    int requester = players.indexOf(source);

                    int row = Integer.parseInt(cmds[2]);
                    int col = Integer.parseInt(cmds[3]);
                    
                    boolean validDimensions = Grid.verifyIndex(this.boardSize, row, col);
                    
                    //Check if it's the player's turn
                    if(validDimensions){
                        if(requester == (this.game.getTurn() % players.size())){

                            //Prevent players from attacking themselves
                            if(request != requester){
                                boolean playerDefeated = this.game.attack(request, row, col);
                                sendMessage(this.game.getInactiveBoard(request), source);
                                
                                if(playerDefeated){
                                    broadcast(playerNames.get(request) + " has been defeated!");
                                    removePlayer(request);
                                }
                                checkWinConditions();
                            }
                        }else{
                            sendMessage("It is not your turn. Please be patient.", source);
                        }
                    } else{
                        sendMessage("Usage: //attack [player_name] [row] [column]", source);
                    }
                } catch(NumberFormatException e){
                    sendMessage("Usage: //attack [player_name] [row] [column]", source);
                }
            }else{
                sendMessage(cmds[1] + " could not be found. Please retry.", source);
            }
        } else {
            sendMessage("Usage: //attack [player_name] [row] [column]", source);
        }
    }

    /**
     * Removes a player from all Collections that track player data
     * @param player The index of a player to be removed
     */
    private void removePlayer(int player){
        try {
            players.get(player).close();
            players.remove(player);
            playerNames.remove(player);
            this.game.removePlayerAt(player);
            this.activePlayers--;
            //TODO: invoke sourceClosed()?
        } catch (IOException e) {
            //Exceptions on this level should be printed to the server
            System.out.println("Error removing player");
        }
    }

    private void checkWinConditions(){
        if(playerNames.size() == 1 && playing){
            this.playing = false;
            String player = playerNames.get(0);
            sendMessage("Congratulations " + player + "!! You Win!!", players.get(0));
            
            this.game = new Game(boardSize);
            this.game.addPlayer();
        }
    }

    @Override
    public void sourceClosed(MessageSource source) {
        // TODO Auto-generated method stub

    }
}
