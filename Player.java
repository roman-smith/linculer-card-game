// Author: Roman Smith
// Class/Assignment: CSE 205/Assignment 3
// Due Date: June 28, 2020
// Description: class representing a player in the game

import java.util.Scanner;

public class Player {
    private TarotHand hand;
    private TarotCard cardPlayed;
    private TarotCard cardOnHead;
    private int tricksPredicted;
    private int tricksWon;
    private int score;
    private boolean isDealer;

    // constructor for player
    public Player() {
        this.hand = new TarotHand(); // empty hand of size 5
        this.cardPlayed = null; // player hasn't played any cards yet
        this.cardOnHead = null; // no card on head yet
        this.tricksPredicted = 0; // player hasn't predicted anything yet
        this.tricksWon = 0; // player hasn't won any tricks yet
        this.score = 7; // every player starts at 7 and their score is subtracted from when they lose
        isDealer = false;
    } // Player() constructor

    // getters and setters
    public TarotHand getHand() { return hand; }
    public void setHand(TarotHand hand) { this.hand = hand; }
    public TarotCard getCardPlayed() { return cardPlayed; }
    public void setCardPlayed(TarotCard cardPlayed) { this.cardPlayed = cardPlayed; }
    public TarotCard getCardOnHead() { return cardOnHead; }
    public void setCardOnHead(TarotCard cardOnHead) { this.cardOnHead = cardOnHead; }
    public int getTricksPredicted() { return tricksPredicted; }
    public void setTricksPredicted(int tricksPredicted) { this.tricksPredicted = tricksPredicted; }
    public int getTricksWon() { return tricksWon; }
    public void setTricksWon(int tricksWon) { this.tricksWon = tricksWon; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public boolean getIsDealer() { return this.isDealer; }
    public void setIsDealer(boolean isDealer) { this.isDealer = isDealer; }

    // takes a card and adds it to the hand
    public void addToHand(TarotCard cardToAdd) {
        try {
            this.hand.add(cardToAdd);
        } catch (Exception e) {
            System.out.println("Cannot add a card to a full hand.");
        }
    } // addToHand()

    // prints player's hand, asks them to enter their prediction for the number of tricks they will win, returns that prediction
    public int makePrediction(int playerNumber) {
        // lists the player's current hand
        System.out.printf("Player %d:%n", playerNumber);
        this.hand.printHand();

        // asks the player to enter their prediction of how many tricks they will win
        System.out.println("Enter the number of tricks you think you will win:");
        return predictSelect();
    } // makePrediction()

    // gets a valid integer for trick prediction, can be called internally or externally
    public int predictSelect() {
        boolean hasPredict = false;
        int selection;

        do {
            selection = goodInt();
            if (selection >= 0 && selection <= this.hand.cardCount) { // if the entered integer is a valid prediction (between 0 and the number of cards in the hand which = the round number)
                hasPredict = true;
            } else {
                System.out.println("You must make a prediction between 0 and the number of cards dealt this round.");
            }
        } while (!hasPredict);
        this.tricksPredicted = selection;
        return this.tricksPredicted;
    } // predictSelect()

    // prints player's hand, asks them to enter the index of the card they want to play, and returns the corresponding card
    public TarotCard playCard(int playerNumber) {
        // lists the player's current hand
        System.out.printf("Player %d:%n", playerNumber);
        this.hand.printHand();

        // asks the player to enter their card choice
        System.out.println("Enter the INDEX of the card you would like to play:");
        return playSelect();
    } // playCard()

    // gets a valid selection of card to play
    private TarotCard playSelect() {
        boolean hasSelect = false;
        int indexSelection;

        do {
            indexSelection = goodInt();
            if (indexSelection > 0 && indexSelection <= this.hand.getCardCount()) { // if the index selection is an actual index in the hand
                hasSelect = true;
            } else {
                System.out.println("That index doesn't exist. Try again.");
            }
        } while (!hasSelect);

        try {
            this.cardPlayed = this.hand.draw(indexSelection - 1); // gets the card at the index that the user selected
        } catch (Exception e) {
            System.out.println("Caught exception." + e.getMessage());
            return null; // if something doesn't work, it's probably bc of this
        }
        if (this.cardPlayed.getName().equals("Joker")) { // if it's a joker, it's value needs to be determined by the user
            setJokerVal();
        }
        return this.cardPlayed; // returns that card

    } // playSelect()

    // copies the first card in the array to the cardOnHead variable
    public void setCardOnHead() {
        this.cardOnHead = this.hand.cards.get(0);
    }

    // moves the head round card from the hand to the cardPlayed
    public TarotCard playHeadCard() {
        try {
            this.cardPlayed = this.hand.draw(0);
            return this.cardPlayed;
        } catch (Exception e) {
            System.out.println("Cannot draw from an empty deck");
            return null;
        }
    }

    // let's the player set their joker value when they play it
    private void setJokerVal() {
        int valueSelect;
        boolean hasGoodVal = false;

        System.out.println("Do you want your joker to be worth '0' or '22'?");

        do { // loops until a valid value is entered
            valueSelect = goodInt();
            if (valueSelect == 0 || valueSelect == 22) { // if the entered value is a value that a joker can be
                hasGoodVal = true;
            } else {
                System.out.println("That isn't a valid value for a joker. Try again.");
            }
        } while (!hasGoodVal);

        cardPlayed.setValue(valueSelect); // sets the joker equal to either 0 or 22, depending on what the user entered
    } // setJokerVal()

    // scans in user input, if it is an integer, it returns that integer, if not, it loops until it gets one
    private int goodInt() {
        boolean hasInt = false;
        Scanner in = new Scanner(System.in);

        // loop until input has an int
        do {
            if (in.hasNextInt()) {
                hasInt = true;
            } else {
                System.out.println("That isn't an integer. Try again.");
            }
        } while (!hasInt);

        return in.nextInt(); // return the int
    } // goodInt()

}
