/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import server.*;
import common.*;
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
    private int port;
    private PrintStreamMessageListener printStream = new PrintStreamMessageListener(System.out);
    private String username;
    //private Game game;//Current game
    private ConnectionAgent connection;
    private final String invalidCmd1 = "Valid Command are: "+
                                "\n\t/play \n\t /attack <target> " +
                                "<[row]> <[col]> \n\t /quit\n\t /show <target>\n";

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
    }

    /**
     * The purpose of this function is to be the meat and bones of the 
     * program. It gets user input, makes sure the board size given is
     * between 5-10, validates input, provides a usage message, and
     * runs commands.
     */
    public void connect(){
        Scanner s = new Scanner(System.in);
        //Make new thread
        String command = "";
        try{              
            Socket socket = new Socket(this.host, this.port);
            //Making a connection agent 
            this.connection = new ConnectionAgent(socket);
            //join message
            while(!command.toLowerCase().equals("/quit")){
                command = s.nextLine();
                //SEND COMMAND TO SERVER
                String[] cmd = command.split(" ");
                if(cmd.length > 0){
                    send(command);
                }
                //Players can send commands to show boards and such while it isnt their 
                //turn but the server will check otehr commands such as attack to make sure its their turn
                //I can make sure the things sent are valid command lengths
            }
            //Send msg to other clients user quit
            //Close socket with sourceClosed()
            connection.close();
        } catch(IOException e){
            s.close();
            connection.close();
            System.out.println("check");
            System.exit(1);
        } catch(NoSuchElementException e){ //DOING CTRL-C ON INPUT
            s.close();
            connection.close();
            System.exit(1);
        } catch(NumberFormatException e){
            s.close();
            connection.close();
            System.exit(1);
        }
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String msg, MessageSource source){
        //There will be echo (same message sent to ConnectionAgent) received and printed again
        printStream.messageReceived(msg, source);
    }

    /**
     * Used to send messages to other observers
     * @param msg: Message being sent to other clients.
     */
    public void send(String msg){
        connection.sendMessage(msg);
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
        //ConnectionAgent is closing itself bc it doesn't know what its connected to
        // and doesn't care
        //The server will invoke my sourceClosed whenever I want to quit and let everyone else 
        //know via broadcast msg I have surrendered
        
        //Do all cleanup
        printStream.sourceClosed(source);
    }
}