/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

/**
 * Represents the discrete number of ships in the game BattleShip
 */
public enum ShipType{
    Battleship('B', "Battleship", 4),
    Carrier('C', "Carrier", 5),
    Cruiser('R', "Cruiser", 3),
    Destroyer('D', "Destroyer", 2),
    Submarine('S', "Submarine", 3);

    /**The char representation of a ship */
    private char symbol;
    
    /**The full name of the ship */
    private String type;
    
    /**The size of the ship, ie: how many consecutive squares it fills */
    private int size;

    /**
     * Initializes a ShipType object
     * @param symbol The char representation of a ship
     * @param type The full name of the ship 
     * @param size The size of the ship
     */
    ShipType(char symbol, String type, int size){
        this.symbol = symbol;
        this.type = type;
        this.size = size;
    }

    /**
     * Returns the symbol representation of the ship
     * @return The symbol representation of the ship
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the full name of the ship
     * @return The full name of the ship
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the size of the ship
     * @return The size of the ship
     */
    public int getSize(){
        return size;
    }
}
