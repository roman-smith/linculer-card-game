// Author: Roman Smith
// Class/Assignment: CSE 205/Assignment 3
// Due Date: June 28, 2020
// Description: class using the deck and player classes to run the game -- is called in the main method class

import java.util.ArrayList;

public class Game {
    // there are exactly four players and four decks in this game
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    // there are 3 decks that are used to play
    private TarotDeck mainDeck;
    private TarotDeck inPlay;
    private TarotDeck discardPile;

    // Deck to hold the cards on players heads during the head round, this is different than playing a card
    private TarotDeck cardsOnHead;

    // ArrayList to keep track of the players' predictions (a deck isn't appropriate since the predictions aren't cards)
    private ArrayList<Integer> trickPredictions;

    // important numbers to keep track of
    int dealerNum; // keeps track of who the dealer is
    int roundNum; // keeps track of which round it is
    int trickNum; // keeps track of which trick it is in the round
    int orderPlace; // keeps track of the current place in the order of play


    public Game() {
        this.player1 = new Player();
        this.player2 = new Player();
        this.player3 = new Player();
        this.player4 = new Player();

        this.mainDeck = new TarotDeck();  // calls unparameterized constructor to fill the tarot deck
        this.inPlay = new TarotDeck(4); // max number of cards in play at once is 4
        this.discardPile = new TarotDeck(22); // calls parameterized constructor to make an empty deck of size 22

        this.cardsOnHead = new TarotDeck(4); // makes a new TarotDeck of size 4

        this.trickPredictions = new ArrayList<>(); // makes a new arraylist

        this.dealerNum = 1; // player 1 starts as the dealer
        this.roundNum = 5; // first round of the game will be 5, rounds count down to 1
        this.trickNum = 1; // starts on the first trick
        this.orderPlace = 2; // play each round starts to left of the dealer
    }

    /*
    // getters and setters
    public TarotDeck getMainDeck() {return mainDeck; }
    public TarotDeck getInPlay() {return inPlay; }
    public TarotDeck getDiscardPile() { return discardPile; }
    public int getDealerNum() { return dealerNum; }
    public int getRoundNum() { return roundNum; }
    public int getTrickNum() { return trickNum; }
    public int getOrderPlace() { return orderPlace; }
     */

    // core method for playing the game, is called in public static void main in Assn3 class
    public void play() {
        dealerNum = 1; // to keep track of who the dealer is -- if dealer is '1', then player1 is dealer

        System.out.println("Welcome to L'Enculer! This is a little-known French card game where you predict how many tricks you will win in each round.");
        System.out.println("If you predict incorrectly, you lose points. When someone is out of points, the game ends and whoever has the most remaining points wins!");

        while (!playerLost()) { // while no one has lost the game yet, play sets and change the dealer each time
            set();
            dealerNum++; // increase the dealer num to the next 'person'
            dealerNum = dealerNum % 4; // if dealer num gets to 4, set to 0 (if dealer count is '0', then player4 is dealer)
        }

        // after someone has lost (score hit 0), print final scores
        System.out.println("The game is over! Here are the final scores:");
        System.out.println("Player 1: " + player1.getScore());
        System.out.println("Player 2: " + player2.getScore());
        System.out.println("Player 3: " + player3.getScore());
        System.out.println("Player 4: " + player4.getScore());
        System.out.println("");

        // determine who won
        if (player1.getScore() > player2.getScore() && player1.getScore() > player3.getScore() && player1.getScore() > player4.getScore()) {
            System.out.println("Player 1 wins!");
        } else if (player2.getScore() > player1.getScore() && player2.getScore() > player3.getScore() && player2.getScore() > player4.getScore()) {
            System.out.println("Player 2 wins!");
        } else if (player3.getScore() > player1.getScore() && player3.getScore() > player2.getScore() && player3.getScore() > player4.getScore()) {
            System.out.println("Player 3 wins!");
        } else if (player4.getScore() > player1.getScore() && player4.getScore() > player2.getScore() && player4.getScore() > player3.getScore()) {
            System.out.println("Player 4 wins!");
        } else {
            System.out.println("It's a tie!");
        }
    } // play()

