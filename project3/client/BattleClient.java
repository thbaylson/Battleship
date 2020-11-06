/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import java.net.InetAddress;

/**
 * BattleClient is one of the classes that implement the client-side logic of this client-server application. It
 * is responsible for creating a ConnectionAgent, reading input from the user, and sending that input to the
 * server via the ConnectionAgent. The class implements the MessageListener interface (i.e., it can "observe"
 * objects that are MessageSources). The class also extends MessageSource, indicating that it also plays the
 * role of "subject" in an instance of the observer pattern.
 */
public class BattleClient {

    private InetAddress host;
    private int port;
    private String username;

    public BattleClient(String hostname, int port, String username){
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
    }

    public void connect(){

    }

    public void messageReceived(String msg, MessageSource source){

    }

    public void sourceClose(MessageSource source){

    }

    public void send(String msg){

    }
    
}
