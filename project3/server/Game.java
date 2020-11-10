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
    private ArrayList<Grid> players;
    private int boardSize;
    
    public Game(int boardSize){
        this.boardSize = boardSize;
        players = new ArrayList<>();
    }

    public void addPlayer(){
        Grid g = new Grid();
        setPieces(g, boardSize);
        players.add(g);
    }

    public void removePlayerAt(int index){
        players.remove(index);
    }

    public String getActiveBoard(int index){
        return players.get(index).toString();
    }

    public String getInactiveBoard(int index){
        String inactive = players.get(index).toString();
        String shipSymbols = "";
        for(int i = 0; i < ShipType.values().length; i++){
            shipSymbols += ShipType.values()[i].getSymbol();
        }
        String regex = "^([" + shipSymbols + "]){1}$";
        inactive.replaceAll(regex, " ");
        return inactive.toString();
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

    public void attack(int index, int row, int col){
        players.get(index).attack(row, col);
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