    // plays through a set - one of each round, all with the same dealer
    private void set() {
        roundNum = 5; // each set starts at round five and plays through 'zero' aka 'head' round

        if (!playerLost()) { // if no one has lost yet, we can play another set

            // play the 5,4,3,2, and 1 rounds
            while (roundNum > 0) {
                round();
                roundNum--; // take the round number down
            }

            // play the 'head' round,
            headRound();
        } // if someone has lost, we don't do anything
    } // set()

    // plays one normal round (as opposed to a special 'head' round) with the indicated round number
    private void round() {
        trickNum = 1; // all rounds start on the first trick
        orderPlace = dealerNum + 1; // keeps track of where the play starts, orderPlace always starts one higher than dealer

        // reset trick predictions for each player
        trickPredictionsReset();

        // reset the tricks won for each player to 0
        tricksWonReset();

        if (!playerLost()) { // if no one has lost yet, we can play another round
            System.out.printf("Round %d%n", roundNum);

            // shuffle
            mainDeck.shuffle();

            // deal
            deal();

            // players make predictions on the number of tricks they will win
            for (int i = 0; i < 3; i++) { // all but one player predict, since the last predictor has special constraints

                if (i == 0) {
                    System.out.println("You are the first to predict.");
                } else {
                    System.out.println("Players' predictions so far:");
                    System.out.println(trickPredictions.toString()); // prints the predictions of everyone else
                }

                if (orderPlace % 4 == 1) { // if it's player1's turn
                    trickPredictions.add(player1.makePrediction(1)); // adds player1's prediction to the array
                } else if (orderPlace % 4 == 2) { // if it's player2's turn
                    trickPredictions.add(player2.makePrediction(2)); // adds player2's prediction to the array
                } else if (orderPlace % 4 == 3) { // if it's player3's turn
                    trickPredictions.add(player3.makePrediction(3)); // adds player3's prediction to the array
                } else { // if it's player4's turn
                    trickPredictions.add(player4.makePrediction(4)); // adds player4's prediction to the array
                }
                orderPlace++; // increases orderPlace
            }

            // get the prediction of the last player
            System.out.println("Players' predictions so far:");
            System.out.println(trickPredictions.toString()); // prints the predictions of everyone else
            lastPrediction(orderPlace); // performs the last prediction
            orderPlace++; // increases order place

            // play tricks 'roundNum' of times
            while (trickNum <= roundNum) {
                trick(); // plays one trick
                trickNum++; // increases trick num
            }

            //adjust scores
            adjustScores();

            // after all tricks are over, put the discarded cards back in the main deck (they are shuffled at the beginning of the method, not the end)
            discardToMain();

        } // if someone has lost, we do not play another round
    }

