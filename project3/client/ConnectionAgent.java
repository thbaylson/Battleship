/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is the class responsible for sending messages to and receiving messages from remote hosts.
 * The class extends the MessageSource class, indicating that it can play the role of the "subject" in an instance
 * of the observer pattern. The class also implements the Runnable interface, indicating that it encapsulates
 * the logic associated with a Thread.
 */
public class ConnectionAgent{

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    public ConnectionAgent(Socket socket){
        this.socket = socket;
    }

    public void sendMessage(String msg){

    }

    public boolean isConnected(){
        return false;
    }

    /**
     * Runs in its own thread. Waits for messages, calls notifyReciept() when a 
     * message is received.
     */
    public void close(){

    }

    public void run(){

    }

}