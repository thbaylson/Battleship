/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import server.*;
import common.ConnectionAgent;
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
    private int port;
    private String echo;
    private Scanner s;
    private Socket socket;
    private PrintStreamMessageListener printStream = new PrintStreamMessageListener(System.out);
    private String username;

    private ArrayList<Thread> threads;
    private ConnectionAgent connection;
    private final String invalidCmd1 = "Valid Command are: "+
                                "\n\t /join <name>\n\t /play \n\t "+ 
                                "/attack <target> <[row]> <[col]>" +
                                "\n\t /quit\n\t /show <target>\n";
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
        this.threads = new ArrayList<>();
    }

    public void connect() {
        this.s = new Scanner(System.in);
        String command = "";
        try{              
            this.socket = new Socket(this.host, this.port);
            System.out.println(socket.toString());
            
            //Making a connection agent 

            this.connection = new ConnectionAgent(socket);
            Thread t = new Thread(this.connection);
            t.start();

            /**Thread t = new Thread(connection);
            t.start();
            threads.add(t);*/
            
            //SEND A join message HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            this.echo = "/join " + this.username;
            send("/join " + this.username);
            while(!command.toLowerCase().equals("/quit")){
                command = s.nextLine();
                //SEND COMMAND TO SERVER
                String[] cmd = command.split(" ");
                    //What happens if they hit enter and cmd[0] is null
                    this.echo = command;
                    send(command);
                    //I got rid of the if statement here because they can still send 
                    // other invalid commands to server

                //Players can send commands to show boards and such while it isnt their 
                //turn but the server will check otehr commands such as attack to make sure its their turn
            }
            //Send msg to other clients user quit
            //Close socket with sourceClosed()
            t.interrupt();
            connection.close();
        } catch(IOException e){
            s.close();
            //connection.close();
            System.out.println("check");
            System.exit(1);
        } catch(NoSuchElementException e){ //DOING CTRL-C ON INPUT
            s.close();
            //connection.close();
            System.exit(1);
        } catch(NumberFormatException e){
            s.close();
            //connection.close();
            System.exit(1);
        }
    }

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
                    System.out.println("Error: Cannot join a game in which a player"+
                        " is already playing.");
                }
            }
            //Returns true if its an invalid command and server sends back useage message
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
        //There will be echo (same message sent to ConnectionAgent) received and printed again
        System.out.println(msg);
        //if(!this.echo.equals(msg)){
            //THERE IS NO ECHO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        printStream.messageReceived(msg, source);
        //}
    }

    public void send(String msg){
        System.out.println("Sending from client to connection agent");
        //connection.sendMessage(msg);
    }   

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
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
        this.s.close();
        printStream.sourceClosed(source);
    }
}