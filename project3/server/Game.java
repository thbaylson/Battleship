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
    private int shipAmt;
    private ArrayList<String> username;
    
    public Game(int boardSize){
        this.boardSize = boardSize;
        players = new ArrayList<>();
        int[] bounds = findBounds(boardSize);
        Random rand = new Random();
        this.shipAmt= rand.nextInt(bounds[1] - bounds[0] + 1) + bounds[0];
    }

    public void addPlayer(){
        Grid g = new Grid(boardSize);
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
        // For each ShipType symbol, remove it from the 'inactive' board
        for(int i = 0; i < ShipType.values().length; i++){
            inactive = inactive.replaceAll(
                ShipType.values()[i].getSymbol()+"", " ");
        }
        return inactive;
    }

    /**
     * Place pieces of a specific ShipType and Direction in a specific place
     * on a specific player's board.
     * @param type
     * @param d
     * @param row
     * @param col
     */
    public void setPiece(int player, ShipType type, Direction d, int row, int col){
        players.get(player).setPiece(type, d, row, col);
    }

    private void setPieces(Grid g, int boardSize){
        //findBounds returns an array of size 2. 
        //Values can be assumed to be [a low bound int, a high bound int]
        int[] bounds = findBounds(boardSize);
        Random rand = new Random();
        int numPieces= rand.nextInt(bounds[1] - bounds[0] + 1) + bounds[0];
        for(int i = 0; i < this.shipAmt; i++){
            g.setRandomPiece();
        }
    }

    public boolean attack(int index, int row, int col){
        if(players.get(index).attack(row, col)){
            return true;
        }
        return false;
    }

    public void clearBoard(int index){
        players.get(index).clearBoard();
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
