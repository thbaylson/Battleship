/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is the class responsible for sending messages to and receiving messages
 * from remote hosts. The class extends the MessageSource class, indicating that
 * it can play the role of the "subject" in an instance of the observer pattern.
 * The class also implements the Runnable interface, indicating that it
 * encapsulates the logic associated with a Thread.
 */
public class ConnectionAgent extends MessageSource {

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    public ConnectionAgent(Socket socket) throws IOException {
        this.thread = this.thread.currentThread();
        this.socket = socket;
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintStream(socket.getOutputStream());
    }

    public void sendMessage(String msg){
        // Maybe use this.in or this.out here
        this.notifyReceipt(msg);
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    /**
     * @throws IOException
     * 
     */
    public void close() throws IOException {
        //Close itself and the socket
        this.socket.close();
    }

    /**
     * Runs in its own thread. Waits for messages, calls notifyReciept() when a 
     * message is received.
     */
    public void run(){
        String msg;
        while(! this.thread.isInterrupted()){
            msg = this.in.nextLine();
            this.sendMessage(msg);
        }
    }

}