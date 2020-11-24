/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import common.MessageListener;
import common.MessageSource;

import java.io.PrintStream;

/**
 * PrintStreamMessageListener is a class that is responsible for writing
 * messages to a PrintStream (e.g., System.out). The class implements the
 * MessageListener interface, indicating that it plays the role of "observer" in
 * an instance of the observer pattern.
 */
public class PrintStreamMessageListener implements MessageListener {

    private PrintStream out;

    /**
     * The purpose of this constructor is to initialize the 
     * PrintStream variable.
     * @param out: PrintStream we wish to print to
     */
    public PrintStreamMessageListener(PrintStream out) {
        this.out = out;
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        this.out.println(message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; 
     * observers can deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        this.out.close();
    }
    
}
