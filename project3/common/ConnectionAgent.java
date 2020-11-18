/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package common;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is the class responsible for sending messages to and receiving messages from remote hosts.
 * The class extends the MessageSource class, indicating that it can play the role of the "subject" in an instance
 * of the observer pattern. The class also implements the Runnable interface, indicating that it encapsulates
 * the logic associated with a Thread.
 */
public class ConnectionAgent extends MessageSource{

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    public ConnectionAgent(Socket socket){
        this.thread = this.thread.currentThread();
        this.socket = socket;
    }

    public void sendMessage(String msg){
        this.notifyReceipt(msg);
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    /**
     * 
     */
    public void close(){
        //Close itself and the socket
    }

    /**
     * Runs in its own thread. Waits for messages, calls notifyReciept() when a 
     * message is received.
     */
    public void run(){
        while(! this.thread.isInterrupted()){
            //listen
        }
    }

}