    // plays the 'head' round - in the physical card game, players do not look at their card but put it on their forehead so they can see everyone else's and everyone can see theirs
    private void headRound() {
        trickNum = 1;
        orderPlace = dealerNum + 1; // keeps track of where the play starts, orderPlace always starts one higher than dealer

        // reset trick predictions for each player
        trickPredictionsReset();

        // reset tricks won for each player to 0
        tricksWonReset();

        // reset cards on head array
        cardsOnHeadReset();

        if (!playerLost()) { // if no one has lost yet, we can play another round
            System.out.println("Head Round");

            // shuffle
            mainDeck.shuffle();

            // deal
            deal(); // one card is always dealt in the head round

            // fill the card on head array so other players' cards can be displayed
            player1.setCardOnHead();
            try {
                cardsOnHead.add(player1.getCardOnHead()); // copies, does not draw, card to cardsOnHead deck
            } catch (Exception e) {
                System.out.println("Exception adding card to player 1 head in headRound().");
            }

            player2.setCardOnHead();
            try {
                cardsOnHead.add(player2.getCardOnHead()); // copies, does not draw, card to cardsOnHead deck
            } catch (Exception e) {
                System.out.println("Exception adding card to player 2 head in headRound().");
            }

            player3.setCardOnHead();
            try {
                cardsOnHead.add(player3.getCardOnHead()); // copies, does not draw, card to cardsOnHead deck
            } catch (Exception e) {
                System.out.println("Exception adding card to player 3 head in headRound().");
            }

            player4.setCardOnHead();
            try {
                cardsOnHead.add(player4.getCardOnHead()); // copies, does not draw, card to cardsOnHead deck
            } catch (Exception e) {
                System.out.println("Exception adding card to player 1 head in headRound().");
            }

            // players make predictions (1 or 0 win or lose)
            for (int i = 0; i < 3; i++) { // all but one player predict, since the last predictor has special constraints

                if (i == 0) {
                    System.out.println("You are the first to predict.");
                } else {
                    System.out.println("Players' predictions so far:");
                    System.out.println(trickPredictions.toString()); // prints the predictions of everyone else
                }

                if (orderPlace % 4 == 1) { // if it's player1's turn
                    System.out.println("Player 1");
                    System.out.println("Other players' cards");
                    // print other players' cards
                    cardsOnHead.printCardAtIndex(1);
                    cardsOnHead.printCardAtIndex(2);
                    cardsOnHead.printCardAtIndex(3);
                    System.out.println(""); // go down a line
                    System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                    trickPredictions.add(player1.predictSelect()); // adds player1's prediction to the array
                } else if (orderPlace % 4 == 2) { // if it's player2's turn
                    System.out.println("Player 2");
                    System.out.println("Other players' cards");
                    // print other players' cards
                    cardsOnHead.printCardAtIndex(0);
                    cardsOnHead.printCardAtIndex(2);
                    cardsOnHead.printCardAtIndex(3);
                    System.out.println("");
                    System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                    trickPredictions.add(player2.predictSelect()); // adds player2's prediction to the array
                } else if (orderPlace % 4 == 3) { // if it's player3's turn
                    System.out.println("Player 3");
                    System.out.println("Other players' cards");
                    // print other players' cards
                    cardsOnHead.printCardAtIndex(0);
                    cardsOnHead.printCardAtIndex(1);
                    cardsOnHead.printCardAtIndex(3);
                    System.out.println("");
                    System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                    trickPredictions.add(player3.predictSelect()); // adds player3's prediction to the array
                } else { // if it's player4's turn
                    System.out.println("Player 4");
                    System.out.println("Other players' cards");
                    // print other players' cards
                    cardsOnHead.printCardAtIndex(0);
                    cardsOnHead.printCardAtIndex(1);
                    cardsOnHead.printCardAtIndex(2);
                    System.out.println("");
                    System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                    trickPredictions.add(player4.predictSelect()); // adds player4's prediction to the array
                }
                orderPlace++; // increases orderPlace
            }

            // get prediction for last player
            lastPredictionHead(orderPlace);
            orderPlace++; // increases orderPlace

            // transfer players' cards to inPlay deck since there is only one card to play
            try {
                inPlay.add(player1.playHeadCard());
                inPlay.add(player2.playHeadCard());
                inPlay.add(player3.playHeadCard());
                inPlay.add(player4.playHeadCard());
            } catch (Exception e) {
                System.out.println("Cannot add card to already full deck.");
            }

            // automatically adjust joker value (if possible) for each player depending on what they predicted
            if (player1.getCardPlayed().getName().equals("Joker")) { // if player played a joker
                if (player1.getTricksPredicted() == 1) { // if player said they would win, set joker to 22
                    player1.getCardPlayed().setValue(22);
                } else { // else set to 0
                    player1.getCardPlayed().setValue(0);
                }
            } else if (player2.getCardPlayed().getName().equals("Joker")) { // if player played a joker
                if (player2.getTricksPredicted() == 1) { // if player said they would win, set joker to 22
                    player2.getCardPlayed().setValue(22);
                } else { // else set to 0
                    player2.getCardPlayed().setValue(0);
                }
            } else if (player3.getCardPlayed().getName().equals("Joker")) {
                if (player3.getTricksPredicted() == 1) { // if player said they would win, set joker to 22
                    player3.getCardPlayed().setValue(22);
                } else { // else set to 0
                    player3.getCardPlayed().setValue(0);
                }
            } else if (player4.getCardPlayed().getName().equals("Joker")) {
                if (player4.getTricksPredicted() == 1) { // if player said they would win, set joker to 22
                    player4.getCardPlayed().setValue(22);
                } else { // else set to 0
                    player4.getCardPlayed().setValue(0);
                }
            }

            // reveal the complete inPlay deck so everyone can see their card
            System.out.println("Complete 'in play' deck:");
            inPlay.printDeck();

            // determine who won the trick
            trickResults();

            // put played cards in discard pile
            inPlayToDiscard();

            // put discarded cards back to main deck
            discardToMain();

        } // if someone has lost, we don't do anything
    } // headRound()

