/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * BattleShipDriver contains the main() method for the server. It parses command
 * line options, instantiates a BattleServer, and calls its listen() method.
 * This takes two command line arguments, the port number for the server and the
 * size of the board (if the size is left off, default to size 10 x 10). You may
 * assume square arrays
 */
public class BattleShipDriver {

    private static final int DEFAULT_BOARD_SIZE = 10;
    private static final int DEFAULT_PORT = 5500;
    
    /**
     * This is the beginning function for teh server side. It will parse in
     * CMA's, and create a new BattleServer to handle incoming clients.
     * @param args: Port and Board size given
     */
    public static void main(String[] args){
        if(args.length == 2){//Port and board size given
            try{
                int portNumber = Integer.parseInt(args[0]);
                int boardSize = Integer.parseInt(args[1]);

                BattleServer server = new BattleServer(portNumber, boardSize);
                while(!server.isClosed()){
                    server.listen();
                }
            }catch(NumberFormatException nfe){//Input wasn't integeers
                System.out.println("Invalid Port or Boardsize. Please Retry.");
                System.out.println("Usage: java server.BattleShipDriver <port>"+
                    " <boardsize>");
                System.exit(1);
            }catch(IOException ioe){
                System.out.println("Error: Something went wrong with "+
                    "BattleShipDriver, please retry.");
                System.out.println("Usage: java server.BattleShipDriver"+
                    " <port> <boardsize>");
                System.exit(1);
            }
        }else if(args.length == 1){
            try{
                int portNumber = Integer.parseInt(args[0]);
                int boardSize = DEFAULT_BOARD_SIZE;

                BattleServer server = new BattleServer(portNumber, boardSize);
                while(!server.isClosed()){
                    server.listen();
                }
            }catch(NumberFormatException nfe){//Port wasn't an int
                System.out.println("Invalid Port or Boardsize. Please Retry.");
                System.out.println("Usage: java server.BattleShipDriver <port>"+
                    " <boardsize>");
                System.exit(1);
            }catch(IOException ioe){
                System.out.println("Error: Something went wrong with "+
                    "BattleShipDriver, please retry.");
                System.out.println("Usage: java server.BattleShipDriver"+
                    " <port> <boardsize>");
                System.exit(1);
            }
        }else if(args.length == 0){
            try{
                int portNumber = DEFAULT_PORT;
                int boardSize = DEFAULT_BOARD_SIZE;

                BattleServer server = new BattleServer(portNumber, boardSize);
                while(!server.isClosed()){
                    server.listen();
                }
            }catch(IOException ioe){
                System.out.println("Error: Something went wrong with "+
                    "BattleShipDriver, please retry.");
                System.out.println("Usage: java server.BattleShipDriver"+
                    " <port> <boardsize>");
                System.exit(1);
            }
        } else {//Not enough CMA
            System.out.println("Error: Not enough command line arguments "+
                "given.");
            System.out.println("Usage: java server.BattleShipDriver <port>"+
                " <boardsize>");
            System.exit(1);
        }
    }
}
