
public class Grid {
    //Size of board
    private int boardSize = 10;
    private String port;

    public static void main(String[] args) {
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
        //Creating a new board of squares with symbol S to test formatting
        Square[][] board = new Square[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = new Square();
            }
        }
       // board[5][3].setToDraw("A");
        String trueBoard = drawBoard(true, board);
        String falseBoard = drawBoard(false, board);
        System.out.println(trueBoard);
        System.out.println("----------------------");
        System.out.println(falseBoard);
    }

    public static String drawBoard(boolean s, Square[][] board){
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
        if(s){
            //Formatting the board per line
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    row += topRow;
                    mid += "  " + board[i][j].getToDraw() + "  " + midRow;
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
        return table;
    }
}
