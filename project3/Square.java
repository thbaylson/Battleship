
public class Square {

    //Symbol to draw on each square
    private String toDraw;

    //If square holds piece
    public boolean holdsPiece;

    //Add one for length of piece maybe?

    public Square(){
        this.holdsPiece = false;
        this.toDraw = "S";
    }

    //Clears square of ship
    public void clear(){
        this.holdsPiece = false;
        this.toDraw = " ";
    }

    public void setHoldsPiece(boolean b){
        this.holdsPiece = b;
    }

    public boolean getsHoldsPiece(){
        return this.holdsPiece;
    }

    public void setToDraw(String symbol){
        this.toDraw = symbol;
    }

    public String getToDraw(){
        return this.toDraw;
    }
}
