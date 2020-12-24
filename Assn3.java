// Author: Roman Smith
// Class/Assignment: CSE 205/Assignment 3
// Due Date: June 28, 2020
// Description: main class for running the game class for assignment 3


import java.util.Scanner;

public class Assn3 {
    public static void main(String[] args) {
        int selection = 1;

        // play while user wants to play
        while (selection == 1) {
            System.out.println("Would you like to play? Enter '1' to play and any other number to quit.");
            selection = goodInt(); // gets an integer input
            if (selection == 1) { // if user wants to play
                Game newGame = new Game();
                newGame.play(); // play a new game
            }
        }
    } // psvm

    // returns a valid integer
    public static int goodInt() {
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
} // Assn3 class
