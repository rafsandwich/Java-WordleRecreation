/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * @author rafha
 */

// model, stores things
public class Model extends Observable {
    
    private boolean isTestState = true; // are we in a test state?
    private boolean isRandomWord = true; // answerWord randomly selected?
    private boolean isWordRecognised; // userWord in common.txt or words.txt?
    
    private final int maxGuessCount = 6; // max allowed number of guesses
    private int guessCount = 0; // counter for number of user guesses
    
    private String userWord; // users entered guess
    private String answerWord; // game selected answer
    
    private List<String> previousWords = new ArrayList<>(); // users previous guess words
    private List<String> commonWords = new ArrayList<>(); // all words from common.txt
    private List<String> answerWords = new ArrayList<>(); // all words from words.txt
    private List<Character> greenLetters; // entered letters in word and in position
    private List<Character> goldLetters; // entered letters in word, not in position
    private List<Character> greyLetters; // entered letters not in word, not in position
    
    public boolean isAlpha(String word) {
        return word.matches("[a-zA-Z]+");
    }
    
    public boolean isRandomWord() {
        return isRandomWord;
    }

    public boolean isTestState() {
        return isTestState;
    }
    
    public boolean isWordRecognised() {
        return isWordRecognised;
    }
    
    public void setWordRecognised(boolean wordRecognised) {
        this.isWordRecognised = wordRecognised;
    }

    public int getMaxGuessCount() {
        assert maxGuessCount == 6; // this should always be 6
        return maxGuessCount;
    }

    public int getGuessCount() {
        assert guessCount <=6 && guessCount >=0; // users guess count should not exceed 6, nor be negative
        return guessCount;
    }
    
    public void setGuessCount(int guessCount) {
        assert guessCount <=6 && guessCount >=0;
        this.guessCount = guessCount;
    }
    
    public String getUserGuess() {
        //assert isAlpha(answerWord); // assert string contains only letters
        //assert answerWord.length()== 5; // assert string is 5 characters long
        return userWord;
    }
    
    public void setUserGuess(String guessWord) {
        //assert isAlpha(answerWord); 
        //assert answerWord.length()== 5;
        this.userWord = guessWord;
    }

    public String getAnswer() {
        assert isAlpha(answerWord); 
        assert answerWord.length()== 5;
        return answerWord;
    }
    
    public void setAnswer(String answerWord) {
        assert isAlpha(answerWord); 
        assert answerWord.length()== 5;
        this.answerWord = answerWord;
    }
    
    public List<String> getCommonWords() {
        return commonWords;
    }
    
    public List<String> getAnswerWords() {
        return answerWords;
    }

    public List<Character> getGreenLetters() {
        return greenLetters;
    }

    public List<Character> getGoldLetters() {
        return goldLetters;
    }

    public List<Character> getGreyLetters() {
        return greyLetters;
    }

    // read common.txt
    public List<String> readCommon() {
        try {
            Scanner sc = new Scanner(new File("common.txt"));
            while (sc.hasNextLine()) {
                //assert isAlpha(sc.nextLine());
                commonWords.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException err) {
            err.printStackTrace();}
        return commonWords; // return populated commondWords list
    }

    // read words.txt
    public List<String> readWords() {
        try {
            Scanner sc = new Scanner(new File("words.txt"));
            while (sc.hasNextLine()) {
                //assert isAlpha(sc.nextLine());
                answerWords.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException err) {
            err.printStackTrace();}
        return answerWords; // return populated answerWords list
    }
    
    // evaluate the letter positions of the users guess vs the answer word, determine green / gold / grey
    public void evaluateLetterPosition(String userGuess) {
        for (int i = 0; i < 5; i++)  {
            char c = userGuess.charAt(i);
            if (c == answerWord.charAt(i)) { // if current character is in word & at the correct position 
                if (!greenLetters.contains(c)) { // if green letters doesn't already contain it
                        greenLetters.add(c); // add it
                    }
                if (goldLetters.contains(c)) { // if letter was already in gold letters
                    goldLetters.remove(Character.valueOf(c)); // remove it
                }
            }
            if (answerWord.contains(String.valueOf(c))) { // if answer contains the current letter
                if (!greenLetters.contains(c)) { // if the letter isn't already in the correct position
                    if (!goldLetters.contains(c)) { // if gold letters doesn't already contain it
                        goldLetters.add(c); // add it to gold letters
                    }
                }
            }
            else {
                if (!greyLetters.contains(c)) {
                    greyLetters.add(c);
                }
            }
        }
        setChanged();
        notifyObservers();
        System.out.println("Letters in the correct position: " + greenLetters);
        System.out.println("Letters in the word, not in the right position: " + goldLetters);
        System.out.println("Letters not in the word: " + greyLetters);
    }
    
    // upon a valid guess, increase the users guess count
    public void increaseGuessCount() {
        guessCount += 1;
        setChanged();
        notifyObservers();
    }

    // is the entered word valid against the .txt's?
    public Boolean validWord(String guess) {
        boolean isValid;
        String[] commonW = commonWords.toArray(new String[]{});
        String[] allowedW = answerWords.toArray(new String[]{});
        List<String> commonList = Arrays.asList(commonW);
        List<String> allowedList = Arrays.asList((allowedW));
        if (commonList.contains(guess) || allowedList.contains(guess)) { // do either .txt contain the users guess word
            isValid = true; // if they do, users guess is a valid word
        }
        else {
            isValid = false;
        }
        return isValid;
    }
    
    // if isRandomWord==True, gets a random word
    public String genRandomWord() {
        String[] words = commonWords.toArray(new String[]{});
        Random rand = new Random();
        String randomWord;
        int randomIndex = rand.nextInt(words.length); 
        randomWord = words[randomIndex]; // word selected randomly from common.txt at index the same as the randomly generated int
        setChanged();
        notifyObservers();
        return randomWord;
    }
   
    // init
    public void initGame() { 
        commonWords = readCommon();
        answerWords = readWords();
        guessCount = 0;
        greenLetters = new ArrayList<>();
        goldLetters = new ArrayList<>();
        greyLetters = new ArrayList<>();
        if (isRandomWord == true) { 
            answerWord = genRandomWord();
        } 
        else {
            String[] commonA = commonWords.toArray(new String[]{});
            answerWord = commonA[0];
            //TODO need to count each game loop, and use this to choose the next word indexed in commonA
        }
    }
}
    
