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

    public PrintStreamMessageListener(PrintStream out) {
        this.out = out;
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        this.out.println(message);
    }

    @Override
    public void sourceClosed(MessageSource source) {
        // TODO Auto-generated method stub

    }
    
}
