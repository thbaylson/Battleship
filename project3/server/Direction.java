package server;

public enum Direction{
    UP("UP", 0, 1),
    DOWN("DOWN", 0, -1),
    LEFT("LEFT", -1, 0),
    RIGHT("RIGHT", 1, 0);

    private String name;
    private int horizontalMovement;
    private int verticalMovement;
    
    private Direction(String name,int horizontalMovement,int verticalMovement){
        this.name = name;
        this.horizontalMovement = horizontalMovement;
        this.verticalMovement = verticalMovement;
        
    }

    public String getName(){
        return this.name;
    }

    public int[] getMovement(){
        int[] movement = {this.horizontalMovement, this.verticalMovement};
        return movement;
    }
}