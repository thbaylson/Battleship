/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import server.*;
import common.MessageListener;
import common.MessageSource;
import java.lang.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * BattleClient is one of the classes that implement the client-side logic of 
 * this client-server application. It is responsible for creating a 
 * ConnectionAgent, reading input from the user, and sending that input to the
 * server via the ConnectionAgent. The class implements the MessageListener 
 * interface (i.e., it can "observe" objects that are MessageSources). The 
 * class also extends MessageSource, indicating that it also plays the
 * role of "subject" in an instance of the observer pattern.
 * @implements MessageListener: Interface some functions are used from
 */
public class BattleClient implements MessageListener{

    private InetAddress host;
    private int port, turn;//Port # and who's turn it is
    private String username;
    private int boardSize;//Size of board
    private Game game;//Current game
    private boolean playing;//If game is in progress
    private ArrayList<String> names;//Names of currently playing players
    private int activePlayers;//Number of active players
    private final String invalidCmd1 = "Valid Command are: \n\t /join <username>" +
                                "\n\t /play \n\t /attack <username> <target> <[0-";
    private final String invalidCmd2 = "]>" +
                                "\n\t /quit <name>\n\t /show <username> <target>\n";

    /** 
     * This constructor is just for testing purposes to have 2 users on the same 
     * client for milestone 1
     * @param String: Hostname given
     * @param int: Port number given
     * @throws UnknownHostException: If host given is invalid
     */
    public BattleClient(String hostname, int port) throws UnknownHostException{
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.names = new ArrayList<String>();
    }

    /** 
     * This constructor is just for testing purposes to have users on the same 
     * client for milestone 1
     * @param String: Hostname given
     * @param int: Port number given
     * @param String: Username of client
     * @throws UnknownHostException: If host given is invalid
     */
    public BattleClient(String hostname, int port, String username) 
        throws UnknownHostException{
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
    }

    /**
     * The purpose of this function is to be the meat and bones of the 
     * program. It gets user input, makes sure the board size given is
     * between 5-10, validates input, provides a usage message, and
     * runs commands.
     */
    public void connect(){
        Scanner s = new Scanner(System.in);
        boolean playing = false, properSize = false;
        String buff;
        this.activePlayers = 0;
        try{              
            System.out.println("Enter board size (5-10): ");
            if(s.hasNextInt()){//GETTING SIZE OF BOARD HERE
                this.boardSize = s.nextInt();               
                if(this.boardSize < 5 || this.boardSize > 10){
                    properSize = false;
                } else {
                    properSize = true;  
                }               
            }
            while(!properSize){//Making sure its the right size
                System.out.println("Not a proper size. Please enter a " +
                    "number between 5 and 10.");
                if(s.hasNextInt()){
                    this.boardSize = s.nextInt();
                    if(this.boardSize < 5 || this.boardSize > 10){
                        properSize = false;
                    } else {
                        properSize = true;  
                    }               
                } else {
                    buff = s.next();
                }
            }
            this.game = new Game(boardSize);//Starting new game
            this.boardSize--;
            this.turn = 0;
            System.out.println("To Join, Enter /join name");
           // Socket socket = new Socket(this.host, this.port);
            String command = s.nextLine();
            int i = 0;
            while(i == 0){//Not a forever loop bc quit changes i
            //while(!socket.isClosed()){
                command = s.nextLine();
                if(validCmd(command)){
                    String[] cmds = command.split(" ");
                    if(cmds[0].toLowerCase().equals("/join")){
                        //Adding players to game
                        game.addPlayer();
                    } else if(cmds[0].toLowerCase().equals("/play")){
                        //Playing game
                            this.playing = true;
                            if(activePlayers > 1){
                                System.out.println("Player " + names.get(0) + ", it is your turn.");
                            }
                    } else if(cmds[0].toLowerCase().equals("/attack")){
                        //Attacking someone
                        if(this.playing){//makes sure game is in progress
                            if(turn == (names.indexOf(cmds[1]))){
                                //if its player turn
                                boolean value = attacking(cmds);
                                if(turn == (names.size() - 1)){
                                    turn = 0;
                                } else {
                                    turn++;
                                    
                                }
                                int index = names.indexOf(cmds[2]);
                                System.out.println(game.getInactiveBoard(index));
                                if(value){//If someone has lost
                                    activePlayers--;
                                    names.remove(index);
                                    game.removePlayerAt(index);
                                    if(this.activePlayers < 2){
                                        this.playing = false;
                                    }
                                } 
                                if(activePlayers > 1){
                                    System.out.println("Player " + names.get(turn) + ", it is your turn.");
                                } else {
                                    index = names.indexOf(cmds[1]);
                                    game.removePlayerAt(index);
                                    game.addPlayer();
                                }
                            } else {//not player turn
                                System.out.println("Error: It is not Player: " + 
                                    cmds[1] + " turn");
                            }
                        } else {//Game not begun
                            System.out.println("Error: Game has not been " +
                                "started yet.");
                        }
                    } else if(cmds[0].toLowerCase().equals("/quit")){
                        //Player wants to surrender
                            if(activePlayers == 0){
                                s.close();
                                System.exit(0);
                            } 
                    } else if(cmds[0].toLowerCase().equals("/show")){
                            showing(cmds);
                    }
                } else {
                    //Printing usage message
                    System.out.println(invalidCmd1 + this.boardSize + "]> <[0-" +
                        this.boardSize + invalidCmd2);
                }
            }
            //Send msg to other clients user quit
            //s.close();
            //socket.close();
        //} catch(IOException e){
            //System.out.println("check");
        } catch(NoSuchElementException e){ //DOING CTRL-C ON INPUT
            s.close();
            System.exit(1);
        } catch(NumberFormatException e){
            s.close();
            System.exit(1);
        }
    }

