/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

/**
 * BattleServer is one of the classes that implement the server-side logic of 
 * this client-server application. It is responsible for accepting incoming 
 * connections, creating ConnectionAgents, and passing the ConnectionAgent off 
 * to threads for processing. Implements the MessageListener interface (i.e., 
 * it can "observe" objects that are MessageSources).
 */
public class BattleServer {

    public static void main(String[] args){
        int boardSize = 10;
        if(args.length == 1){
        } else {
            try {
                //System.out.println(boardSize);
                boardSize = Integer.parseInt(args[1]);
                if(boardSize < 2){
                    boardSize = 10;
                }
            }catch(NumberFormatException e){
                boardSize = 10;
            }
        }

        Game game = new Game();
    }
}
