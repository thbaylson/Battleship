public class Square {

    //Symbol to draw on each square
    private Ship piece;

    //If square holds piece
    public boolean holdsPiece;

    //Add one for length of piece maybe?

    public Square(){
        this.holdsPiece = false;
        this.piece = "S";
    }

    //Clears square of ship
    public void clear(){
        this.holdsPiece = false;
        this.piece = " ";
    }

    public void setHoldsPiece(boolean b){
        this.holdsPiece = b;
    }

    public boolean getsHoldsPiece(){
        return this.holdsPiece;
    }

    public void setToDraw(String symbol){
        this.piece = symbol;
    }

    public String toString(){
        return " " + this.piece + " ";
    }
}
