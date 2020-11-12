/**
 * Authors: Tyler Baylson & Dillon Gorlesky
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
    
    /**The list of players player this Game */
    private ArrayList<Grid> players;
    
    /**The size of all boards that will be used in this game */
    private int boardSize;
    private int shipAmt;
    
    /**
     * Initialized a Game object with board sizes defined by boardSize
     * @param boardSize The size of all boards that will be used in this game
     */
    public Game(int boardSize){
        this.boardSize = boardSize;
        players = new ArrayList<>();
        int[] bounds = findBounds(boardSize);
        Random rand = new Random();
        this.shipAmt= rand.nextInt(bounds[1] - bounds[0] + 1) + bounds[0];
    }

    /**
     * Adds a new player's Grid to the ArrayList of player Grids. It is the 
     * responsibiity of the using class to keep track of player indices
     */
    public void addPlayer(){
        Grid g = new Grid(boardSize);
        setPieces(g, boardSize);
        players.add(g);
    }
    /**
     * Removes the player's Grid at a given index
     * @param index The index of a player's Grid to remove
     */
    public void removePlayerAt(int index){
        players.remove(index);
    }

    /**
     * The board of the active player. This method will return a String
     * containing all ship locations for the given player's Grid
     * @param index The player's Grid 
     * @return A Grid that will be represented in full
     */
    public String getActiveBoard(int index){
        return players.get(index).toString();
    }

    /**
     * The board of an inactive player. This method will return a String
     * containing only past hits and misses on this Grid. Ships will be hidden
     * @param index The player's Grid 
     * @return A Grid with Ships hidden
     */
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
     * @param type The type of the Ship
     * @param dir The direction from the head to draw the Ship in
     * @param row The row where the first part of this Ship will be placed
     * @param col The column where the first part of this Ship will be placed
     */
    public void setPiece(int player, ShipType type, Direction d, int row, int col){
        players.get(player).setPiece(type, d, row, col);
    }

    /**
     * Sets an appropriate number of pieces to a given Grid
     * according to the given board size
     * @param g The grid that will have its initial pieces set
     * @param boardSize The size of the board
     */
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

    /**
     * Attemps to attack the player at the given index at the given location
     * @param index The player being attacked
     * @param row The row of the attack
     * @param col The column of the attack
     */
    public void attack(int index, int row, int col){
        players.get(index).attack(row, col);
    }

    /**
     * Removes all pieces and states from the given player's Grid
     * @param index The player whose Grid will be cleared
     */
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
