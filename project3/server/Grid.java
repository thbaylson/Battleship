/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

/**
 * Grid is the logic for a single board of Battleship.
 */
public class Grid {
    
    //Size of board
    private int boardSize = 10;
    private String port;
    private Square[][] board;

    public Grid(int boardSize) {
        //Creating a new board of squares with symbol S to test formatting
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

    public String toString(){
        String table = "";
        String topRow = "+-----";
        String midRow = "|";
        String row = "\t";
        String mid = "\t|";
        String hold = "     ";
        StringBuilder val = new StringBuilder("\t   0");
        //Printing the column numbers at the top
        for(int i = 1; i < board.length; i++){
            val.append(hold).append(i);
        }
        String temp = "";
        table = val.toString();
        /**
        if(s){
            //Formatting the board per line
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    row += topRow;
                    mid += board + midRow;
                    hold = row;
                }
                row = row + "+";
                table += "\n" + row;
                table += "\n" + i;
                table += mid;
                row = "\t";
                mid = "\t|";
            }
            //Printing bottom line of board
            table += "\n" + hold + "+";

        } else {
            //Formatting the board per line
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    row += topRow;
                    mid += "     " + midRow;
                    hold = row;
                }
                row = row + "+";
                table += "\n" + row;
                table += "\n" + i;
                table += mid;
                row = "\t";
                mid = "\t|";
            }
            //Printing bottom line of board
            table += "\n" + hold + "+";
        }
        */
        return table;
    }
}
