/**
 * Authors: Tyler Baylson & Dillion Gorlesky
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

    public static void main(String[] args) throws UnknownHostException {
        /**if(args.length < 3){
            System.out.println("Usage: java client.BattleDriver hostname port nickname");
            System.exit(1);
        }*/
        if(args.length < 2){
            System.out.println("Usage: java client.BattleDriver hostname port size");//Change size back to nickname
            System.exit(1);
        }
        try{
            String hostname = args[0];
            int port = Integer.parseInt(args[1]);
            //String userName = args[2];
            
            BattleClient client = new BattleClient(hostname, port);
            //BattleClient client = new BattleClient(hostname, port, userName);
            client.connect();
        } catch(NumberFormatException e){
            System.out.println("Invalid Port Given. Please Retry.");
            System.out.println("Usage: java client.BattleDriver hostname port nickname");
            System.exit(1);
        } catch(UnknownHostException e){
            System.out.println("Unknown Host Given. Please Retry.");
            System.out.println("Usage: java client.BattleDriver hostname port nickname");
            System.exit(1);
        }
    }
}
