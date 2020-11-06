/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.util.Random;

/**
 * Grid is the logic for a single board of Battleship.
 */
public class Grid {
    //Final Fields
    private static final int DEFAULT_SIZE = 5;
    
    //Size of the board
    private int boardSize;
    
    //Connecting port
    private String port;
    
    //2-Dimensional array of squares representing the game board
    private Square[][] board;

    public Grid(){
        this(DEFAULT_SIZE);
    }

    public Grid(int boardSize) {
        //Creating a new board of squares with symbol S to test formatting
        this.boardSize = boardSize;
        this.board = new Square[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = new Square();
            }
        }
    }

    public void setPiece(Ships s, int i, int j){
        this.board[i][j].setToDraw(s.toString());
    }

    public void setRandomPiece(){
        Random rand = new Random();
        Ships ship = Ships.values()[rand.nextInt(Ships.values().length)];
        int orientation = rand.nextInt(3);
        int sLength = ship.getLength();
        
        int widthRange;
        int heightRange;

        switch(orientation){
            case 0:
                // Place the top first, then subsequent pieces down
                widthRange = rand.nextInt(boardSize);
                heightRange = rand.nextInt(boardSize - sLength);
                break;
            case 1:
                //Place the bottom first, then subsequent pieces up
                widthRange = rand.nextInt(boardSize);
                heightRange = rand.nextInt(sLength - boardSize) + boardSize;
                break;
            case 2:
                // Place the leftmost first, then subsequent pieces to the right
                widthRange = rand.nextInt(boardSize);
                heightRange = rand.nextInt(boardSize);
                break;
            case 3:
                // Place the rightmost first, then subsequent pieces to the left
                widthRange = rand.nextInt(boardSize);
                heightRange = rand.nextInt(boardSize);
                break;
        }
    }

    public String toString(){
        //Build the spacer string
        StringBuilder spacer = new StringBuilder("\n   ");
        for(int i = 0; i < boardSize; i++){
            spacer.append("+---");
        }
        spacer.append("+\n");
        
        //Header for column indices
        StringBuilder table = new StringBuilder("   ");
        for(int k = 0; k < boardSize; k++){
            table.append(String.format("%3s ", k));
            if(k == boardSize){
                table.append(spacer);
            }
        }
        
        //All rows
        table.append(spacer);
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize + 1; j++){
                // Row indices
                if(j == 0){
                    table.append(String.format(" %s |", i));
                }else{
                    table.append(String.format(" %s |", board[i][j-1]));
                }
            }
            table.append(spacer);
        }

        return table.toString();
    }
}
