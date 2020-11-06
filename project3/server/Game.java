/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

import java.util.ArrayList;

/**
 * Game contains the logic for the game of BattleShip. It has a Grid for each
 * client.
 */
public class Game {
    private ArrayList<Grid> clients;
    private Grid board;
    
    public Game(int boardSize){
        this.board = new Grid(boardSize);
    }

    public void printBoards(){
        for(Grid g : clients){
            System.out.println(g);
        }
    }
}
