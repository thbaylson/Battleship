/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;

import common.MessageListener;
import common.MessageSource;

/**
 * BattleServer is one of the classes that implement the server-side logic of
 * this client-server application. It is responsible for accepting incoming
 * connections, creating ConnectionAgents, and passing the ConnectionAgent off
 * to threads for processing. Implements the MessageListener interface (i.e., it
 * can "observe" objects that are MessageSources).
 */
public class BattleServer implements MessageListener {

    private int port;
    private ServerSocket server;
    private int current;
    private Game game;

    public BattleServer(int port, int boardSize) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
        this.game = new Game(boardSize);
    }

    /**
     * Constandtly listening
     */
    public void listen(){

    }

    public void broadcast(String message){

    }

    public boolean isClosed(){
        return server.isClosed();
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sourceClosed(MessageSource source) {
        // TODO Auto-generated method stub

    }

    /**
     * Connection agent is key
     * Client side -> Port param
     * & set up server side (on its own thread)
     * 
     * Each client gets a connection agent
     */

}
