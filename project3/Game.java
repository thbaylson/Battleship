/**
 * Has a grid for each client
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
