/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

/**
 *
 * @author rafha
 */
public class Main {
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater( // TL practical formatfro
                new Runnable() {
                    public void run () {
                        makeGUI();}
                }
        );
    }

    public static void makeGUI() {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(model,
                controller);
        //model.addObserver(view);
        model.initGame();
    }
    
}
