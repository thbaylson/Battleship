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
