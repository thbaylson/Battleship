/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */
package server;

public class Ship{

    private ShipType type;
    private Direction direction;
    private int[] head = new int[2];
    private char[] parts;

    Ship(ShipType type, Direction dir, int[] head){
        this.type = type;
        this.direction = dir;
        this.head = head;
        this.parts = new char[type.getSize()];
        for(int i = 0; i < parts.length; i++){
            this.parts[i] = type.getSymbol();
        }
    }

    public char[] getParts(){
        return parts;
    }

    public int getLength(){
        return parts.length;
    }

    public int[] getHead(){
        return head;
    }

    public Direction getDirection(){
        return direction;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public void setHead(int i, int j) {
        this.head[0] = i;
        this.head[1] = j;
    }

    public void setHead(int[] head) {
        this.head = head;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void hit(int index){
        if(0 <= index && index < this.parts.length){
            this.parts[index] = '@';
        }
    }

    public boolean isSunken(){
        boolean sunken = true;
        for(int i = 0; i < this.parts.length && sunken; i++){
            sunken = this.parts[i] != type.getSymbol();
        }
        return sunken;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < this.parts.length; i++){
            str.append(this.parts[i]);
        }
        return str.toString();
    }
}