/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.io.IOException;
import java.util.Random;

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
    
    public static void main(String[] args){
        if(args.length == 2){
            try{
            int portNumber = Integer.parseInt(args[0]);
            int boardSize = Integer.parseInt(args[1]);

            BattleServer server = new BattleServer(portNumber, boardSize);
            while(!server.isClosed()){
                server.listen();
            }
            }catch(NumberFormatException nfe){
                //TODO: print usage
            }catch(IOException ioe){
                //Something went wrong with BattleServer
            }
        }else{
            //TODO: print usage message
        }

        /** Testing without clients
        System.out.println("Size: " + boardSize);
        
        Game game = new Game(boardSize);
        game.addPlayer();
        game.clearBoard(0);
        game.setPiece(0, ShipType.Carrier, Direction.DOWN, 0, 3);

        System.out.println("\nOriginal Board: \n");
        System.out.println(game.getActiveBoard(0));
        
        System.out.println("Using args[0] to attack that many random locations:");
        for(int i = 0; i < Integer.parseInt(args[0]); i++){
            Random rand = new Random();
            int row = rand.nextInt(boardSize);
            int col = rand.nextInt(boardSize);
            //System.out.println("Attacking: col " + col + ", row " + row);
            game.attack(0, row, col);
        }
        
        System.out.println("\nAttacked Board: \n");
        System.out.println(game.getActiveBoard(0));
        */

    }
}
