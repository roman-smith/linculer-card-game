// Author: Roman Smith
// Class/Assignment: CSE 205/Assignment 3
// Due Date: June 28, 2020
// Description: class for a deck of tarot cards to be used for the main game deck and for each player's hand

import java.util.ArrayList;
import java.lang.Exception;

public class TarotDeck {
    protected ArrayList<TarotCard> cards;
    protected int cardCount;
    protected int maxSize;

    // private method to build a standard deck of tarot cards (ordered), called in the unparam constructor
    private ArrayList<TarotCard> tarotDeck() {
        // deck to return
        ArrayList<TarotCard> returnDeck = new ArrayList<>();

        // adds number cards 1 through 21
        for (int value = 1; value < 22; value++) {
            returnDeck.add(new TarotCard(value));
        }

        // adds joker
        returnDeck.add(new TarotCard());

        return returnDeck;
    } // tarotDeck() private method

    // constructs a tarot deck of 22 cards, for building main deck
    public TarotDeck() {
        this.cards = tarotDeck();
        this.cardCount = 22;
        this.maxSize = 22;
    } // TarotDeck (unparam)

    // constructs an empty deck of a specified size, for building players' hands and discard pile
    public TarotDeck(int size) {
        this.cards = new ArrayList<>();
        this.cardCount = 0;
        this.maxSize = size;
    } // TarotDeck (param)

    // getters for card count, max size, and card at an index, setter for max size
    public int getCardCount() { return this.cardCount; }
    public int getMaxSize() { return this.maxSize; }
    public TarotCard getCardAt(int index) { return this.cards.get(index); }
    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }

    // shuffles cards in deck
    public void shuffle() {
        int ranIndex;
        TarotCard temp;

        // iterates through cards array, swapping random remaining card with card in current index
        for (int index = 0; index < this.cardCount; index++) {
            // chooses a random number of index in the range between the index to be replaced and the last card in the array
            ranIndex = (int)(Math.random()*(this.cardCount - index) + index);

            // swaps the card in the random index with the card in the current index
            temp = this.cards.get(index);
            this.cards.set(index, this.cards.get(ranIndex));
            this.cards.set(ranIndex, temp);
        }
    } //shuffle()

    // removes the last card in the deck and returns it, for drawing from the main deck
    public TarotCard draw() throws Exception {
        TarotCard lastCard;

        if (this.cardCount > 0) { // if there are cards in the array
            lastCard = this.cards.get(cardCount - 1);// copies last card in the array
            this.cards.set(cardCount - 1, null); // sets actual index to null
            cardCount--; // reduces card count by 1
            return lastCard; // returns copied TarotCard
        } else { // if the array is empty
            throw new Exception("Cannot draw from empty deck.");
        }
    } // draw(), from end

    // adds a card to the end of the deck
    public void add(TarotCard newCard) throws Exception {
        if (this.cardCount != this.maxSize) { // checks that deck is not full
            if (this.cardCount < this.cards.size()) { // checks if there are empty slots in the arraylist, meaning we have to use set for an already created index
                this.cards.set(cardCount, newCard); // adds new card to next open spot in array
            } else { // if there are no empty spots in the arraylist, add one to the end
                this.cards.add(newCard);
            }
            cardCount++;
        }
        else
            { // if the array is already full
            throw new Exception("Deck is already full, cannot add another card.");
        }
    } // Add()

    // prints the deck in a formatted grid
    public void printDeck() {
        int index = 0;
        // prints cards one by one
        while (index < this.cardCount) {
            if (index % 11 == 0) { // new line after 11 cards
                System.out.print("\n");
            }
            System.out.printf("%10s", this.cards.get(index).toString());
            index++;
        }
        System.out.print("\n"); // goes down a line
    } // PrintDeck()

    // prints the card at a specified index
    public void printCardAtIndex(int index) {
        System.out.print(this.cards.get(index).toString());
    } // printCardAtIndex
}
