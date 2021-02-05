# linculer-card-game
Description: I wrote this Java program to practice and demonstrate my object-oriented programming skills, specficially inheretence and polymorphism, as well as my array manipulation skills. I modified and built from the classes I created in my card-deck-operations repository. The program allows the users to play a tarot card game called L'Inculer in the terminal. Players take turns predicting how many tricks they will win each round and then play cards to determine who wins each trick. If a player predicts incorrectly, they lose the amount of points that they were errant by. This continues until someone's score has reached zero and the player with the most remaining points wins. Each round has specific rules.
I modified my Card.java and Deck.java files from the card-deck-operations to make the TarotCard.java and TarotDeck.java. TarotHand.java extends TarotDeck.java and adds functionality specific to the cards in a player's hand. Player.java represents the class for the player in the game and houses the methods for a lot of the actions a player can take. Game.java houses the method play() (among other methods) where the highest level funcitonality of the game is written. Player and TarotDeck objects are created and used in play(). Assn3.java contains the main method which asks the user if they would like to play and creates a new game of Game class and calls Game.play() if so.
This is by far the most complex project I have ever done, and I quickly learned how important "good quality" code is. Some of the program's functionality isn't written in the most logical classes which made the rest of the program more difficult to write and organize.
