package com.company;

public enum Ships{

    Battleship('B', "Battleship", 4),
    Cruiser('R', "Cruiser", 3),
    Carrier('C', "Carrier", 5),
    Submarine('S', "Submarine", 3),
    Destroyer('D', "Destroyer", 2),
    Empty(' ', "Empty", 1),
    Hit('X', "Hit", 1),
    Miss('M', "Miss", 1);

    private char letter;
    private String type;
    private int length;

    Ships(char letter, String type, int length){
        this.letter = letter;
        this.type = type;
        this.length = length;
    }

    public char getLetter() {
        return letter;
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

    public void setLetter(char letter) {
        this.letter = letter;
    }
}
