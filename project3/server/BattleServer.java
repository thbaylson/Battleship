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
    private final String invalidCmd1 = "Valid Commands are: \n\t /join <username>" +
                                "\n\t /play \n\t /attack <target> <[0-";
    private final String invalidCmd2 = "]>" +
                                "\n\t /quit <name>\n\t /show <target>\n";

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
        
        /*
        int i = 0;
        
        for(ConnectionAgent t : players){
            if(!t.isConnected()){
                System.out.println(message + " inside");
                String[] cmds = message.split(" ");
                MessageSource ml = players.get(i);
                quitCmd(cmds, ml);
            }
            i++;
            //Check if theres a connection (client does ctrl-c)
        }*/
        
        if(this.server.isClosed()){
            for(Thread t : threads){
                t.interrupt();
            }
        }
    }

    /**
     * 
     * @param message
     */
    private void broadcast(String message){
        message = message + "\n";
        for(ConnectionAgent player : players){
            player.sendMessage(message);
        }
    }


    /**
     * Broadcasts a message to every connection except the given connection
     * @param message
     * @param player
     */
    private void broadcastExcept(String message, MessageSource source){
        message = message + "\n";
        for(ConnectionAgent player : players){
            if(!player.equals(source)){          
                player.sendMessage(message);
            }
        }
    }

    private void sendMessage(String message, MessageSource source){
        for(ConnectionAgent player : players){
            if(player.equals(source)){
                message = message + "\n";
                player.sendMessage(message);
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

    private void handleMessage(String message, MessageSource source){
        String[] cmdList = message.split(" ");
        int size = this.boardSize - 1;
        if(cmdList.length > 0){
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
            } else {
                //SEND BACK USAGE MESSAGE
                sendMessage(invalidCmd1 + size + "]> <[0-" + size + invalidCmd2, source);
            }
        } else {
            //Send back usage message
            sendMessage(invalidCmd1 + size + "]> <[0-" + size + invalidCmd2, source);
        }
    }

    private void playCmd(String[] cmds, MessageSource source){
        if(!playing){
            if(cmds[0].toLowerCase().equals("/play")){
                if(activePlayers >= 2){
                    broadcast(playerNames.get(getPlayerBySource(source)) + " has started the game!");
                    broadcast(playerNames.get(0) + " it is your turn!");
                    this.playing = true;
                } else {
                    sendMessage("There are not enough players to start the game.", source);
                }
            }
        } else {
            sendMessage("The game has already started.", source);
        }
    }

    private void quitCmd(String[] cmds, MessageSource source){
        int size = this.boardSize - 1;
        //We have to do for loop bc we get a ConcurrentModificationException
        //Here when using a for-each loop (not sure exactly why though)
        boolean first = true;
        for(int i = 0; i < playerNames.size(); i++){
            String name = playerNames.get(i);
            if(cmds.length > 1){
                if(name.equals(cmds[1])){
                    int player = playerNames.indexOf(cmds[1]);
                    broadcast(playerNames.get(player) + " has surrendered!");
                    removePlayer(player);
                }
            } 
            if(first){
                if(cmds.length == 1){
                    //Send back usage message
                    sendMessage(invalidCmd1 + size + "]> <[0-" + size + invalidCmd2, source);
                    first = false;
                }
            }
        }
        if(activePlayers == 1){
            this.playing = false;
        }
    }

    private void joinCmd(String[] cmds, MessageSource source){
        if(!playing){
            boolean validName = true;
            for(String name : playerNames){
                if(name.equals(cmds[1])){
                    validName = false;
                    sendMessage("The name " + cmds[1] + " is already in use. Please enter a new name.", source);
                    sendMessage("1", source);
                }
            }
            if(validName){
                this.activePlayers++;
                this.game.addPlayer();
                this.playerNames.add(cmds[1]);

                sendMessage("Welcome " + cmds[1] + "!", source);
                broadcastExcept(cmds[1] + " has connected!", source);
            }
        } else { 
            sendMessage("The game is already in progress. You cannot join.", source);
        }
    }

    private void showCmd(String[] cmds, MessageSource source){
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

    private void attackCmd(String[] cmds, MessageSource source){
        boolean validName = false;
        int pos = 0;
        if(cmds.length == 4){
            int i = 0;
            //Checking first name exists
            for(String person : playerNames){
                if(cmds[1].equals(person)){
                    pos = i;
                    validName = true;
                }
                i++;
            }
            
            //Check the dimensions
            if(validName){
                if(players.get(pos).equals(source)){
                    sendMessage("Error: Cannot attack yourself.", source);
                }
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
                                sendMessage("Attack Report:\n" + 
                                    this.game.getInactiveBoard(request), source);

                                
                                broadcastExcept(playerNames.get(requester) +
                                    " has attacked " + playerNames.get(request) + "!"
                                        , players.get(request));
                                
                                String sunk = this.game.getSunk(request);
                                if(!sunk.equals("")){
                                    broadcast(playerNames.get(requester) +
                                        " has sunk " + playerNames.get(request) + "'s " 
                                        + sunk + "!");
                                        this.game.setSunk(request, "");
                                }

                                sendMessage(playerNames.get(requester) + 
                                    " has attacked you!\nAttack Report:\n" +
                                    this.game.getActiveBoard(request), players.get(request));
                                if(playerDefeated){
                                    broadcast(playerNames.get(request) + " has been defeated!");
                                    removePlayer(request);
                                }
                                if(!checkWinConditions()){
                                    broadcast(playerNames.get((requester + 1) % players.size()) 
                                    + " it is your turn!");
                                }
                            }
                        }else{
                            sendMessage("It is currently " + playerNames.get(
                                this.game.getTurn() % players.size()) + "'s turn.", source);
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
            Thread t = this.threads.get(player);
            t.interrupt();
            this.threads.remove(player);
            this.game.removePlayerAt(player);
            this.activePlayers--;
            //TODO: invoke sourceClosed()?
        } catch (IOException e) {
            //Exceptions on this level should be printed to the server
            System.out.println("Error removing player");
        }
    }

    private boolean checkWinConditions(){
        if(playerNames.size() == 1 && playing){
            this.playing = false;
            String player = playerNames.get(0);
            sendMessage("Congratulations " + player + "!! You Win!!", players.get(0));
            
            this.game = new Game(boardSize);
            this.game.addPlayer();
            return true;
        }
        return false;
    }

    /**
     * Returns the index of the player who has the given source
     * @param source
     * @return
     */
    private int getPlayerBySource(MessageSource source){
        int index = -1;
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).equals(source)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}
