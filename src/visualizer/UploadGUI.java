package visualizer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class UploadGUI implements ActionListener {
	JFrame welcomeFrame;
	JButton nextButton;
	static int numFiles = 0;
	
	// Welcome window
    public UploadGUI(){
    	
    	welcomeFrame = new JFrame("Welcome to our app!");
    	
        welcomeFrame.setSize(500, 500); //The window Dimensions
        welcomeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Get size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Determine the new location of the window
        int w = welcomeFrame.getSize().width;
        int h = welcomeFrame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
         
        // Move the window
        welcomeFrame.setLocation(x, y);
        
        welcomeFrame.setContentPane(createContentPane());

        welcomeFrame.setVisible(true);
    }
    
    public void fileUpload() {
    	
    	JFileChooser chooser = new JFileChooser();
    	chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(welcomeFrame);
    	File[] files = chooser.getSelectedFiles();
    	System.out.println(files.length);
    	numFiles = files.length;
    	System.out.println(numFiles);
		try
		{
			// for loop for loadProcessing
			for (int i = 0; i < numFiles; i++) {
				Main.loadProcessing(files[i].getAbsolutePath(), Integer.toString(i));
			}
			//EdgeDetectionFlow();
			//Main.loadProcessing(fname, numImage);
			new DisplayFrame().setVisible(true);
		}
		catch(IOException e)
		{
			System.exit(0);
		}
    	
    }
    
    public void imagePreviews() {
    	
    }
    
    public void startSketch() {
    	
    }
    
    public JPanel createContentPane() {
    	JPanel entireGUI = new JPanel();
    	entireGUI.setLayout(null);
    	
    	JPanel textPanel = new JPanel();
    	textPanel.setLayout(null);
    	textPanel.setLocation(0, 0);
    	textPanel.setSize(500, 250);
    	entireGUI.add(textPanel);
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setLayout(null);
    	buttonPanel.setLocation(0,0);
    	buttonPanel.setSize(500, 500);
    	entireGUI.add(buttonPanel);
    	
    	JLabel welcomeText = new JLabel("Welcome to our Processing App (Beta)");
    	JLabel welcomeText2 = new JLabel("Please click next to continue...");
    	welcomeText.setLocation(0, 0);
    	welcomeText.setSize(500, 100);
    	welcomeText.setHorizontalAlignment(0);
    	welcomeText2.setLocation(0, 100);
    	welcomeText2.setSize(500, 100);
    	welcomeText2.setHorizontalAlignment(0);
    	textPanel.add(welcomeText);
    	textPanel.add(welcomeText2);
    	
    	//entireGUI.add(bluePanel);
    	
    	nextButton = new JButton("Next");
    	nextButton.setLocation(200, 350);
    	nextButton.setSize(100, 50);
    	nextButton.addActionListener(this);
    	buttonPanel.add(nextButton);
    	
    	
    	//content panes must be opaque
    	entireGUI.setOpaque(true);
    	return entireGUI;
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == nextButton)
    	{
    		//also could use .dispose()... unsure which atm
    		//welcomeFrame.setVisible(false);
    		welcomeFrame.dispose();
    		fileUpload();
    	}
    }
    
    public int getNumFiles(){
    	return numFiles;
    }
}