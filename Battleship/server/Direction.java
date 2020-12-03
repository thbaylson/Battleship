/**
 * Authors: Tyler Baylson & Dillon Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

/**
 * Represents game logic for the directions a ship can face
 */
public enum Direction{
    UP("UP", 0, -1),
    DOWN("DOWN", 0, 1),
    LEFT("LEFT", -1, 0),
    RIGHT("RIGHT", 1, 0);

    /**The name of the direction */
    private String name;

    /**The horizontal movement of this direction */
    private int horizontalMovement;

    /**The vertical movement of this direction */
    private int verticalMovement;
    
    /**
     * Initializes a Direction
     * @param name The name of the direction
     * @param horizontalMovement The horizontal movement of this direction
     * @param verticalMovement The vertical movement of this direction
     */
    private Direction(String name,int horizontalMovement,int verticalMovement){
        this.name = name;
        this.horizontalMovement = horizontalMovement;
        this.verticalMovement = verticalMovement;
        
    }

    /**
     * Returns the name of this direction
     * @return The name of this direction
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns an int[] of size 2. The first index represents how this
     * direction changes the horizontal position of a ship and the second
     * position representes changes in the vertical position.
     * @return An int[] of size 2 with movement information
     */
    public int[] getMovement(){
        int[] movement = {this.horizontalMovement, this.verticalMovement};
        return movement;
    }
}