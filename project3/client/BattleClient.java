/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
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
 * @extends MessageSource: ConnectionAgent is an instance of MessageSource
 */
public class BattleClient extends MessageSource implements MessageListener{

    private InetAddress host;
    private int port;
    private Scanner s;
    private Socket socket;
    private PrintStreamMessageListener printStream;
    private String username;
    private ConnectionAgent connection;

    /** 
     * This constructor is to initialize global variables passed from client
     * driver.
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
        this.printStream = new PrintStreamMessageListener(System.out);
        this.addMessageListener(printStream);
    }

    /**
     * The purpose of this function is to create a new ConnectionAgent
     * whenever a new client joins by creating a nw thread and waiting for user
     * input.
     */
    public void connect() {
        this.s = new Scanner(System.in);
        String command = "";
        try{            
            //Creating new socket  
            this.socket = new Socket(this.host, this.port);
            
            //Making a connection agent 
            this.connection = new ConnectionAgent(socket);
            connection.addMessageListener(this);
            //Creating new thread of a connection agent
            Thread t = new Thread(this.connection);
            t.start();
            
            //SEND A join message
            send("/join " + this.username);
            while(connection.isConnected()){
                command = s.nextLine();
                //Making sure command isn't /join while username is used.
                //And also forcing other players to quit that isn't themselves
                if(!checkCmd(command)){
                    //SEND COMMAND TO SERVER 
                    send(command);
                } 
                //If user wishes to quit the program it alerts the server to 
                //remove them then closes all connections.
                if(quitting(command)){
                    t.interrupt();
                    connection.close();
                    sourceClosed(this);
                }
                //Players can send commands to show boards and such while it 
                //isnt their turn but the server will check otehr commands 
                //such as attack to make sure its their turn
            }
            //Close socket with sourceClosed()
            t.interrupt();
            connection.removeMessageListener(this);
            connection.close();
            this.closeMessageSource();
        } catch(IOException e){
            s.close();
            System.out.println("Error: An IOException has occurred." +
                " Make sure the command line arguments you inputted " +
                " are valid and retry.");
            System.exit(1);
        } catch(NoSuchElementException e){ //DOING CTRL-C ON INPUT
            s.close();
            System.exit(1);
        } catch(NumberFormatException e){//Input isn't integers
            s.close();
            System.out.println("Number format exception");
            System.exit(1);
        }
    }

    /**
     * The purpose of this function is to see if the command to quit is valid
     * @param String: Command the user wants to quit
     * @return boolean: If quit command is /quit <name>
     */
    public boolean quitting(String command){
        String[] cmd = command.split(" ");
        if(cmd[0].toLowerCase().equals("/quit")){
            if(cmd.length > 1){
                return true;
            }
        }
        return false;
    }

    /**
     * The purpose of this function is to check and make sure a user can't join
     * a game that is already in use, handles it if they need to rejoin, and
     * also handles trying to quit other players
     * @param String: Command given form user
     * @return boolean: If command given is valid to send to server
     */
    public boolean checkCmd(String command){
        String[] cmd = command.split(" ");
        if(cmd[0].toLowerCase().equals("/join")){
            if(this.username != null){//If user is still active
                System.out.println("Error: Cannot join a new player with "+
                    "used client.");
                return true;
            } else {
                if(cmd.length > 1){
                    this.username = cmd[1];//Setting new username
                    try {//Creating new connectionAgents
                        this.connection = new ConnectionAgent(socket);
                        connection.addMessageListener(this);
                    } catch (IOException e) {
                        System.out.println("Error: An Error occurred while " +
                            " trying to rejoin.  Please retry.");
                    }
                    //Starting thread to talk to server with
                    Thread t = new Thread(this.connection);
                    t.start();
                    return false;
                }
            }
        } else if(cmd[0].toLowerCase().equals("/quit")){
            if(cmd.length != 1){
                if(!cmd[1].equals(this.username)){//Cant quit other players
                    System.out.println("Error: Cannot quit another player" +
                        " that is not yourself.");
                        return true;
                } else {
                    this.username = null;
                }
            }
        }
        return false;
    }

    /**
     * The purpose of this function it to make sure the commands entered have 
     * the correct length if they're valid commands.
     * @param String[]: Array of the commands given
     * @return boolean: If length is valid or if its an invalid command
     */
    public boolean validLength(String[] cmd){
        if(cmd.length > 0){
            if(cmd[0].toLowerCase().equals("/attack")){
                if(cmd.length == 4){
                   return true;
                }
            } else if(cmd[0].toLowerCase().equals("/play")){
               if(cmd.length == 1){
                    return true;
                }
            } else if(cmd[0].toLowerCase().equals("/quit")){
                if(cmd.length == 1){
                    this.username = "";
                    return true;
                }
            } else if(cmd[0].toLowerCase().equals("/show")){
                if(cmd.length == 2){
                    return true;
                }
            } else if(cmd[0].toLowerCase().equals("/join")){
                if(this.username.equals("")){
                    //They can't join a second time
                    if(cmd.length == 2){
                        return true;
                    }
                } else {
                    System.out.println("Error: Cannot join a game in which a "+
                    "player is already playing.");
                }
            }
            //Returns true if its an invalid command and server sends back 
            //useage message
            return true;
        }
        return true;
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String msg, MessageSource source){
        if(msg.equals("1")){//Letting client know they can quit safely
            this.username = null;
        } else {//Prints out message received
            printStream.messageReceived(msg, source);
            //If a player loses, it closes the connection
            if(msg.equals(this.username + " has been defeated!")){
                sourceClosed(this);
            } 
        }
    }

    /**
     * The purpose of this function is to send messages to the connection 
     * agent so it can be delivered to the server
     * @param String: msg to be sent
     */
    public void send(String msg){
        connection.sendMessage(msg);
    }   

    /**
     * Used to notify observers that the subject will not receive new messages; 
     * observers can deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        //ConnectionAgent is closing itself bc it doesn't know what its 
        //connected to
        // and doesn't care
        //The server will invoke my sourceClosed whenever I want to quit
        // and let everyone else 
        //know via broadcast msg I have surrendered
        
        //Do all cleanup and exit gracefully
        connection.removeMessageListener(this);
        source.removeMessageListener(this);
        this.closeMessageSource();
        System.exit(0);
    }
}