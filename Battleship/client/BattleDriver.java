/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package client;

import java.net.UnknownHostException;

/**
 * BattleDriver contains the main() method for the client. It parses command
 * line options, instantiates a BattleClient, reads messages from the keyboards,
 * and sends them to the client. The command line arguments are: hostname,
 * portnumber and user nickname. All of these command line arguments are
 * required.
 */
public class BattleDriver {
    private final static int DEFAULT_PORT = 5500;

    /**
     * The purpose of this function is to act as the main for the entire 
     * program to get command line arguments, handle improper input, then
     * make a new BattleClient.
     * @param String[]: Command line arguments including the port, hostname, 
     *      and username.
     * @throws UnknownHostException: If host can't be located
     * @throws NumberFormatException: If user input isn't integers for the port.
     */
    public static void main(String[] args) throws UnknownHostException, 
        NumberFormatException {
        String userName = "";
        int port = DEFAULT_PORT;
        String hostname = "";
        if(args.length < 2){
            System.out.println("Usage: java client.BattleDriver <hostname> " +
            "<port> <nickname>");
            System.exit(1);
        }
        try{ 
            if(args.length == 2){// Incorrent assumption that the port was left off
                hostname = args[0];
                userName = args[1];
            } else {//only port given
                hostname = args[0];
                port = Integer.parseInt(args[1]);
                userName = args[2];
            }
            BattleClient client = new BattleClient(hostname, port, userName);
            client.connect();
        } catch(NumberFormatException e){
            System.out.println("Invalid Port Given. Please Retry.");
            System.out.println("Usage: java client.BattleDriver hostname port "+
                "nickname");
            System.exit(1);
        } catch(UnknownHostException e){
            System.out.println("Unknown Host Given. Please Retry.");
            System.out.println("Usage: java client.BattleDriver hostname port "+
                "nickname");
            System.exit(1);
        }
    }
}
