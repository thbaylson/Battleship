/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

/**
 * Game contains the logic for the game of BattleShip. It has a Grid for each client.
 */
public class Game {
    private ArrayList<Grid> clients;
    
    public Game(int boardSize){
        this.board = new Grid(boardSize);
    }

    public String getBoards(){
        for(Grid g : clients){
            System.out.println(g);
        }
    }
}
