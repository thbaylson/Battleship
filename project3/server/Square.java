/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

public class Square {

    //Symbol to draw on each square
    private String piece;

    //If square holds piece
    private boolean isEmpty;

    //Add one for length of piece maybe?

    public Square(){
        clear();
    }

    //Clears the square
    public void clear(){
        this.piece = " ";
        this.isEmpty = true;
    }

    public boolean isEmpty(){
        return this.isEmpty;
    }

    public void miss(){
        this.piece = "X";
    }

    public void setToDraw(String symbol){
        this.piece = symbol;
        this.isEmpty = false;
    }

    public void setToDraw(char symbol){
        setToDraw(symbol + "");
    }

    public String toString(){
        return this.piece;
    }
}
