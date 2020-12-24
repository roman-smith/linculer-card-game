// Author: Roman Smith
// Class/Assignment: CSE 205/Assignment 3
// Due Date: June 28, 2020
// Description: class representing a player's hand - inherits from deck class

public class TarotHand extends TarotDeck { // child class of TarotDeck
    public TarotHand() {
        super(5); // uses parameterized constructor in deck, 5 cards is the most a player can ever have in their hand
    }

    // removes the card at the specified index and returns it, all other cards move down one index, for playing cards from a hand
    public TarotCard draw(int indexToDraw) throws Exception {
        TarotCard cardToDraw;

        System.out.println(this.cardCount);
        if (this.cardCount > 0) {
            cardToDraw = this.cards.get(indexToDraw); // copies card at specified index
            this.cards.set(indexToDraw, null); // sets index to null

            // moves all the rest of the cards down one index
            for (int index = indexToDraw; index < this.cardCount - 1; index++) {
                TarotCard temp = this.cards.get(index + 1);
                this.cards.set(index, temp);
            }

            this.cards.set(cardCount - 1, null); // sets the previous last card index to null, since there is now one fewer card
            cardCount--; // reduces card count by 1 since there is one fewer card
            return cardToDraw;
        } else {
            throw new Exception("Cannot draw from empty deck.");
        }
    } // draw(), from index

    // prints the hand in a formatted line
    public void printHand() {
        int index = 0;
        // prints cards one by one
        while (index < this.cardCount) {
            // prints the cards in the hand, labeling them 1,2,3,4, or 5
            System.out.printf("%10d: %s", index + 1, this.cards.get(index).toString());
            index++;
        }
        System.out.print("\n"); // goes down a line
    } // PrintHand()
}
