/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.util.Random;

/**
 * BattleShipDriver contains the main() method for the server. It parses command
 * line options, instantiates a BattleServer, and calls its listen() method.
 * This takes two command line arguments, the port number for the server and the
 * size of the board (if the size is left off, default to size 10 x 10). You may
 * assume square arrays
 */
public class BattleShipDriver {
    
    public static void main(String[] args){
        int boardSize = 10;
        if(args.length == 1){
        } else {
            try {
                //System.out.println(boardSize);
                boardSize = Integer.parseInt(args[1]);
                if(boardSize < 2){
                    boardSize = 10;
                }
            }catch(NumberFormatException e){
                //TODO: print usage message
            }
        }

        System.out.println("Size: " + boardSize);
        Game game = new Game(boardSize);
        System.out.println("\nOriginal Board: \n");
        game.printBoards();
        for(int i = 0; i < Integer.parseInt(args[0]); i++){
            Random rand = new Random();
            int row = rand.nextInt(boardSize);
            int col = rand.nextInt(boardSize);
            //System.out.println("Attacking: col " + col + ", row " + row);
            game.attack(row, col);
        }
        System.out.println("\nAttacked Board: \n");
        game.printBoards();
    }
}