    // plays one trick, this is the most fundamental 'unit' of the game where players actually play cards
    private void trick() {
        System.out.printf("Trick number %d%n", trickNum);

        // all players play their cards
        for (int i = 0; i < 4; i++) { // does it four times, so each player plays

            // either say the player is going first, or print the deck already in play
            if (i == 0) {
                System.out.println("You're going first");
            } else {
                System.out.println("Cards already in play:");
                inPlay.printDeck();
            }

            if (orderPlace % 4 == 1) { // if it's player1's turn
                try {
                    inPlay.add(player1.playCard(1)); // adds player's card to the inPlay deck
                } catch (Exception e) {
                    System.out.println("Exception trick routine player 1 turn: "+ e.getMessage());
                    e.printStackTrace();
                }
            } else if (orderPlace % 4 == 2) { // if it's player2's turn
                try {
                    inPlay.add(player2.playCard(2)); // adds player's card to the inPlay deck
                } catch (Exception e) {
                    System.out.println("Exception trick routine player 2 turn: "+ e.getMessage());
                    e.printStackTrace();
                }
            } else if (orderPlace % 4 == 3) { // if it's player3's turn
                try {
                    inPlay.add(player3.playCard(3)); // adds player's card to the inPlay deck
                } catch (Exception e) {
                    System.out.println("Exception trick routine player 3 turn: "+ e.getMessage());
                    e.printStackTrace();
                }
            } else { // if it's player4's turn
                try {
                    inPlay.add(player4.playCard(4)); // adds player's card to the inPlay deck
                } catch (Exception e) {
                    System.out.println("Exception trick routine player 4 turn: "+ e.getMessage());
                    e.printStackTrace();
                }
            }
            orderPlace++; // increases the order place
        }

        // prints the deck once everyone has played so everyone can see all the cards that were played
        System.out.println("Final 'in play' deck: ");
        inPlay.printDeck();

        // determining who won the trick
        trickResults();

        // put played cards in the discard pile
        inPlayToDiscard();

    } // trick()

    // calculates who won the trick and adjusts numbers accordingly
    private void trickResults() {
        if (player1.getCardPlayed().getValue() > player2.getCardPlayed().getValue() && player1.getCardPlayed().getValue() > player3.getCardPlayed().getValue() && player1.getCardPlayed().getValue() > player4.getCardPlayed().getValue()) {
            player1.setTricksWon(player1.getTricksWon() + 1); // increases tricks won by 1
            System.out.println("Player 1 won this trick.");
            orderPlace = 1; // whoever wins the previous trick starts the next trick (within the same round)
        } else if (player2.getCardPlayed().getValue() > player1.getCardPlayed().getValue() && player2.getCardPlayed().getValue() > player3.getCardPlayed().getValue() && player2.getCardPlayed().getValue() > player4.getCardPlayed().getValue()) {
            player2.setTricksWon(player2.getTricksWon() + 1); // increases tricks won by 1
            System.out.println("Player 2 won this trick.");
            orderPlace = 2;
        } else if (player3.getCardPlayed().getValue() > player1.getCardPlayed().getValue() && player3.getCardPlayed().getValue() > player2.getCardPlayed().getValue() && player3.getCardPlayed().getValue() > player4.getCardPlayed().getValue()) {
            player3.setTricksWon(player3.getTricksWon() + 1); // increases tricks won by 1
            System.out.println("Player 3 won this trick.");
            orderPlace = 3;
        } else {
            player4.setTricksWon(player4.getTricksWon() + 1); // increases tricks won by 1
            System.out.println("Player 4 won this trick.");
            orderPlace = 4;
        }
    } // trickResults()

