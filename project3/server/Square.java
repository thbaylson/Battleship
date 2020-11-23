/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

/**
 * Represents a single square in a Battleship game board
 */
public class Square {

    //Symbol to draw on each square
    private String piece;

    //If square holds piece
    private boolean isEmpty;

    /**
     * Initializes the square object and clears itself
     */
    public Square(){
        clear();
    }

    /**
     * Sets the string representation to be a single space character
     */
    public void clear(){
        this.piece = " ";
        this.isEmpty = true;
    }

    /**
     * Returns true if this Square is empty, otherwise returns false
     * @return True if this Square is empty, otherwise returns false
     */
    public boolean isEmpty(){
        return this.isEmpty;
    }

    /**
     * Set the string representation to be a single 'X' character
     */
    public void miss(){
        this.piece = "X";
    }

    /**
     * Set the string representation to be the given string
     * @param symbol The symbol that represents the state of this Square
     */
    public void setToDraw(String symbol){
        this.piece = symbol;
        this.isEmpty = false;
    }

    /**
     * Set the string representation to be the given string
     * @param symbol The symbol that represents the state of this Square
     */
    public void setToDraw(char symbol){
        setToDraw(symbol + "");
    }

    /**
     * The string representation of a Square
     */
    public String toString(){
        return this.piece;
    }
}
