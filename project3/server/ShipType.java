package server;

public enum ShipType{
    Battleship('B', "Battleship", 4),
    Carrier('C', "Carrier", 5),
    Cruiser('R', "Cruiser", 3),
    Destroyer('D', "Destroyer", 2),
    Submarine('S', "Submarine", 3);

    private char symbol;
    private String type;
    private int size;

    ShipType(char symbol, String type, int size){
        this.symbol = symbol;
        this.type = type;
        this.size = size;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getAllSymbols(){
        return "BCRDS";
    }

    public String getType() {
        return type;
    }

    public int getSize(){
        return size;
    }
}
