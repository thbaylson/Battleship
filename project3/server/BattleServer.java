/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import common.*;

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
    private ArrayList<ConnectionAgent> players;
    private ArrayList<Thread> threads;

    public BattleServer(int port, int boardSize) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
        this.game = new Game(boardSize);
        this.players = new ArrayList<>();
        this.threads = new ArrayList<>();
    }

    /**
     * Constantly listening
     * 
     * @throws IOException
     */
    public void listen() throws IOException {
        System.out.println("Start of Listen");
        Socket socket = this.server.accept();
        ConnectionAgent ca = new ConnectionAgent(socket);
        ca.addMessageListener(this);
        Thread thread = new Thread(ca);
        thread.run();
        threads.add(thread);
        players.add(ca);

        broadcast("A player has connected");

        if(this.server.isClosed()){
            for(Thread th : threads){
                th.interrupt();
            }
        }
        
        //ca.addMessageListener(client);

        //InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        //BufferedReader br = new BufferedReader(isr);
        //System.out.println(br.readLine());
    }

    public void broadcast(String message){
        for(ConnectionAgent player : players){
            player.sendMessage(message);
        }
    }

    public boolean isClosed(){
        return server.isClosed();
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        System.out.println(message);

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