    // transfers all the cards in the inPlay deck to the discard pile
    private void inPlayToDiscard() {
        for (int i = 0; i < inPlay.getMaxSize(); i++) { // for each of the cards in play
            // System.out.println("Discard: " + i);
            try {
                discardPile.add(inPlay.draw()); // transfer them to the discard pile
            } catch (Exception e) {
                System.out.println("Exception inPlayToDiscard method.");
            }
        }
    } // inPlayToDiscard()

    // transfers all the cards in the discard pile to the main deck
    private void discardToMain() {
        int cardCountAtEnd = discardPile.getCardCount(); // transferring cards changes the card count so we have to copy it to another variable

        for (int i = 0; i < cardCountAtEnd; i++) { // for each of the cards in play
            try {
                mainDeck.add(discardPile.draw()); // transfer them to the discard pile
            } catch (Exception e) {
                System.out.println("Exception discardToMain method.");
            }
        }
    } // discardToMain()

    // reset trick predictions array
    private void trickPredictionsReset() {
        // attach trickPredictions to a new empty array list
        trickPredictions = new ArrayList<>();
    }

    // reset tricks won for each player to 0
    private void tricksWonReset() {
        player1.setTricksWon(0);
        player2.setTricksWon(0);
        player3.setTricksWon(0);
        player4.setTricksWon(0);
    } // tricksWonReset()

    // checks if the last player to predict gave a valid prediction
    private void lastPrediction(int orderPlace) {
        boolean madeGoodPrediction = false;
        int tempPredict;

        // the sum of all the predictions cannot equal the round number, in other words, someone has to lose
        do {
            // have the player who hasn't predicted yet make their predicition
            if (orderPlace % 4 == 1) {
                tempPredict = player1.makePrediction(1);
            } else if (orderPlace % 4 == 2) {
                tempPredict = player2.makePrediction(1);
            } else if (orderPlace % 4 == 3) {
                tempPredict = player3.makePrediction(3);
            } else {
                tempPredict = player4.makePrediction(4);
            }

            // if the prediction doesn't cause the sum to be equal to round number, we're good, if it does, we have to go again
            if (trickPredictions.get(0) + trickPredictions.get(1) + trickPredictions.get(2) + tempPredict == roundNum) {
                System.out.println("L'Enculer! The sum of the predictions cannot equal to number of cards in this round. Make another prediction.");
            } else {
                madeGoodPrediction = true;
            }

        } while (!madeGoodPrediction);
        trickPredictions.add(tempPredict); // once we have an acceptable prediction, we can add it to the predictions array

    } // lastPrediction()

