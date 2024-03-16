/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author rafha
 */
public class View extends JPanel implements Observer, KeyListener, ActionListener{
    
    private final Model model;
    private final Controller controller;
    private final JFrame gameFrame = new JFrame(); 
    private final JPanel gameField = new JPanel(new GridLayout(6,5));
    private final JPanel displayPanel = new JPanel(new GridLayout(5,3));
    private final JPanel keyboardFrame = new JPanel(new GridLayout(4,1));
    private final JPanel keyboardField = new JPanel(new GridLayout(3,8));
    private final JPanel gameButtons = new JPanel(new GridLayout(1,3));
    JLabel answerDisplay = new JLabel();
    JLabel guessCount = new JLabel();
    JLabel[][] wordleBoard = new JLabel[6][5];
    JTextField userInput;
    JButton Backspace;
    JButton Restart;
    JButton Submit;
    JButton keyboardButtons[];
    String strBuffer = "";
    //static final String alphabet = "QWERTYUIOPASDFGHJKLZXCVBNM";
    static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public View(Model model, Controller controller) { // TL practical format
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        createGUI();
        controller.setView(this);
        update(model, null);
    }

    public void createGUI() { 
        gameFrame.setTitle("Wordle");
        gameFrame.setSize(800,800);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new GridLayout(2,1));
        
        userInput = new JTextField(5); // limit virtual keyboard typing to 5 characters max
        userInput.setActionCommand("" + strBuffer);
        displayPanel.setBackground(Color.white);
        displayPanel.setBorder(new LineBorder(Color.black,1));
        keyboardField.setBackground(Color.white);
        keyboardFrame.add(displayPanel);
        keyboardFrame.add(userInput);
        userInput.setFont(new Font("Serif", Font.BOLD, 60)); // user input styling
        
        keyboardButtons = new JButton[alphabet.length()]; // keyboard buttons
        Backspace = new JButton("Backspace");
        Restart = new JButton("Restart");
        Restart.setEnabled(false); // disabled for guessCount = 0
        Submit = new JButton("Submit");
        gameButtons.add(Restart);
        gameButtons.add(Backspace);
        gameButtons.add(Submit);
        Backspace.addActionListener(this);
        Restart.addActionListener(this);
        Submit.addActionListener(this);
        
        for (int i = 0; i < 6; i++) { // populate wordle guesses display grid
            for (int j = 0; j < 5; j++) { 
                wordleBoard[i][j] = new JLabel();
                wordleBoard[i][j].setBorder(new LineBorder(Color.white,5));
                wordleBoard[i][j].setOpaque(true);
                gameField.add(wordleBoard[i][j]);
            }
        }

