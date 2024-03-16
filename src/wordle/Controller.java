/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import java.util.List;
/**
 *
 * @author rafha
 */
public class Controller {
    
    private Model model;
    private View view;

    public Controller(Model model) { //set controller
        this.model = model;
    }

    public void setView(View view) { //set view
        this.view = view;
    }
    
    public void initialise() {
        model.initGame();
    }

    public Boolean isTestState() {
        return model.isTestState();
    }

    public String getAnswer() {
        return model.getAnswer();
    }

    public String getUserGuess() {
        return model.getUserGuess();
    }

    public void setUserGuess(String s) {
        model.setUserGuess(s);
    }

    public Boolean validWord(String s) {
        return model.validWord(model.getUserGuess());
    }

    public void evaluateLetterPosition(String s) {
        model.evaluateLetterPosition(s);
    }

    public void increaseGuessCount() {
        model.increaseGuessCount();
    }

    public Integer getGuessCount() {
        return model.getGuessCount();
    }

    public List<Character> getGreenLetters() {
        return model.getGreenLetters();
    }

    public List<Character> getGoldLetters() {
        return model.getGoldLetters();
    }

    public List<Character> getGreyLetters() {
        return model.getGreyLetters();
    } 
}
