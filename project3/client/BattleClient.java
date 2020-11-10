/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

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
    public ArrayList<String> names;
    public int activePlayers = 0;
    private String invalidCmd = "Valid Command are: \n\t /join <username>" +
                                "\n\t /play \n\t /attack <username> <target> <[0-9]> <[0-9]>" +
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
        boolean playing, properSize = false;
        String buff;
        try{
                //GETTING SIZE OF BOARD HERE
            System.out.println("Enter board size (5-10): ");
            if(s.hasNextInt()){
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
            this.boardSize = s.nextInt();//GETTING SIZE OF BOARD HERE
            System.out.println("To Join, Enter /join name");
           // Socket socket = new Socket(this.host, this.port);
            String command;
            int i = 0;
            while(i == 0){
            //while(!socket.isClosed()){
                command = s.nextLine();
                if(validCmd(command)){
                    System.out.println("Valid");
                } else {
                    System.out.println("Invalid");
                }
            }
            //Send msg to other clients user quit
            //s.close();
            //socket.close();
        //} catch(IOException e){
            //System.out.println("check");
        } catch(NoSuchElementException e){ //DOING CTRL-C ON INPUT

        } catch(NumberFormatException e){

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
        boolean test = false;
        if(cmdList.length == 2){//Maybe can be removed in case they do /join Jack ghsbgk hgrks
            if(cmdList[0].toLowerCase().equals("/join")){//Join
                for(String name : names){
                    if(name.equals(cmdList[1])){
                        System.out.println("Error: " + cmdList[1] + " is already in use. Please enter new name.");
                        return false;
                    }
                }
                activePlayers++;
                names.add(cmdList[1]);
                return true;
            } else if(cmdList[0].toLowerCase().equals("/quit")){//QUIT
                for(String name : names){
                    if(name.equals(cmdList[1])){
                        System.out.println("Player: " + cmdList[1] + " has surrendered.");
                        names.remove(cmdList[1]);
                        activePlayers--;
                        return true;
                    }
                }
                System.out.println("Error: " + cmdList[1] + " is not a player. Please retry.");
                return false;
            }
        }
        if(cmdList[0].toLowerCase().equals("/play")){//PLAY
            if(activePlayers >= 2){
                System.out.println("Game can begin.");
            } else {
                System.out.println("Error: Not enough players.");
            }
            return true;
        }

        if(cmdList[0].toLowerCase().equals("/show")){//SHOW
            if(cmdList.length >= 3){
                //Checking first name exists
                boolean nameCheck = false;
                for(String person : names){
                    if(cmdList[1].equals(person)){
                        nameCheck = true;
                    }
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmdList[1] + " Player name not found. Please retry.");
                    return false;
                }
                //Checking target name exists
                nameCheck = false;
                for(String target : names){
                    if(cmdList[2].equals(target)){
                        nameCheck = true;
                    }
                }
                if(!nameCheck){
                    System.out.println("Error: " + cmdList[2] + " Player name not found. Please retry.");
                    return false;
                }
                return true;
            } else {
                System.out.println("Error: not enough arguments for show.");
                return false;
            }
        }

        if(cmdList[0].toLowerCase().equals("/attack")){
            if(cmdList.length < 5){
                System.out.println("Error: Not enough arguments given for attack command.");
                return false;
            } else {

            }
        }
        return test;
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
