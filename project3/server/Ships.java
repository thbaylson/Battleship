<<<<<<< HEAD:project3/Ships.java
=======
/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

>>>>>>> 1c9c6cc0a30786e5fc6890d2280eb42d7a36a7f2:project3/server/Ships.java
public enum Ships{

    Battleship('B', "Battleship", 4),
    Carrier('C', "Carrier", 5),
    Cruiser('R', "Cruiser", 3),
    Destroyer('D', "Destroyer", 2),
    Hit('@', "Hit", 1),
    Miss('X', "Miss", 1),
    Submarine('S', "Submarine", 3);

    private char symbol;
    private String type;
    private int length;

    Ships(char symbol, String type, int length){
        this.symbol = symbol;
        this.type = type;
        this.length = length;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLetter(char symbol) {
        this.symbol = symbol;
    }

    public String toString(){
        return this.symbol + "";
    }
}