    /**
     * The purpose of this function is to handle any commands involving 
     * showing a board
     * for the game
     * @param commands: Arguments include <person_req> <target>
     */
    public void showing(String[] commands){
        String request = commands[1];
        String target = commands[2];
        if(request.equals(target)){
            int index = names.indexOf(request);
            //Person is asking for their own board
            System.out.println(game.getActiveBoard(index));
        } else {
            int index = names.indexOf(target);
            //Person is asking for someone elses board
            System.out.println(game.getInactiveBoard(index));
        }
    }

    /**
     * This functions purpose is to use the commands from the user to attack 
     * @param commands: Arguments include <person_req> <target> <pos> <pos>
     * @return boolean: True if player lost, else false
     */
    public boolean attacking(String[] commands){
        int row = Integer.parseInt(commands[3]);
        int col = Integer.parseInt(commands[4]);
        int index = names.indexOf(commands[2]);
        if(game.attack(index,row, col)){
            //Someone lost
            System.out.println("Player " + commands[2] + " has lost.");
            return true;
        }
        return false;
    }

    /**
     * The purpose of this function is to make sure the command entered 
     * is a valid one
     * @param String: String entered in console.
     * @return boolean: If its a valid command or not
     */
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

    /**
     * The purpose of this function is to handle the commands
     * related to playing, if there are enough players to play or
     * if game is already in progress.
     * @param String[]: Array of strings for the commands requested
     * @return boolean: Valid command or not
     */
    public boolean playCmd(String[] cmds){
        if(!playing){
            if(activePlayers >= 2){
                System.out.println("Game can begin.");
            } else {
                System.out.println("Error: Not enough players.");
            }
        } else {
            System.out.println("Error: Game already started.");
        }
        return true;
    }

