/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JPRG;

/**
 *
 * @author Raell
 */
import JPRG.GhostImage.GhostedDragImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
public class GameFrame extends JFrame implements ActionListener, MouseListener, ItemListener, MouseMotionListener {
    
    private JLabel titleLbl, nameLbl, diffLbl, themeLbl, backgroundLbl, selectionmodeLbl;
    private ImageIcon background;
    private JButton startBtn, resetBtn, histBtn, exitBtn, origBtn;
    private JButton[] puzBtnArr, finBtnArr;
    private JPanel northPanel, centerPanel, westPanel, eastPanel, southPanel, westInnerPanel, eastInnerPanel, buttonPanel, southWestPanel, southWestInnerPanel;
    private JTextField nameTxt;
    private JTextArea instrTxt;
    private JComboBox diffBox, themeBox, selectionmodeBox;
    private JScrollPane instrScroll;
    private DefaultCaret caret;
    private int totalpieces, rows, selected, remaining, placeselection;
    private boolean started, dragdropmode, exited, isSelected, dragging;
    private GhostedDragImage dragImage;
    private Game g;
    
    public GameFrame() {
        
        //initialization of the game and GUI
        g = new Game();
        
        //initializing values
        totalpieces = g.getTotalPieces(); //get starting total number of puzzle pieces
        rows = g.getRows(); //get total number of rows/columns for puzzle panel
        selected = 100;  //dummy value to set no object is selected
        started = false; //initialize game as not started
        placeselection = 100; //dummy value to set for position to place puzzle piece
        dragdropmode = true; //starts program with drag & drop mode as default
        exited = true; //for 'Drag & Drop', to check whether the mouse is outside the east panel (completed puzzle panel)
        isSelected = false; //for 'Click Selection', to check whether any piece is currently selected
        dragging = false; //initialize that it is not currently dragging
        
        //creation of north panel (title panel)
        northPanel = new JPanel();
        titleLbl = new JLabel("My Puzzle Game");
        titleLbl.setFont(new Font("Serif", Font.BOLD, 24));
        titleLbl.setForeground(Color.WHITE);
        northPanel.add(titleLbl);
        //creation of central panel (main game panel)
        centerPanel = new JPanel();
        
        //generate contents of central panel
        setPuzzle();
        
        //creation of south panel (menu panel)
        southPanel = new JPanel();
        southWestPanel = new JPanel();
        
        //creation of menu box panel in south panel
        southWestInnerPanel = new JPanel();
        //user enters player name here
        nameLbl = new JLabel("Enter player's name:");
        nameTxt = new JTextField();
        //user can set game difficulty here
        diffLbl = new JLabel("Choose the difficulty:");
        String difficulty[] = {"Easy", "Medium", "Hard"};
        diffBox = new JComboBox(difficulty);
        //user can change color theme here
        themeLbl = new JLabel("Select background theme:");
        String theme[] = {"Red", "Blue", "Green"};
        themeBox = new JComboBox(theme);
        //user can set selection mode here
        selectionmodeLbl = new JLabel("Select selection mode:");
        String selection[] = {"Drag & Drop", "Click Selection"};
        selectionmodeBox = new JComboBox(selection);
        //sets color of text
        nameLbl.setForeground(Color.WHITE);
        diffLbl.setForeground(Color.WHITE);
        themeLbl.setForeground(Color.WHITE);
        selectionmodeLbl.setForeground(Color.WHITE);
        //sets itemlistener to combo boxes
        diffBox.addItemListener(this);
        themeBox.addItemListener(this);
        selectionmodeBox.addItemListener(this);

        //adding all components to menu box panel
        southWestInnerPanel.setLayout(new GridLayout(4,2,2,0));
        southWestInnerPanel.add(nameLbl);
        southWestInnerPanel.add(nameTxt);
        southWestInnerPanel.add(diffLbl);
        southWestInnerPanel.add(diffBox);
        southWestInnerPanel.add(themeLbl);
        southWestInnerPanel.add(themeBox);
        southWestInnerPanel.add(selectionmodeLbl);
        southWestInnerPanel.add(selectionmodeBox);
        southWestInnerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        //creation of buttons in menu
        buttonPanel = new JPanel();
        startBtn = new JButton("Start Game");
        resetBtn = new JButton("Reset Game");
        resetBtn.setEnabled(false);
        histBtn = new JButton("Get History");
        exitBtn = new JButton("Exit Game");
        buttonPanel.setLayout(new GridLayout(2,2));
        buttonPanel.add(startBtn);
        buttonPanel.add(histBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(exitBtn);
        //add actionlistener to buttons
        startBtn.addActionListener(this);
        resetBtn.addActionListener(this);
        histBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        
        southWestPanel.setLayout(new BorderLayout());
        southWestPanel.add(southWestInnerPanel, BorderLayout.NORTH);
        southWestPanel.add(buttonPanel, BorderLayout.CENTER);
        southWestPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        
        //creation of instruction text area in south panel
        instrTxt = new JTextArea("Instructions:\n\nEnter your name and click 'Start Game' button to begin the game.\nClick the 'Get History' button to view previous playthroughs.\nClick the 'Exit Game' button to exit the game.",6,40);
        instrTxt.setMargin(new Insets(2,5,2,5));
        //enable scrolling
        instrScroll = new JScrollPane(instrTxt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        instrTxt.setEditable(false); //set as uneditable by users
        instrTxt.setFont(new Font("Sans-Serif", Font.PLAIN, 16));
        instrTxt.setBackground(Color.pink);
        //set to auto-update to scroll to bottom of scrollpane when new messages are added
        caret = (DefaultCaret)instrTxt.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        //creation of button to enable the user to view original puzzle image
        origBtn = new JButton("Click here to view original picture");
        origBtn.setEnabled(false); //initialize as unavailable before game started
        origBtn.addActionListener(this);
        
        //forming up south panel
        southPanel.setLayout(new BorderLayout());
        southPanel.add(southWestPanel, BorderLayout.WEST);
        southPanel.add(instrScroll, BorderLayout.CENTER);
        southPanel.add(origBtn, BorderLayout.EAST);
        southPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Puzzle Game", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans-Serif", Font.PLAIN, 10), Color.WHITE));
        
