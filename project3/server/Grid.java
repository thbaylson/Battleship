/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.util.ArrayList;
import java.util.Random;

/**
 * Grid is the logic for a single board of Battleship.
 */
public class Grid {
    /**The default size of a game board */
    private static final int DEFAULT_SIZE = 5;
    
    /**Size of the board */
    private int boardSize;
    
    /**Connecting port */
    private String port;
    
    /**2-Dimensional array of squares representing the game board */
    private Square[][] board;

    /**The ships that have been placed on the game board */
    private ArrayList<Ship> ships;

    /**
     * Default constructor to initialize a grid when no parameters are passed
     */
    public Grid(){
        this(DEFAULT_SIZE);
    }

    /**
     * Initializes a grid with the specified board size
     * @param boardSize The size of the board
     */
    public Grid(int boardSize) {
        //Creating a new board of squares with symbol S to test formatting
        this.ships = new ArrayList<>();
        this.boardSize = boardSize;
        this.board = new Square[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = new Square();
            }
        }
    }

    /**
     * Returns the size of the board
     * @return The size of the board
     */
    public int getBoardSize(){
        return this.boardSize;
    }

    /**
     * Attempts to place a piece on the game board. Will return true if the
     * piece was set or false if the piece could not be set
     * @param type The ShipType of the piece to be set
     * @param d The Direction of the piece to be set
     * @param row The row of the piece to be set
     * @param col The column of the piece to be set
     * @return True if the piece was set, false if the piece was not set
     */
    public boolean setPiece(ShipType type, Direction d, int row, int col){
        int[] head = {col, row};
        Ship s = new Ship(type, d, head);
        return setPiece(s);
    }

    /**
     * Attempts to place a piece on the game board. Will return true if the
     * piece was set or false if the piece could not be set
     * @param s The piece to be set on the game board
     * @return True if the piece was set, false if the piece was not set
     */
    private boolean setPiece(Ship s){
        int col = s.getHead()[0];
        int row = s.getHead()[1];

        boolean validHeadIndex = 
            verifyIndex(row, col) && this.board[row][col].isEmpty();
        
        //Before we draw on the board, we need to make sure the ship fits
        int lastRow = row + s.getDirection().getMovement()[1] * (s.getLength()-1);
        int lastCol = col + s.getDirection().getMovement()[0] * (s.getLength()-1);
        boolean shipFits = validHeadIndex && verifyIndex(lastRow, lastCol);

        //Before we draw on the board, we need to make sure ships don't collide
        boolean allSpotsValidAndEmpty = shipFits;
        for(int i = 1; i < s.getLength() && allSpotsValidAndEmpty; i++){
            col += s.getDirection().getMovement()[0];
            row += s.getDirection().getMovement()[1];
            allSpotsValidAndEmpty = this.board[row][col].isEmpty();
        }

        if(allSpotsValidAndEmpty){
            //Reset the head pointers
            col = s.getHead()[0];
            row = s.getHead()[1];

            //Draw the ship
            this.board[row][col].setToDraw(s.getParts()[0]);
            for(int i = 1; i < s.getLength(); i++){
                col += s.getDirection().getMovement()[0];
                row += s.getDirection().getMovement()[1];
                this.board[row][col].setToDraw(s.getParts()[i]);
            }
            this.ships.add(s);
        }
        return allSpotsValidAndEmpty;
    }

    /**
     * Chooses a random ShipType, Direction, and starting row and column
     * to place a new piece onto the game board
     */
    public void setRandomPiece(){
        Random rand = new Random();
        boolean pieceSet = false;
        while(!pieceSet){
            ShipType type = ShipType.values()[
                rand.nextInt(ShipType.values().length)];
            Direction dir = Direction.values()[
                rand.nextInt(Direction.values().length)];
            
            int[] head = {rand.nextInt(boardSize), rand.nextInt(boardSize)};
            Ship ship = new Ship(type, dir, head);
            //System.out.println("Attempting a: " + type.getSymbol() + " facing " + dir.getName() + " at col " + head[0] + ", row " + head[1]);
            pieceSet = setPiece(ship);
        }
    }

    /**
     * Invokes one of two methods on the Square representing the given row and
     * column. Invokes Square.miss() if the Square is empty, otherwise will
     * determine if there is a Ship occupying that Square. If there is a Ship
     * occupying that Square, this method invokes Square.hit() of that Square
     * @param row The row to attempt an attack on 
     * @param col The column to attempt an attack on
     */
    public void attack(int row, int col){
        if(this.board[row][col].isEmpty()){
            this.board[row][col].miss();
        }else{
            for(Ship s : ships){
                int startRow = s.getHead()[1];
                int lastRow = startRow + 
                    s.getDirection().getMovement()[1] * (s.getLength()-1);
                
                int startCol = s.getHead()[0];
                int lastCol = startCol + 
                    s.getDirection().getMovement()[0] * (s.getLength()-1);
                
                int temp;
                if(startCol > lastCol){
                    temp = startCol;
                    startCol = lastCol;
                    lastCol = temp;
                }
                if(startRow > lastRow){
                    temp = startRow;
                    startRow = lastRow;
                    lastRow = temp;
                }
                boolean validRow = startRow <= row && row <= lastRow;
                boolean validCol = startCol <= col && col <= lastCol;
                if(validRow && validCol && !s.isSunken()){
                    //System.out.println("\t\tInside validation check");
                    int index = Math.abs(startRow - row) + Math.abs(startCol - col);
                    s.hit(index);
                    this.board[row][col].setToDraw(s.getParts()[index]);
                    if(s.isSunken()){
                        System.out.println(s.getType().getType() + " Was Sunk!");
                        checkEndCondition();
                    }
                }
            }
        }
    }

    /**
     * Clears all the Square objects from the game board
     */
    public void clearBoard(){
        for(Square[] row : this.board){
            for(Square s : row){
                s.clear();;
            }
        }
    }

    /**
     * Determines if all Ships belonging to this game board have sunken
     */
    private void checkEndCondition(){
        int sunkenShips = 0;
        for(Ship s : ships){
            if(s.isSunken()){
                sunkenShips++;
            }
        }
        if(sunkenShips == ships.size()){
            System.out.println("Player Loses!");
        }
    }

    /**
     * Returns true if the given row and column exist within the bounds of the 
     * game board, otherwise will return false
     * @param row The row to be checked if valid
     * @param col The column to be checked if valid
     * @return True if the row/column pair represent valid indices on the baord
     */
    private boolean verifyIndex(int row, int col){
        boolean validRow = 0 <= row && row < boardSize;
        boolean validCol = 0 <= col && col < boardSize;
        return validRow && validCol;
    }

    /**
     * The String representation of the board
     * @return The String representation of the board
     */
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