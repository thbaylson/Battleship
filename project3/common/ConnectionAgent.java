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
public class ConnectionAgent extends MessageSource implements Runnable{

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    /**
     * This constructors purpose is to create new input, output
     * streams as well ascall its super constructor to initialize it.
     * @param SSocket: The socket we wish to get input and output streams
     * for
     * @throws IOException: IOException thrown at runtime
     */
    public ConnectionAgent(Socket socket) throws IOException {
        super();
        this.socket = socket;
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintStream(socket.getOutputStream());
    }

    /**
     * The purpose of this function is sending a message to the 
     * PrintStream of the socket.
     * @param String: Message being sent
     */
    public void sendMessage(String msg){
        this.out.println(msg);
    }

    /**
     * This function checks if the socket is still connected
     * @return boolean: True or false if socket is connected
     */
    public boolean isConnected(){
        return socket.isConnected();
    }

    /**
     * The purpose of this function is to do final cleanup when closing a
     * connectionAgent
     * @throws IOException: IOException thrown during runtime
     */
    public void close() throws IOException {
        //Close itself and the socket
        this.socket.close();
        this.closeMessageSource();
    }

    /**
     * Runs in its own thread. Waits for messages, calls notifyReciept() when a 
     * message is received.
     */
    public void run(){
        String msg;
        this.thread = this.thread.currentThread();
        while(!this.thread.isInterrupted()){
            if(this.in.hasNext()){//While theres input
                msg = this.in.nextLine();
                notifyReceipt(msg);
            }
        }
    }

}