        //set empty areas as opaque to show background
        southPanel.setOpaque(false);
        southWestPanel.setOpaque(false);
        southWestInnerPanel.setOpaque(false);
        
        //adding all components to frame
        setLayout(new BorderLayout());
        //adds a background image to frame, all components will be placed on top of the background
        background = new ImageIcon("Images/red.jpg");
        backgroundLbl = new JLabel(background);
        setContentPane(backgroundLbl);
        backgroundLbl.setLayout(new BorderLayout());
        backgroundLbl.add(northPanel, BorderLayout.NORTH);
        backgroundLbl.add(centerPanel, BorderLayout.CENTER);
        backgroundLbl.add(southPanel, BorderLayout.SOUTH);
        
        
        addMouseMotionListener(this);
        
        
    }
    
    public void setPuzzle() {
        
        //method to create the contents of center panel
        centerPanel.removeAll(); //initialize center panel to contain nothing
        centerPanel.updateUI(); //reflect updated UI when changes are made
        
        westPanel = new JPanel(); //creation of west panel (puzzle panel)
        westInnerPanel = new JPanel(); //creation of panel to hold puzzle pieces in west panel
        puzBtnArr = new JButton[totalpieces]; //creation of JButton array to hold puzzle pieces and initializing length based on number of puzzle pieces
        
        
        westInnerPanel.setLayout(new GridLayout(rows,rows)); //initializing how puzzle pieces will be laid out
        
        for (int i = 0; i < puzBtnArr.length; i++) { //loop to create buttons in array and initializing puzzle images
            
            ImageIcon[] puzzle = g.getPuzzle(); //grab an image array to set from game object
            puzBtnArr[i] = new JButton(puzzle[i]); //creates a button and assigns the image according to position in image array
            westInnerPanel.add(puzBtnArr[i]);
            //adds listeners
            puzBtnArr[i].addActionListener(this);
            puzBtnArr[i].addMouseListener(this);
            puzBtnArr[i].addMouseMotionListener(this);
            
        }
        //set layout and borders of west panel
        westPanel.setLayout(new BorderLayout());
        westPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Puzzle Pieces", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans-Serif", Font.PLAIN, 10), Color.WHITE));
        westPanel.add(westInnerPanel, BorderLayout.CENTER);
        
        eastPanel = new JPanel(); //creation of east panel (completed puzzle panel)
        eastInnerPanel = new JPanel(); //creation of panel to hold completed puzzle pieces in east panel
        finBtnArr = new JButton[totalpieces]; //creation of JButton array to hold completed puzzle pieces, similar to west panel
        
        eastInnerPanel.setLayout(new GridLayout(rows,rows)); //similar to west panel
        for (int i = 0; i < finBtnArr.length; i++) { //loop to create empty buttons for the JButton array
            
            //same as above
            finBtnArr[i] = new JButton();
            eastInnerPanel.add(finBtnArr[i]);
            //adds listeners
            finBtnArr[i].addActionListener(this);
            finBtnArr[i].addMouseListener(this);
            finBtnArr[i].addMouseMotionListener(this);
            
        }
        //same as west panel
        eastPanel.setLayout(new BorderLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Complete the Puzzle", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans-Serif", Font.PLAIN, 10), Color.WHITE));
        eastPanel.add(eastInnerPanel, BorderLayout.CENTER);
        
        //sets all panel to be transparent to show background
        northPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        eastPanel.setOpaque(false);
        westPanel.setOpaque(false);
        
        //assign both west and east panel to center panel
        centerPanel.setLayout(new GridLayout(1,2,10,0));
        centerPanel.add(westPanel);
        centerPanel.add(eastPanel);
        
    }
    
    public void checkGame(int select) {
        
        //method to check correct placement to puzzle piece
        int correctpos = g.checkPuzzle(selected); //get correct answer from game object for current selected piece
        g.addSteps(); //add steps + 1

        if (select == correctpos) { //condition if selected position for puzzle piece matches correct position
            
            remaining -= 1; //number of puzzle pieces remaining - 1
            finBtnArr[select].setIcon(g.setAnswer(select)); //set corresponding completed button to show puzzle piece
            finBtnArr[select].removeActionListener(this); //removes actionlistener from completed piece
            finBtnArr[select].removeMouseListener(this); //remove mouselistener from completed piece
            
            for (int i = 0; i < totalpieces; i++) //re-enables all unselected puzzle pieces
                puzBtnArr[i].setEnabled(true);
            
            
            puzBtnArr[selected].removeActionListener(this); //same as above but for the puzzle panel
            puzBtnArr[selected].removeMouseListener(this); //same as above but for the puzzle piece
            puzBtnArr[selected].setIcon(null); //removes icon from containing button
            puzBtnArr[selected].setBackground(Color.LIGHT_GRAY); //greys background color
            instrTxt.append("\n\nPuzzle piece " + (selected + 1) + " placed.\n" + remaining + " more to go."); //shows text in instructions
            centerPanel.updateUI(); //reflect UI changes
            selected = 100; //resets selection
            
            if (remaining == 0) { //if all pieces are placed
                
                JOptionPane.showMessageDialog(centerPanel, "Game Completed!\nYou did it in " + g.getSteps() + " steps."); //game end dialog box
                instrTxt.setText("Instructions:\n\nGame Completed!\nYou did it in " + g.getSteps() +" steps.\nClick 'Reset Game' to play again"); //shows text in instructions
                g.addPlays(); //add play count + 1
                g.setHistory(); //writes game data to history
                g.clearGame(); //resets number of steps to 0
                
            }
            
        } 
            
        else
            JOptionPane.showMessageDialog(null, "Wrong! Try again."); //shows message that puzzle is placed wrongly
 
    }
    
    public void setSelection(int select) {
        
        //method to select and deselect puzzle pieces
        selected = select; //sets new selected piece
            
        if (puzBtnArr[select].isEnabled() == true && isSelected == true) { //this causes a deselection
            for (int i = 0; i < totalpieces; i++) { //re-enables all buttons in puzBtnArr
                
                puzBtnArr[i].setEnabled(true);
                                
            }
            
            isSelected = false;
            instrTxt.append("\n\nPuzzle piece " + (selected + 1) + " deselected.");
            selected = 100; //resets selected to dummy value
            
        }
        
        else { //if no piece was previously seleceted, set curret piece as selected
            for (int i = 0; i < totalpieces; i++) { //causes all buttons except selected piece to be disabled

                if (i != select) {
                    
                    puzBtnArr[i].setEnabled(false);
                
                }
                
                isSelected = true;
                
            }
            
            instrTxt.append("\n\nPuzzle piece " + (selected + 1) + " selected.\nPlease click the correct position in the 'Complete the Puzzle' panel to place it.");
            
        }
        
    }
    
    public void actionPerformed(ActionEvent e) { //handles all the action events
        
        if (e.getSource() == startBtn) { //executes when the Start Button is pressed
            if (nameTxt.getText().trim().isEmpty() == false) { //executes if a name is inserted by the user
                
                started = true; //signifies game has started
                startBtn.setEnabled(false); //disable Start Button
                resetBtn.setEnabled(true); //enable Reset Button
                origBtn.setEnabled(true); //enable the button that holds the Original Picture
                nameTxt.setEditable(false); //disable editing of Name field
                
                g.setName(nameTxt.getText()); //update name in game class
                diffBox.setEnabled(false); //disable changing of difficulty
                
                if (dragdropmode == true) //executes when game is started in Drag & Drop mode
                    instrTxt.setText("Welcome " + nameTxt.getText() + ".\n\nGame has started!\nDrag and drop the puzzle pieces into the correct location.");
                
                else if (dragdropmode == false) //executes when game is started in Click Selection mode
                    instrTxt.setText("Welcome " + nameTxt.getText() + ".\n\nGame has started!\nSelect a puzzle piece to begin, you may deselect by clicking on it again.");
                
                remaining = totalpieces; //initialize number of pieces 
                
            }
            
            else 
                JOptionPane.showMessageDialog(null, "Please enter your name."); //executes when no name was inserted
        
        }
        
        else if (e.getSource() == resetBtn) { //exectues when Reset Button is pressed
            
            started = false; //signifies game has ended
            startBtn.setEnabled(true); //enable Start Button
            resetBtn.setEnabled(false); //disable Reset Button
            origBtn.setEnabled(false); //disable button that holds the Original Picture
            diffBox.setEnabled(true); //enable changing of difficulty
            origBtn.setIcon(null); //remove the original picture (if previously shown)
            nameTxt.setText(null); //clears all text in the Name field
            nameTxt.setEditable(true); //enable editing of Name field
            //resets the text fields
            origBtn.setText("Click here to view original picture");
            instrTxt.setText("Instructions:\n\nEnter your name and click 'Start Game' button to begin the game.\nClick the 'Get History' button to view previous playthroughs.\nClick the 'Exit Game' button to exit the game.");
            //reshuffles the puzzle pieces
            g.prepareImages();
            //reset the number of steps
            g.clearGame();
            //rebuild the center panel (puzzle panel)
            setPuzzle();
            
        }
        
        else if (e.getSource() == histBtn) { //executes when History Button is pressed
            
            g.displayHistory(); //game history opens
            
        }
        
        else if (e.getSource() == exitBtn) { //executes when Exit Button is pressed
            
            //exit confirmation dialog opens
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) { //executes when user press 'Yes' on confirmation
            
                //exits game
                JOptionPane.showMessageDialog(null, "Thank you!");
                System.exit(0);
                
            }
            
        }
        
        else if (e.getSource() == origBtn) { //executes when Original Image Buttton is pressed
            
            //clears the text and displays the image
            ImageIcon origIcon = new ImageIcon("Images/original.jpg"); 
            origBtn.setIcon(origIcon);
            origBtn.setText(null);
            
        }
        
        else { //action event handler for 'Click Selection'
            
            for (int i = 0; i < totalpieces; i++) { //checks which button the event occured using loop
            
                if (e.getSource() == puzBtnArr[i] && started == true && dragdropmode == false) { //executes when user tries to select a puzzle piece
                    
                    //executes method to select puzzle pieces
                    setSelection(i);
                    
                }
                
                else if (e.getSource() == finBtnArr[i] && selected != 100 && started == true && dragdropmode == false) { //executes when user tries to place puzzle piece
                    
                    //executes method to check puzzle
                    checkGame(i);
                
                    
                }
            
            }
             
        }
        
    }
    
    public void itemStateChanged(ItemEvent z) {
        
        if (z.getSource() == diffBox && z.getStateChange() == ItemEvent.SELECTED) { //executes when user changes difficulty
            
            if (diffBox.getSelectedItem() == "Easy") { //executes when 'Easy' difficulty is selected
                
                //changes game difficulty and adjusts related variables accordingly
                g.setDifficulty(1);
                rows = g.getRows();
                totalpieces = g.getTotalPieces();
                remaining = totalpieces;
                //reshuffle and rebuild puzzle
                g.prepareImages();
                setPuzzle();
                
            }
            else if (diffBox.getSelectedItem() == "Medium") { //executes when 'Medium' difficulty is selected
                
                //same as above
                g.setDifficulty(2);
                rows = g.getRows();
                totalpieces = g.getTotalPieces();
                remaining = totalpieces;
                g.prepareImages();
                setPuzzle();
                
            }
            else { //executes when 'Hard' difficulty is selected
                
                //same as above
                g.setDifficulty(3);
                rows = g.getRows();
                totalpieces = g.getTotalPieces();
                remaining = totalpieces;
                g.prepareImages();
                setPuzzle();
                 
            }
            
        }
        
        else if (z.getSource() == themeBox && z.getStateChange() == ItemEvent.SELECTED) { //executes when users changes the theme
            
            if (themeBox.getSelectedItem() == "Red") { //executes when 'Red' theme is chosen
                
                //changes background image and sets the color of the instructions field
                background = new ImageIcon("Images/red.jpg");
                backgroundLbl.setIcon(background);
                instrTxt.setBackground(Color.PINK);
                
            }
            else if (themeBox.getSelectedItem() == "Blue") { //executes when 'Blue' theme is chosen
                
                //same as above
                background = new ImageIcon("Images/blue.jpg");
                backgroundLbl.setIcon(background);
                instrTxt.setBackground(Color.LIGHT_GRAY);
                
            }
            else { //executes when 'Green' theme is chosen
                
                //same as above
                background = new ImageIcon("Images/green.jpg");
                backgroundLbl.setIcon(background);
                instrTxt.setBackground(Color.WHITE);
                
            }
            
        }
        
        else if (z.getSource() == selectionmodeBox && z.getStateChange() == ItemEvent.SELECTED) { //executes when user changes the selection mode
            
            if (selectionmodeBox.getSelectedItem() == "Drag & Drop") { //executes when 'Drag & Drop' mode is selected
                
                dragdropmode = true; //sets 'Drag & Drop' mode
                
                if (selected != 100) { //executes when a puzzle piece was previously selected
                    
                    for (int i = 0; i < puzBtnArr.length; i++)
                        puzBtnArr[i].setEnabled(true); //enables all buttons (if not enabled)
                    
                }
                
                selected = 100; //resets selection (if any)
                
                if (started == true) //if selection mode changed when game has started, add instructions
                    instrTxt.append("\n\nNow in Drag & Drop mode\nPlease drag the puzzle pieces into the correct position.");
                
            }
            
            else if (selectionmodeBox.getSelectedItem() == "Click Selection") { //executes when 'Click Selection' mode is enabled
                
                dragdropmode = false; //sets 'Click Selection' mode
                
                if (started == true) //same as above
                    instrTxt.append("\n\nNow in Click Selection mode\nClick to select a puzzle piece, you may deselect by clicking again.");
                
            }
            
        }
        
    }
    
    public void mousePressed(MouseEvent a) { //handles mouse presses

        boolean selectCheck = false; //check to determine if legal press was made 
        
        if (started == true && dragdropmode == true) { //executes when game started and 'Drag & Drop' mode is on
        
            for (int i = 0; i < puzBtnArr.length; i++) { //loop to check exact button pressed

                if (a.getSource() == puzBtnArr[i]) {

                    puzBtnArr[i].setEnabled(false); //disables pressed button
                    selected = i; //sets pressed button as selected
                    selectCheck = true; //shows that legal button was pressed
                    
                    int xpos = 0;
                    int ypos = 0;
                    
                    //sets how much offset the ghost image would start with based on piece size
                    if (diffBox.getSelectedItem() == "Easy") {
                    
                        xpos = -105;
                        ypos = -75;
                        
                    }
                    
                    else if (diffBox.getSelectedItem() == "Medium") {
                    
                        xpos = -85;
                        ypos = -60;
                          
                    }
                    
                    else {
                    
                        xpos = -70;
                        ypos = -50;
                          
                    }
                    
                    Point coord = new Point(xpos,ypos); //sets offsets to a point
                    dragImage = new GhostedDragImage(puzBtnArr[i], MouseInfo.getPointerInfo().getLocation(), puzBtnArr[i].getIcon(), coord); //creates a ghost drag image of the seleceted piece
                    dragging = true;
                    
                }           

            }
            
            if (selectCheck == true) //shows instructions when legal button is pressed
                instrTxt.append("\n\nPuzzle piece " + (selected + 1) + " selected.\nPlease drag into the correct position in the 'Complete the Puzzle' panel.");
        
        }
        
    }
    
    public void mouseReleased(MouseEvent a) { //handles mouse button releases
        
        if (started == true && dragdropmode == true) { //same as above

            if (dragging == true)
                dragImage.dispose(); //deletes any ghost image from selection
            
            for (int i = 0; i < puzBtnArr.length; i++) { //re-enables all buttons (if previously disabled)

                if (puzBtnArr[i].isEnabled() == false) {
                
                    puzBtnArr[i].setEnabled(true);
                
                }
            
            }
            
            if (exited == true) //executes when button is outside the east panel (completed puzzle panel)
                instrTxt.append("\n\nPuzzle piece " + (selected + 1) + " deselected.");
        
            if (placeselection != 100 && selected != 100) //executes when both selected and end position on both west and east panel are defined
                checkGame(placeselection); //executes method to check puzzle
            
            //resets selected and end position
            selected = 100;
            placeselection = 100;
            //ends dragging
            dragging = false;
            
        }  
        
    }
    
    public void mouseClicked(MouseEvent a) {
        
    }
    
    public void mouseExited(MouseEvent a) { //handles mouse exiting the buttons
        
        if (started == true && dragdropmode == true) { //same as above
        
            for (int i = 0; i < finBtnArr.length; i++) { //checks whenever the mouse leaves any button in the completed puzzle panel
            
                if (a.getSource() == finBtnArr[i]) {
                
                    //resets end position and sets that mouse is outside the panel
                    placeselection = 100;
                    exited = true;
                
                }
            
            }
            
        } 
        
    }
    
    public void mouseEntered(MouseEvent a) { //handles mouse entering the buttons
        
        if (started == true && dragdropmode == true) { //same as above

            for (int i = 0; i < finBtnArr.length; i++) { //checks whenever the mouse enters any button in the completed puzzle panel

                if (a.getSource() == finBtnArr[i]) {

                    //sets end position as current button mouse is on and sets that mouse is inside the panel
                    placeselection = i;
                    exited = false;

                }

            }
            
        }
        
    }
    
    public void mouseMoved(MouseEvent b) {
        
        
        
    }
    
    public void mouseDragged(MouseEvent b) { //handles mouse dragging
        
        if (selected != 100 && dragdropmode == true) {
            
            dragImage.move(b.getLocationOnScreen()); //sets ghost image to current mouse position when dragging
            
        }
        
    }

}
