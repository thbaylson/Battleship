/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.util.ArrayList;
import java.util.Random;

/**
 * Game contains the logic for the game of BattleShip. It has a Grid for each
 * client.
 */
public class Game {
    private ArrayList<Grid> clients;
    
    public Game(int boardSize){
        clients = new ArrayList<>();
        clients.add(new Grid(boardSize));
        for(Grid g : clients){
            setPieces(g, boardSize);
        }
    }

    public void printBoards(){
        for(Grid g : clients){
            System.out.println(g);
        }
    }

    private void setPieces(Grid g, int boardSize){
        //findBounds returns an array of size 2. 
        //Values can be assumed to be [a low bound int, a high bound int]
        int[] bounds = findBounds(boardSize);
        Random rand = new Random();
        int numPieces= rand.nextInt(bounds[1] - bounds[0] + 1) + bounds[0];
        for(int i = 0; i < numPieces; i++){
            g.setRandomPiece();
        }
    }

    public void attack(int row, int col){
        clients.get(0).attack(row, col);
    }

    /**
     * Calculates the range of pieces a board should have given
     * the size of the board.
     * @param boardSize The size of the board.
     * @return An array of integers that represents bounds for number of pieces
     */
    private int[] findBounds(int boardSize){
        int lowBound = 0;
        int highBound = 0;

        if(boardSize > 7){
            lowBound = (boardSize/2-1);
            highBound = (boardSize/2+1);
        }else{
            lowBound = (boardSize/2-1);
            highBound = (boardSize/2);
        }
        int[] bounds = {lowBound, highBound};
        return bounds;
    }
}
