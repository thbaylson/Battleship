/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import common.*;

/**
 * BattleServer is one of the classes that implement the server-side logic of 
 * this client-server application. It is responsible for accepting incoming 
 * connections, creating ConnectionAgents, and passing the ConnectionAgent off 
 * to threads for processing. Implements the MessageListener interface (i.e., 
 * it can "observe" objects that are MessageSources).
 */
public class BattleServer implements MessageListener{
    public static final int DEFAULT_PORT = 5500;
    public static final int DEFAULT_BOARDSIZE = 10;
    public int port;
    public int boardSize;
    
    public BattleServer(){
       this.port = DEFAULT_PORT;
       this.boardSize = DEFAULT_BOARDSIZE; 
    }

    public BattleServer(int port){
        this.port = port;
        this.boardSize = DEFAULT_BOARDSIZE;
    }

    public BattleServer(int port, int boardSize){
        this.port = port;
        this.boardSize = boardSize;
    }

    
    public void listen(){
        try{
            ServerSocket serSocket = new ServerSocket(this.port);
            Game game = new Game(boardSize);
            while(!serSocket.isClosed()){
                Socket socket = serSocket.accept();
                InputStream inputStream = socket.getInputStream();
                
                
                game.printBoards();
            }
            serSocket.close();
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(1);
        } catch(IOException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void broadcast(String message){

    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sourceClosed(MessageSource source) {
        // TODO Auto-generated method stub

    }


}