        for (int i = 0; i < alphabet.length(); i++) { // populate keyboard buttons from alphabet
            keyboardButtons[i] = new JButton("" + alphabet.charAt(i));
            keyboardField.add(keyboardButtons[i]);
            keyboardButtons[i].addActionListener(this);
        }
        keyboardFrame.add(keyboardField);
        keyboardFrame.add(gameButtons);
        gameFrame.add(gameField); // ordered so keyboard is below the wordle board
        gameFrame.add(keyboardFrame);
        gameFrame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        // read from model, update display based on instruction
        assert true;
        int guessNum = controller.getGuessCount();
        String ansW = controller.getAnswer();
        answerDisplay.setText("Answer word: "+ String.valueOf(ansW));
        displayPanel.add(answerDisplay);
        guessCount.setText("Current number of guesses: " +String.valueOf(guessNum));
        displayPanel.add(guessCount);
    }
    
    public void restartGame() {
        // reset guess count,destroy and recreate panels and frames
        // guessCount=0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) { 
        int guessNum = controller.getGuessCount();
        String ansW = controller.getAnswer();
        if (controller.isTestState()) {
            answerDisplay.setText("Answer word: "+ String.valueOf(ansW));
            displayPanel.add(answerDisplay);
        }
        guessCount.setText("Current number of guesses: " +String.valueOf(guessNum));
        displayPanel.add(guessCount);
        
        if (e.getSource() == Backspace) {
            strBuffer = strBuffer.substring(0,strBuffer.length()-1);
            try {
                userInput.setText("" + strBuffer);
            }
            catch (StringIndexOutOfBoundsException exception) {
                JOptionPane.showMessageDialog(gameFrame, "You cannot use backspace on an empty field.");
            }
        }
        else if (e.getSource() == Restart) {
            controller.initialise(); //TODO, does restart the game, but does not clear all jpanel stuffs instead overwrites it as the game is played
            Submit.setEnabled(true);
            for (int i = 0; i < alphabet.length(); i++) {
                keyboardButtons[i].setEnabled(true);
            }
            Backspace.setEnabled(true);
            //gameFrame.dispose();
            //gameFrame.removeAll();
            //gameFrame.remove(gameField);
            //createGUI();
        }
        else if (e.getSource() == Submit) {
            String userGuess = userInput.getText();
            controller.setUserGuess(userGuess);
            System.out.println("Answer for this game is: "+ controller.getAnswer()); // for testing only, print answer to console output
            if (controller.validWord(controller.getUserGuess())){ // if the entered word is valid
                Restart.setEnabled(true);
                controller.evaluateLetterPosition(controller.getUserGuess()); // evaluate letter positions
                for (int j = 0; j < 5; j++) {  
                    wordleBoard[guessNum][j].setText(String.valueOf(userGuess.charAt(j)));
                    wordleBoard[guessNum][j].setHorizontalAlignment(SwingConstants.CENTER);
                    wordleBoard[guessNum][j].setFont(new Font("Serif", Font.BOLD, 30));
                    if ((controller.getGreenLetters()).contains(userGuess.charAt(j))) {
                        wordleBoard[guessNum][j].setBackground(Color.GREEN);
                        //TODO, keyboard index update to green colour too, etc.
                        //where userGuess.charAt(j) = keyboard[String.Valueof(userGuess.chartAt(j)]
                        //keyboard[x].setBackground(Color.Green);
                    }
                    else if(controller.getGoldLetters().contains(userGuess.charAt(j))) {
                        wordleBoard[guessNum][j].setBackground(Color.YELLOW);
                    } else {
                        wordleBoard[guessNum][j].setBackground(Color.GRAY);
                    }
                }
                controller.increaseGuessCount(); // increase user guess count after valid guess attempt
                if (guessNum == 5) {
                    Submit.setEnabled(false);
                    for (int i = 0; i < alphabet.length(); i++) {
                        keyboardButtons[i].setEnabled(false);
                    }
                    Backspace.setEnabled(false);
                }
                if (userGuess.equals(String.valueOf(controller.getAnswer()))) {
                    Submit.setEnabled(false);
                    for (int i = 0; i < alphabet.length(); i++) {
                        keyboardButtons[i].setEnabled(false);
                    }
                    Backspace.setEnabled(false);
                }
            }
            else {
                System.out.println("Invalid word submitted: "+ controller.getUserGuess()); // console output
            }
            guessNum = controller.getGuessCount(); // update guessNum
            guessCount.setText("Current number of guesses: " +String.valueOf(guessNum));
            displayPanel.add(guessCount);
            System.out.println("Current number of guesses: " +String.valueOf(guessNum)); // for console output testing
        }        
        else {
            for (int i = 0; i < alphabet.length(); i++) {
                if (e.getSource() == keyboardButtons[i]) {
                    strBuffer += alphabet.toLowerCase(Locale.ROOT).charAt(i);
                    if (!(userInput.getText().length() > 4)) {
                        userInput.setText("" + strBuffer);
                    }
                    break;
                }
            } // credit to this video for help with the virtual keyboard -> https://www.youtube.com/watch?v=WEeaP6qflOo
        }
    }
}