    /**
     * The purpose of this function is to handle the commands
     * related to quitting, decrementing players, if the person 
     * trying to quit exists
     * @param String[]: Array of strings for the commands requested
     * @return boolean: Valid command or not
     */
    public boolean quitCmd(String[] cmds){
        if(activePlayers == 0){
            return true;
        }
        if(cmds.length == 1){
            return false;
        }
        for(String name : names){
            if(name.equals(cmds[1])){
                System.out.println("Player: " + cmds[1] + " has surrendered.");
                int index = names.indexOf(cmds[1]);
                names.remove(cmds[1]);
                activePlayers--;
                this.game.removePlayerAt(index);
                if(activePlayers == 1){
                    playing = false;
                }
                return true;
            }
        }
        System.out.println("Error: " + cmds[1] + " is not a player. "+
            "Please retry.");
        return false;
    }

    /**
     * The purpose of this function is to handle the commands
     * related to joining. If the name is being used or game is in
     * progress
     * @param String[]: Array of strings for the commands requested
     * @return boolean: Valid command or not
     */
    public boolean joinCmd(String[] cmds){
        if(!playing){
            if(cmds.length == 1){
                return false;
            }
            for(String name : names){
                if(name.equals(cmds[1])){
                    System.out.println("Error: " + cmds[1] + " is " +
                        "already in use. Please enter new name.");
                    return false;
                }
            }
            activePlayers++;
            names.add(cmds[1]);
        } else { 
            System.out.println("Error: Game already in progress. Can't join");
        }
        return true;
    }

    /**
     * The purpose of this function is to handle the commands
     * related to showing. Showing someone elses board, your
     * board, and if the names exist
     * @param String[]: Array of strings for the commands requested
     * @return boolean: Valid command or not
     */
    public boolean showCmd(String[] cmds){
        boolean nameCheck = false;
        if(cmds.length >= 3){
            //Checking first name exists
            for(String person : names){
                if(cmds[1].equals(person)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[1] + " Player name not "+
                    "found. Please retry.");
                return false;
            }
            //Checking target name exists
            nameCheck = false;
            for(String target : names){
                if(cmds[2].equals(target)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[2] + " Player name not "+
                    "found. Please retry.");
                return false;
            }
            return true;
        } else {
            System.out.println("Error: not enough arguments for show.");
            return false;
        }
    }

    /**
     * The purpose of this function is to handle the commands
     * related to attacking. If correct number of arguments are given, 
     * names exist, etc.
     * @param String[]: Array of strings for the commands requested
     * @return boolean: Valid command or not
     */
    public boolean attackCmd(String[] cmds){
        boolean nameCheck = false;
        if(cmds.length < 5){
            System.out.println("Error: Not enough arguments given for attack "+
                "command.");
            return false;
        } else {
            //Checking first name exists
            for(String person : names){
                if(cmds[1].equals(person)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[1] + " Player name not "+
                    "found. Please retry.");
                return false;
            }
            //Checking target name exists
            nameCheck = false;
            for(String target : names){
                if(cmds[2].equals(target)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[2] + " Player name not "+
                    "found. Please retry.");
                return false;
            }
            try{
                int row = Integer.parseInt(cmds[3]);
                int col = Integer.parseInt(cmds[4]);
                if(!(row <= this.boardSize)){
                    nameCheck = false;
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmds[3] + " Is not on "+
                        "the board.");
                    return false;
                }

                if(!(col <= this.boardSize)){
                    nameCheck = false;
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmds[4] + " Is not on "+
                        "the board.");
                    return false;
                }
                if(cmds[1].equals(cmds[2])){
                    System.out.println("Error: Player " + cmds[1] + 
                        " cannot attack themselves.");
                    return false;
                }
                return true;
            } catch(NumberFormatException e){
                return false;
            }
        }
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String msg, MessageSource source){

    }

    /**
     * Used to send messages to other observers
     * @param msg: Message being sent to other clients.
     */
    public void send(String msg){

    }

    /**
     * Used to notify observers that the subject will not receive new messages;
     *  observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        
    }
}