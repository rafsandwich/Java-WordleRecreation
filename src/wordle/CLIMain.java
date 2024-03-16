/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import java.util.Locale;
import java.util.Scanner;
/**
 *
 * @author rafha
 */
public class CLIMain {
    
    public static void wordleCLI() {
        Model model = new Model(); // CLIMain talks to the model
        boolean gameLoop = true; // to allow users to play again
        while (gameLoop) {
            model.initGame();
            String gameAnswer = model.getAnswer();
            System.out.println("You are playing Wordle. You have 6 attempts to guess the secret answer word.");
            System.out.println("As you make attempts, you will gain information such as: ");
            System.out.println("1. If that attempts letters were in the right position ");
            System.out.println("2. If they exist somewhere in the word ");
            System.out.println("3. Or if they do not appear at all ");
            System.out.println("");

            if (model.isTestState()) { // if test state is true, display answer word
                System.out.println("Game is in the test state. The secret word is: " +gameAnswer);
            } 
            while (model.getGuessCount() <= 5) {
                System.out.print("Please enter a 5 letter word: ");
                Scanner sc = new Scanner(System.in);
                String userGuess = sc.nextLine();
                userGuess = userGuess.toLowerCase(Locale.ROOT);
                model.setUserGuess(userGuess); 
                model.setWordRecognised(model.validWord(userGuess)); 

                if (userGuess.length() != 5) {
                    System.out.println("The word must contain 5 characters. ");
                } 
                else {
                    if (model.isWordRecognised() == true) {
                        System.out.println("");
                        model.evaluateLetterPosition(userGuess);
                        if (gameAnswer.equals(userGuess)) {
                            System.out.println("");
                            System.out.println("Congratulations, " +userGuess+" was the right answer!");
                            break;
                        } 
                        else if (!gameAnswer.equals(userGuess) && model.getGuessCount() == 5) {
                            System.out.println("");
                            System.out.println("Sorry, you lost. The correct answer was: " + gameAnswer);
                            break;
                        }
                        model.increaseGuessCount();
                        System.out.println("");
                        System.out.println("Attempts left: " + (model.getMaxGuessCount() - model.getGuessCount()));
                    } 
                    else {
                        System.out.println("Your word is invalid. ");
                    }
                }
            }
            System.out.print("Would you like to play again? (1: Yes | 2: No): ");
            Scanner sc = new Scanner(System.in);
            if (sc.nextInt() != 1) {
                gameLoop = false;
            } // TODO, user can choose any int other than 1 to stop, and entering a non-int breaks it due to nextInt
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        wordleCLI();
    }
}
