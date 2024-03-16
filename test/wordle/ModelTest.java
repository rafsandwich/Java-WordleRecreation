/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rafha
 */
public class ModelTest {
    
    public ModelTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of increaseGuessCount method, of class Model.
     */
    @Test
    public void testIncreaseGuessCount() {
        System.out.println("increaseGuessCount");
        Model instance = new Model();
        instance.increaseGuessCount();
        instance.increaseGuessCount(); // increase users guess count twice
        assertEquals(true, (instance.getGuessCount() == 2)); // we expect getGuessCount to now return 2
    }

    /**
     * Test of validWord method, of class Model.
     */
    @Test
    public void testValidWord() {
        System.out.println("validWord");
        List<String> validWords = Arrays.asList(new String[]{"feign", "crust", "abcee", "barps"}); // 4 words that are valid, 2 from common.txt and 2 from words.txt
        List<String> invalidWords = Arrays.asList(new String[]{"ooooo", "zxzxz", "mn123", "!Q9?K"}); // 4 words, none of which appear in either .txt
        Model instance = new Model();
        instance.initGame(); 
        for (int i = 0; i < validWords.size(); i++) {
            assertEquals(true, instance.validWord(validWords.get(i))); // expect true, the words exist
        }
        for (int i = 0; i < invalidWords.size(); i++) {
            assertEquals(false, instance.validWord(invalidWords.get(i))); // expect false, words do not exist
        }
    }
    
    /**
     * Test of getGreenLetters method, of class Model.
     */
    @Test
    public void testGetGreenLetters() {
        System.out.println("getGreenLetters");
        Model instance = new Model();
        instance.initGame();
        instance.setAnswer("croak");
        instance.setUserGuess("claws"); // 'c' should be a green letter, as it is in the correct position
        instance.evaluateLetterPosition(instance.getUserGuess());
        assertEquals(true, (instance.getGreenLetters()).contains('c')); // so expect true, instance.getGreenLetters() does contain 'c'
    }
}
