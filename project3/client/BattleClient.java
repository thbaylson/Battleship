/**
 * Authors: Tyler Baylson & Dillion Gorlesky
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
 * BattleClient is one of the classes that implement the client-side logic of this client-server application. It
 * is responsible for creating a ConnectionAgent, reading input from the user, and sending that input to the
 * server via the ConnectionAgent. The class implements the MessageListener interface (i.e., it can "observe"
 * objects that are MessageSources). The class also extends MessageSource, indicating that it also plays the
 * role of "subject" in an instance of the observer pattern.
 */
public class BattleClient implements MessageListener{

    private InetAddress host;
    private int port;
    private String username;
    public int boardSize;
    public boolean playing;
    public ArrayList<String> names;
    public int activePlayers;
    private String invalidCmd1 = "Valid Command are: \n\t /join <username>" +
                                "\n\t /play \n\t /attack <username> <target> <[0-";
    private String invalidCmd2 = "]>" +
                                "\n\t /quit <name>\n\t /show <username> <target>\n";

    /** 
     * This constructor is just for testing purposes to have 2 users on the same client for milestone 1
     * 
     */
    public BattleClient(String hostname, int port) throws UnknownHostException{
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.names = new ArrayList<String>();
    }

    public BattleClient(String hostname, int port, String username) throws UnknownHostException{
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
    }

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
                System.out.println("Not a proper size. Please enter a number between 5 and 10.");
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
            Game game = new Game(boardSize);
            this.boardSize--;
            System.out.println("To Join, Enter /join name");
           // Socket socket = new Socket(this.host, this.port);
            String command = s.nextLine();
            int i = 0;
            while(i == 0){//CHANGE FOREVER LOOP
            //while(!socket.isClosed()){
                command = s.nextLine();
                if(validCmd(command)){
                    String[] cmds = command.split(" ");
                    if(cmds[0].toLowerCase().equals("/join")){
                        game.addPlayer();
                        
                        
                    } else if(cmds[0].toLowerCase().equals("/play")){
                            
                    } else if(cmds[0].toLowerCase().equals("/attack")){
                            attacking(cmds);
                    } else if(cmds[0].toLowerCase().equals("/quit")){
                            if(activePlayers == 0){
                                s.close();
                                System.exit(0);
                            } 
                            //FIX QUITTING FOR FIRST COMMAND
                    } else if(cmds[0].toLowerCase().equals("/show")){
                            showing(cmds);
                    }
                } else {
                    System.out.println(invalidCmd1 + this.boardSize + "]> <[0-" + this.boardSize + invalidCmd2);
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
     * The purpose of this function is to handle any commands involving showing a board
     * for the game
     * @param commands: Arguments include <person_req> <target>
     */
    public void showing(String[] commands){

    }

    /**
     * This functions purpose is to use the commands from the user to attack 
     * @param commands: Arguments include <person_req> <target> <pos> <pos>
     */
    public void attacking(String[] commands){
        int row = Integer.parseInt(commands[3]);
        int col = Integer.parseInt(commands[4]);
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
        for(String name : names){
            if(name.equals(cmds[1])){
                System.out.println("Player: " + cmds[1] + " has surrendered.");
                names.remove(cmds[1]);
                activePlayers--;
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
            for(String name : names){
                if(name.equals(cmds[1])){
                    System.out.println("Error: " + cmds[1] + " is already in use. Please enter new name.");
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
                System.out.println("Error: " + cmds[1] + " Player name not found. Please retry.");
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
                System.out.println("Error: " + cmds[2] + " Player name not found. Please retry.");
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
        if(cmds.length < 5){
            System.out.println("Error: Not enough arguments given for attack command.");
            return false;
        } else {
            //Checking first name exists
            for(String person : names){
                if(cmds[1].equals(person)){
                    nameCheck = true;
                }
            }
            if(!nameCheck){
                System.out.println("Error: " + cmds[1] + " Player name not found. Please retry.");
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
                System.out.println("Error: " + cmds[2] + " Player name not found. Please retry.");
                return false;
            }
            try{
                int row = Integer.parseInt(cmds[3]);
                int col = Integer.parseInt(cmds[4]);
                if(!(row <= this.boardSize)){
                    nameCheck = false;
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmds[3] + " Is not on the board.");
                    return false;
                }

                if(!(col <= this.boardSize)){
                    nameCheck = false;
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmds[4] + " Is not on the board.");
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

    public void send(String msg){

    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        
    }
}