/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

/**
 * Represents the logic for a ship piece in the game BattleShip
 */
public class Ship{

    /**The type of the Ship */
    private ShipType type;
    
    /**The direction from the head to draw the Ship in */
    private Direction direction;

    /**The board row&column where the first part of this Ship will be placed */
    private int[] head = new int[2];

    /**The parts of this Ship that will exist across multiple Squares */
    private char[] parts;

    /**
     * Initializes a Ship with the given type, direction, and starting point
     * @param type The type of the Ship
     * @param dir The direction from the head to draw the Ship in
     * @param head The board index where the first part of this will be placed
     */
    Ship(ShipType type, Direction dir, int[] head){
        this.type = type;
        this.direction = dir;
        this.head = head;
        this.parts = new char[type.getSize()];
        for(int i = 0; i < parts.length; i++){
            this.parts[i] = type.getSymbol();
        }
    }

    /**
     * Returns the parts of this Ship
     * @return The parts of this Ship
     */
    public char[] getParts(){
        return parts;
    }

    /**
     * Returns the length of this Ship
     * @return The length of this Ship
     */
    public int getLength(){
        return parts.length;
    }

    /**
     * Returns the head of this Ship
     * @return The head of this Ship
     */
    public int[] getHead(){
        return head;
    }

    /**
     * Returns the ShipType of this Ship
     * @return The ShipType of this Ship
     */
    public ShipType getType(){
        return this.type;
    }

    /**
     * Returns the Direction of this Ship
     * @return The Direction of this Ship
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Sets the ShipType of this Ship
     * @param type The ShipType of this Ship
     */
    public void setType(ShipType type) {
        this.type = type;
    }

    /**
     * Sets the head of this Ship
     * @param i The row index to place the head
     * @param j The column index to place the head
     */
    public void setHead(int i, int j) {
        this.head[0] = i;
        this.head[1] = j;
    }

    /**
     * Sets the head of this Ship
     * @param head An int[] of size 2 which represents a game board index
     */
    public void setHead(int[] head) {
        this.head = head;
    }

    /**
     * Sets the Direction of this Ship
     * @param direction The Direction of this Ship
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Changes this Ship's parts to an '@' at the given index
     * @param index The part of the Ship that is being hit
     */
    public void hit(int index){
        if(0 <= index && index < this.parts.length){
            this.parts[index] = '@';
        }
    }

    /**
     * Returns true if each part of this Ship is anything other than
     * the symbol defined in the ShipType Enum, thus representing a hit
     * @return True if each part of the Ship has been hit, otherwise false
     */
    public boolean isSunken(){
        boolean sunken = true;
        for(int i = 0; i < this.parts.length && sunken; i++){
            sunken = this.parts[i] != type.getSymbol();
        }
        return sunken;
    }

    /**
     * The String representation of a BattleShip Ship
     */
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < this.parts.length; i++){
            str.append(this.parts[i]);
        }
        return str.toString();
    }
}