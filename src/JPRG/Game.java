/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JPRG;

/**
 *
 * @author Raell
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
public class Game {
    
    private String name;
    private int steps, totalpieces, rows, noOfPlays;
    private String difficulty, difficultyname;
    private ArrayList<String> history;
    private ImageIcon[] puzzle, pics;
    private int[] positions;
    private JFrame historyFrame;
    
    public Game() {
        
        //initializing game
        setDifficulty(1); //sets default game difficulty to 'Easy'
        prepareImages(); //prepare the game's images
        //initialize game variables
        steps = 0; //sets the no. of steps taken to 0
        noOfPlays = 0; //sets the no. of games played to 0
        //set up history column titles
        historyFrame = new JFrame();
        history = new ArrayList<String>(); //declare arraylist for history
        history.add("S/N");
        history.add("Difficulty");
        history.add("Player Name");
        history.add("No. of Steps");
        
    }
    
    public void addPlays() {
        
        //method to increment no. of plays
        noOfPlays++;
        
    }
    
    public void addSteps() {
        
        //method to increment no. of steps
        steps++;
        
    }
    
    public int getSteps() {
        
        //return value of no. of steps
        return steps;
        
    }
    
    public int checkPuzzle(int a) {
        
        //method to return correct end position of picture in position a
        int origPos = 0; //dummy value to initialize
        
        for (int i = 0; i < positions.length; i++) { //checks on array of the solution for a match, then return that value
            
            if (positions[i] == a) {
                
                origPos = i;
                
            }
            
        }
        
        return origPos;

    }
    
    public void clearGame() { 
        
        //resets no. of steps to 0
        steps = 0;
        
    }
    
    public void setName(String gname) { 
        
        //method to save user's name
        name = gname;
        
    }
    
    public ImageIcon[] getPuzzle() { 
        
        //method to return randomized puzzle images
        return puzzle;
        
    }
    
    public ImageIcon setAnswer(int position) { 
        
        //returns correct image when puzzle piece is solved
        return pics[position];
    }
    
    public void prepareImages() {
        
        //method to prepare images for the game
        puzzle = new ImageIcon[totalpieces]; //imageicon array to be randomnized and set in game
        pics = new ImageIcon[totalpieces]; //imageicon array in original positions
        
        for (int i = 0; i < pics.length; i++) { //setting up pictures to be added to pics
            
            //creation of link to call images from
            String diff = difficulty + "pic";
            int a = i + 1;
            diff += a + ".jpg";
            pics[i] = new ImageIcon("Images/" + diff); //setting the image itself
            
        }
        
        positions = randomizePositions(totalpieces); //generates positions of pictures in the puzzle
        
        for (int i = 0; i < pics.length; i++) { //copies pics[] to puzzle[] based on the positions that were generated
            
            int a = positions[i];
            puzzle[a] = pics[i];
            
        }
         
    }
    
    public int[] randomizePositions(int num) {
        
       //method that generates an array with non-repeating numbers in each field (a randomnizer)
       int[] random = new int[num]; //declares size based on number of puzzle pieces (difficulty)
       boolean[] check = new boolean[random.length]; //declares a checker to prevent numbers from repeating
       boolean test; //when true this will cause a break in the inner loop
       int a; //value to hold generated number
       
        for (int i = 0; i < random.length; i++) { //outer loop repeats as many times to fill the int[]
            
             do{ //inner loop checks whether generated number is repeated
                 a = (int)(Math.random() * random.length); //generates a number based on length of array
                 test = false; //sets loop checker to be false
                         
                 if (check[a] == false) { //this checks whether the number is already included, if not it executes
                     
                     random[i] = a; //sets number to array
                     check[a] = true; //sets check to true to prevent number from being repeated again
                     test = true; //breaks out of inner loop
                     
                 }
             }while(test == false); //inner loop breaks when number is unique        
        }
        
        return random;
    }
    
    public void setDifficulty(int diff) {
        
        //method to set the difficulty level of the game
        if (diff == 1) { //executes when diificulty is 'Easy'
            
            //sets related variables
            totalpieces = 9;
            rows = 3;
            difficulty = "easy";
            difficultyname = "Easy";
        
        }
        else if (diff == 2) { //executes when diificulty is 'Medium'
            
            //sets related variables
            totalpieces = 16;
            rows = 4;
            difficulty = "med";
            difficultyname = "Medium";
        
        }
        else { //executes when diificulty is 'Hard'
            
            //sets related variables
            totalpieces = 25;
            rows = 5;
            difficulty = "hard";
            difficultyname = "Hard";
            
        }
        
    }
    
    public int getTotalPieces() {
        
        //method to return number of total puzzle pieces
        return totalpieces;
        
    }
    
    public int getRows() {
        
        //method to return number of rows (rows * rows == totalpieces)
        return rows;
        
    }
    
    public void displayHistory() {
        
        //method to display a new frame to show history
        String[] hist = new String[history.size()]; //declares a new String array with the same size as the history arraylist
        history.toArray(hist); //copies history to hist
        JList historyList = new JList(hist); //creates a JList to hold the history values
        //JList initialization
        historyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        historyList.setVisibleRowCount(-1);
        historyList.setFixedCellWidth(100);
        
        historyFrame.dispose(); //dispose of current frame

        //frame initialization
        historyFrame.setVisible(true);
        historyFrame.setSize(420, 300);
        historyFrame.setTitle("History");
        historyFrame.setResizable(false);
        historyFrame.setLayout(new BorderLayout());
        historyFrame.add(historyList, BorderLayout.CENTER); //adds the JList to frame
        
    }
    
    public void setHistory() {
        
        //method to write history values to arraylist
        history.add(String.valueOf(noOfPlays)); //add current play count
        history.add(difficultyname); //add level of difficulty of game
        history.add(name); //add name of player
        history.add(String.valueOf(steps)); //add number of steps taken
        
    }
   
}
