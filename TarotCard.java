// Author: Roman Smith
// Class/Assignment: CSE 205/Assignment 3
// Due Date: June 28, 2020
// Description: class representing a tarot card, these cards are essentially just numbers from 1-21 and a joker that can either be 0 or 22

public class TarotCard {
    private int value;
    private String name;
    private boolean faceUp;

    // no unparameterized constructor because there can't be a card without a value or name
    // constructor for all number cards
    public TarotCard (int value) {
        this.value = value;
        this.name = Integer.toString(value);
    } // TarotCard() value param

    // constructor for the joker card
    public TarotCard() {
        this.value = 0; // initialized at 0 but the user may determine it's value in game, either 0 or 22
        this.name = "Joker";
    } // TarotCard() unparam

    // getters for value and name
    public int getValue() { return this.value; }
    public String getName() { return this.name; }

    // setter for joker value
    public void setValue(int newValue) {
        if (this.name.equals("Joker") && (newValue == 0 || newValue == 22)) { // only conditions when a card should ever be changed
            this.value = newValue;
        }
    } // setValue()

    //getter and setter for faceUp boolean
    public boolean getFaceUp() { return this.faceUp; }
    public void setFaceUp(boolean faceUp) { this.faceUp = faceUp; }

    // toString() override
    @Override
    public String toString() {
        if (this.name == null) {
            return String.format("[%d]", this.value);
        } else {
            return String.format("[%s]", this.name);
        }
    } // toString()
} // TarotCard class
