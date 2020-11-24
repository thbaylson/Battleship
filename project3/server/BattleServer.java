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
    private ArrayList<ConnectionAgent> players;//Used to store any CA found
    private ArrayList<String> playerNames;//Used to store player names when
                                        //They join
    private ArrayList<Thread> threads;//Used to relay messages properly
    private final String invalidCmd1 = "Valid Commands are: \n\t /join "+
                                "<username>" +
                                "\n\t /play \n\t /attack <target> <[0-";
    private final String invalidCmd2 = "]>" +
                                "\n\t /quit <name>\n\t /show <target>\n";

    /**
     * The purpose of this constructor is to initialize the variables and create
     * a new server socket from the port.
     * @param port: Port given from CMA
     * @param boardSize: BoardSize given from CMA
     * @throws IOException: Throws IOException during runtime
     */
    public BattleServer(int port, int boardSize) throws IOException {
        /**Board size during the game*/
        this.boardSize = boardSize;

        this.server = new ServerSocket(port);

        /**Game object */
        this.game = new Game(boardSize);

        /**List of player ConnectionAgents*/
        this.players = new ArrayList<>();

        /**Player Names*/
        this.playerNames = new ArrayList<String>();

        /**Threads of ConnectionAgents*/
        this.threads = new ArrayList<>();

        /**If game is being played*/
        this.playing = false;

        /**Number of current players*/
        this.activePlayers = 0;
    }

    /**
     * This function is constantly listening for any new connections to the
     * ServerSocket where it creates a new ConnectionAgent, and adds a 
     * MessageListener
     * 
     * @throws IOException: Throws IOException during runtime
     */
    public void listen() throws IOException {
        Socket socket = this.server.accept();
        ConnectionAgent ca = new ConnectionAgent(socket);
        ca.addMessageListener(this);
        Thread thread = new Thread(ca);
        thread.start();
        threads.add(thread);
        players.add(ca);    
    }

    /**
     * This function sends a message to every ConnectionAgent connected
     * @param message: Message being sent to everyone
     */
    private void broadcast(String message){
        message = message + "\n";//Adding spacing between messages
        for(ConnectionAgent player : players){
            player.sendMessage(message);
        }
    }

    /**
     * Broadcasts a message to every connection except the given connection
     * @param message: Message being sent to everyone
     * @param source: Player that will not receive the message
     */
    private void broadcastExcept(String message, MessageSource source){
        message = message + "\n";//Adding spacing to output
        for(ConnectionAgent player : players){
            if(!player.equals(source)){          
                player.sendMessage(message);
            }
        }
    }

    /**
     * The purpose of this function is to send a message to one individual
     * @param message: Message being sent
     * @param source: Player the message is going to
     */
    private void sendMessage(String message, MessageSource source){
        for(ConnectionAgent player : players){
            if(player.equals(source)){
                message = message + "\n";//Adding spacing to output
                player.sendMessage(message);
            }
        }
    }

    /**
     * The purpose of this function is to see if the server socket has been
     * closed
     * @return boolean: True or false if its been closed
     */
    public boolean isClosed(){
        return server.isClosed();
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        handleMessage(message, source);
    }

    /**
     * The purpose of this function is to properly manage and divide all 
     * incoming messages to their appropriate functions & also make sure
     * they are valid commands
     * @param message: Message from client
     * @param source: Client the message came from
     */
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
                //SEND BACK USAGE MESSAGE, Not proper message
                sendMessage(invalidCmd1 + size + "]> <[0-" + size + invalidCmd2,
                     source);
            }
        } else {
            //Send back usage message
            sendMessage(invalidCmd1 + size + "]> <[0-" + size + invalidCmd2,
                 source);
        }
    }

    /**
     * The purpose of this function is to handle the commands when it comes
     * to playing
     * @param String[]: Split up line for commands
     * @param MessageSource: The ConnectionAgent the message came from
     */
    private void playCmd(String[] cmds, MessageSource source){
        if(!playing){//If not playing
            if(cmds[0].toLowerCase().equals("/play")){//make sure command is
                                                //Play
                if(activePlayers >= 2){//If there are at least 2 players
                    broadcast(playerNames.get(getPlayerBySource(source)) +
                         " has started the game!");
                    broadcast(playerNames.get(0) + " it is your turn!");
                    broadcast("pTurn: " + this.game.getTurn());
                    this.playing = true;
                } else {//Not enough players
                    sendMessage("There are not enough players to start the "+
                        "game.", source);
                }
            }
        } else {//trying to play when game has started
            sendMessage("The game has already started.", source);
        }
    }

    /**
     * The purpose of this function is to handle all commands relating to users
     * quitting the game.
     * @param cmds: Commands given by client
     * @param source: Which client sent the message
     */
    private void quitCmd(String[] cmds, MessageSource source){
        int size = this.boardSize - 1;
        //We have to do for loop bc we get a ConcurrentModificationException
        //Here when using a for-each loop (not sure exactly why though)
        boolean first = true;
        for(int i = 0; i < playerNames.size(); i++){
            String name = playerNames.get(i);
            if(cmds.length > 1){//Checks it's /quit <name>
                if(name.equals(cmds[1])){
                    int player = playerNames.indexOf(cmds[1]);
                    broadcast(playerNames.get(player) + " has surrendered!");
                    removePlayer(player);//removing player information
                    checkWinConditions();
                }
            } 
            if(first){//If this isn't here it prints the usage message twice
                if(cmds.length == 1){
                    //Send back usage message
                    sendMessage(invalidCmd1 + size + "]> <[0-" + size + 
                        invalidCmd2, source);
                    first = false;
                }
            }
        }
    }

    /**
     * The purpose of this function is to handle all commands relating to users
     * joining the game.
     * @param cmds: Commands given by client
     * @param source: Which client sent the message
     */
    private void joinCmd(String[] cmds, MessageSource source){
        if(!playing){//Can't join when playing
            boolean validName = true;
            for(String name : playerNames){
                if(name.equals(cmds[1])){//Makes sure name isn't already used
                    validName = false;
                    sendMessage("The name " + cmds[1] + " is already in use. "+
                        "Please enter a new name.", source);
                    sendMessage("1", source);//Alerts the client to make username
                        //Null so they can do /join <name> again
                }
            }
            if(validName){
                //If name isn't used it makes a new board and adds the player
                this.activePlayers++;
                this.game.addPlayer();
                this.playerNames.add(cmds[1]);

                sendMessage("Welcome " + cmds[1] + "!", source);
                broadcastExcept(cmds[1] + " has connected!", source);
            }
        } else { 
            sendMessage("The game is already in progress. You cannot join.",
                 source);
        }
    }

    /**
     * The purpose of this function is to handle all commands relating to users
     * showing boards during the game.
     * @param cmds: Commands given by client
     * @param source: Which client sent the message
     */
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
                //Getting if its the person asking for their own or someone
                //elses board
                int request = playerNames.indexOf(cmds[1]);
                int requester = players.indexOf(source);
                if(request == requester){
                    sendMessage(this.game.getActiveBoard(request), source);
                } else {
                    sendMessage(this.game.getInactiveBoard(request), source);
                }
            } else{
                //Person requested not found
                sendMessage(cmds[1] + " could not be found. Please retry.",
                     source);
            }
        } else {
            sendMessage("Usage: \\show [player_name]", source);
        }    
    }

    /**
     * The purpose of this function is to handle all commands relating to users
     * attacking during the game.
     * @param cmds: Commands given by client
     * @param source: Which client sent the message
     */
    private void attackCmd(String[] cmds, MessageSource source){
        boolean validName = false;
        int pos = 0;
        if(cmds.length == 4){
            int i = 0;
            //Checking person being attacked exists
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
                    
                    //Verifying row and col are in boards boundaries
                    boolean validDimensions =
                             Grid.verifyIndex(this.boardSize, row, col);
                    
                    //Check if it's the player's turn
                    if(validDimensions){
                        if(requester == (this.game.getTurn() % playerNames.size())){
                            //Prevent players from attacking themselves
                            if(request != requester){
                                boolean playerDefeated = 
                                    this.game.attack(request, row, col);
                                //Send a report back to the person attacking
                                sendMessage("Attack Report:\n" + 
                                    this.game.getInactiveBoard(request), source);

                                //Broadcasting that the person attacked someone
                                broadcastExcept(playerNames.get(requester) +
                                    " has attacked " + playerNames.get(request)+
                                         "!"
                                        , players.get(request));
                                
                                //Checking if a ship was sunk
                                String sunk = this.game.getSunk(request);
                                if(!sunk.equals("")){//If there was it prints
                                    //To everyone
                                    broadcast(playerNames.get(requester) +
                                        " has sunk " + playerNames.get(request) +
                                        "'s " + sunk + "!");
                                        this.game.setSunk(request, "");
                                }
                                
                                //Sends message to person who was attacked
                                //that they were attacked and by who
                                sendMessage(playerNames.get(requester) + 
                                    " has attacked you!\nAttack Report:\n" +
                                    this.game.getActiveBoard(request), 
                                        players.get(request));
                        
                                //If someone has lost
                                if(playerDefeated){
                                    broadcast(playerNames.get(request) + 
                                        " has been defeated!");
                                    removePlayer(request);
                                }
                                //Changes who's turn it is
                                if(!checkWinConditions()){
                                    broadcast("aTurn: " + this.game.getTurn());
                                    broadcast(playerNames.get((requester + 1) 
                                        % playerNames.size()) 
                                    + " it is your turn!");
                                }
                            }
                        }else{
                            //Trying to attack when its someone elses turn
                            sendMessage("It is currently " + playerNames.get(
                                this.game.getTurn() % playerNames.size()) + 
                                    "'s turn.", source);
                        }
                    } else{
                        //Improper input
                        sendMessage("Usage: //attack [player_name] [row] "+
                            "[column]", source);
                    }
                } catch(NumberFormatException e){
                    //Row or col weren't ints
                    sendMessage("Usage: //attack [player_name] [row] [column]",
                        source);
                }
            }else{
                //Person being attacked doesn't exist
                sendMessage(cmds[1] + " could not be found. Please retry.", 
                    source);
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
        } catch (IOException e) {
            //Exceptions on this level should be printed to the server
            System.out.println("Error removing player");
        }
    }

    /**
     * This function is used to check if there is only one person remaining
     * and tell them they've won
     * @return boolean: If they've won or not
     */
    private boolean checkWinConditions(){
        if(playerNames.size() == 1 && playing){
            this.playing = false;
            String player = playerNames.get(0);
            sendMessage("Congratulations " + player + "!! You Win!!",
                 players.get(0));

            this.game = new Game(boardSize);
            this.game.addPlayer();
            return true;
        }
        return false;
    }

    /**
     * Returns the index of the player who has the given source
     * @param source: The ConnectionAgent we wish to find the index of
     * @return int: Position in players
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

    /**
     * Used to notify observers that the subject will not receive new messages; 
     * observers can deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}
