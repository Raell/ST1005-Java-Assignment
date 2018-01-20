/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JPRG;

/**
 *
 * @author Raell
 */
import javax.swing.JFrame;

public class GameFrameUser {
    
    public static void main(String[] args) {
        
        //creates a game
        GameFrame f = new GameFrame(); //create the frame
        f.setTitle("Puzzle Game"); //sets title
        f.setSize(1150, 620); //sets size
        f.setVisible(true); //sets visibility to true
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //sets program to close on exit
        f.setResizable(false); //sets the frame as not resizable
        
    }
    
}
