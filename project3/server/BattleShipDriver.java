/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package server;

/**
 * BattleShipDriver contains the main() method for the server. It parses command line options, instantiates a
 * BattleServer, and calls its listen() method. This takes two command line arguments, the port number for
 * the server and the size of the board (if the size is left off, default to size 10 x 10). You may assume square
 * arrays
 */
public class BattleShipDriver {
    
    public static void main(String[] args){
        int boardSize = 10;
        if(args.length == 1){
        } else {
            try {
                int port = Integer.parseInt(args[0]);
                boardSize = Integer.parseInt(args[1]);
                if(boardSize < 5){
                    System.out.println("Usage: java server.BattleShipDriver port boardSize(5-10)");
                    System.exit(1);
                } else if(boardSize > 10){
                    System.out.println("Usage: java server.BattleShipDriver port boardSize(5-10)");
                    System.exit(1);
                }
                BattleServer battleServer = new BattleServer(port, boardSize);
                battleServer.listen();
            }catch(NumberFormatException e){
                System.out.println("Invalid Board Size Given. Please Retry.");
                System.out.println("Usage: java server.BattleShipDriver port boardSize(5-10)");
                System.exit(1);
            }
        } 
    }
}