    // checks if the last player to predict gave a valid prediction, only for the head round
    private void lastPredictionHead(int orderPlace) {
        boolean hasGoodPrediction = false;
        int temp;
        System.out.println("Players' predictions so far:");
        System.out.println(trickPredictions.toString()); // prints the predictions of everyone else
        do {
            if (orderPlace % 4 == 1) { // if it's player1's turn
                System.out.println("Player 1");
                System.out.println("Other players' cards");
                // print other players' cards
                cardsOnHead.printCardAtIndex(1);
                cardsOnHead.printCardAtIndex(2);
                cardsOnHead.printCardAtIndex(3);
                System.out.println(""); // go down a line
                System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                temp = player1.predictSelect();
            } else if (orderPlace % 4 == 2) { // if it's player2's turn
                System.out.println("Player 2");
                System.out.println("Other players' cards");
                // print other players' cards
                cardsOnHead.printCardAtIndex(0);
                cardsOnHead.printCardAtIndex(2);
                cardsOnHead.printCardAtIndex(3);
                System.out.println("");
                System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                temp = player2.predictSelect();
            } else if (orderPlace % 4 == 3) { // if it's player3's turn
                System.out.println("Player 3");
                System.out.println("Other players' cards");
                // print other players' cards
                cardsOnHead.printCardAtIndex(0);
                cardsOnHead.printCardAtIndex(1);
                cardsOnHead.printCardAtIndex(3);
                System.out.println("");
                System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                temp = player3.predictSelect();
            } else { // if it's player4's turn
                System.out.println("Player 4");
                System.out.println("Other players' cards");
                // print other players' cards
                cardsOnHead.printCardAtIndex(0);
                cardsOnHead.printCardAtIndex(1);
                cardsOnHead.printCardAtIndex(2);
                System.out.println("");
                System.out.println("Enter your prediction, '1' if you think your card is the highest, '0' if it is NOT the highest");
                temp = player4.predictSelect(); // adds player4's prediction to the array
            }

            if (trickPredictions.get(0) + trickPredictions.get(1) + trickPredictions.get(2) + temp == 1) { // 1 is the number they cannot equal in the head round
                System.out.println("L'Enculer! The sum of the predictions cannot equal to number of cards in this round. Make another prediction.");
            } else {
                hasGoodPrediction = true;
            }

        } while (!hasGoodPrediction);
        trickPredictions.add(temp); // once a valid prediction is made, add it to the trick predictions array
    } // lastPredictionHead()

    private void cardsOnHeadReset() {
        // attach cardsOnHead reference to a new empty deck size 4
        cardsOnHead = new TarotDeck(4);
    }

    // uses each player's current trick count and trick prediction to adjust their score, called at the end of each round
    private void adjustScores() {
        // if a player predicted the number of tricks they would win correctly, they do not lose points
        // if they did not, they lose the absolute value of the difference between their prediction and the number they won
        player1.setScore(player1.getScore() - Math.abs(player1.getTricksPredicted() - player1.getTricksWon()));
        player2.setScore(player2.getScore() - Math.abs(player2.getTricksPredicted() - player2.getTricksWon()));
        player3.setScore(player3.getScore() - Math.abs(player3.getTricksPredicted() - player3.getTricksWon()));
        player4.setScore(player4.getScore() - Math.abs(player4.getTricksPredicted() - player4.getTricksWon()));
    } // adjustScores()

    // deals the appropriate number of cards to each player based on the round number
    private void deal() {
        if (roundNum > 0) { // if it's a 'normal round'
            for (int i = 0; i < roundNum; i++) {
                try {
                    player1.addToHand(mainDeck.draw());
                    player2.addToHand(mainDeck.draw());
                    player3.addToHand(mainDeck.draw());
                    player4.addToHand(mainDeck.draw());
                } catch (Exception e) {
                    System.out.println("Cannot draw from an empty deck");
                }
            }
        } else { // if it's the 'head' round, the roundNum is 0 but we still want to deal a card
            try {
                player1.addToHand(mainDeck.draw());
                player2.addToHand(mainDeck.draw());
                player3.addToHand(mainDeck.draw());
                player4.addToHand(mainDeck.draw());
            } catch (Exception e) {
                System.out.println("Cannot draw from an empty deck");
            }
        }

    } // deal()

    // checks to see if any player has lost, if so returns true, if not returns false
    private boolean playerLost() {
        if (player1.getScore() == 0) {
            return true;
        } else if (player2.getScore() == 0) {
            return true;
        } else if (player3.getScore() == 0) {
            return true;
        } else if (player4.getScore() == 0) {
            return true;
        } else {
            return false;
        }
    } // playerLost()
}
