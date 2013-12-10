package visualizer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("unused")
public class EdgeDetectFlow implements ActionListener {
	String filename;
	String nameExt;
	int index;
	File[] files;
	JFrame edgeFrame;
	JLabel edgeLabel;
	JLabel edgeLabel2;
	JButton progressButton;
	JButton previewButton;
	String edgeDetectValue;
	int edgeLowThres;
	int edgeHighThres;
	JSlider edgeLowSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
	JSlider edgeHighSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
	
	public EdgeDetectFlow(String file, String ext, int i, File[] fileList) {
		filename = file;
		nameExt = ext;
		index = i;
		files = fileList;
	}

	// will return an int (or a file?)
	public void determineEdgeDetect(String num) {
		edgeFrame = new JFrame("EDGE DETECT WOO " + num);
    	
        edgeFrame.setSize(500, 800); //The window Dimensions
        edgeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Get size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Determine the new location of the window
        int w = edgeFrame.getSize().width;
        int h = edgeFrame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
         
        // Move the window
        edgeFrame.setLocation(x, y);
        
        edgeFrame.setContentPane(createEdgeDetectPane(Integer.toString(index + 1)));

        edgeFrame.setVisible(true);
        
	}
	
	public JPanel createEdgeDetectPane(String num) {
	   	JPanel entireGUI = new JPanel();
	   	entireGUI.setLayout(null);
	   	
	   	JPanel textPanel = new JPanel();
	   	textPanel.setLayout(null);
	   	textPanel.setLocation(0, 0);
	   	textPanel.setSize(500, 500);
	   	entireGUI.add(textPanel);
	   	
	   	JPanel buttonPanel = new JPanel();
	   	buttonPanel.setLayout(null);
	   	buttonPanel.setLocation(0,0);
	   	buttonPanel.setSize(500, 800);
	   	entireGUI.add(buttonPanel);
	   	
	   	JLabel welcomeText = new JLabel("Set Edge Detection for Image " + num);
	   	welcomeText.setLocation(0, 0);
	   	welcomeText.setSize(500, 100);
	   	welcomeText.setHorizontalAlignment(0);
	   	textPanel.add(welcomeText);
	   	
        edgeLabel = new javax.swing.JLabel();
        edgeLabel.setText("Edge Detect Low Threshold: ");
	    	
	    edgeLabel.setLocation(0, 100);
	    edgeLabel.setSize(500, 100);
	    edgeLabel.setHorizontalAlignment(0);
	    textPanel.add(edgeLabel);

	    //Turn on labels at major tick marks.
	    edgeLowSlider.setMajorTickSpacing(10);
	    edgeLowSlider.setMinorTickSpacing(1);
	    edgeLowSlider.setPaintTicks(true);
	    edgeLowSlider.setPaintLabels(true);
	    
	    edgeLowSlider.setLocation(0, 200);
	    edgeLowSlider.setSize(100, 100);
	    textPanel.add(edgeLowSlider);
	    
        edgeLabel2 = new javax.swing.JLabel();
        edgeLabel2.setText("Edge Detect High Threshold: ");
	    	
	    edgeLabel2.setLocation(0, 300);
	    edgeLabel2.setSize(500, 100);
	    edgeLabel2.setHorizontalAlignment(0);
	    textPanel.add(edgeLabel2);

	    //Turn on labels at major tick marks.
	    edgeHighSlider.setMajorTickSpacing(10);
	    edgeHighSlider.setMinorTickSpacing(1);
	    edgeHighSlider.setPaintTicks(true);
	    edgeHighSlider.setPaintLabels(true);
	    
	    edgeHighSlider.setLocation(0, 400);
	    edgeHighSlider.setSize(100, 100);
	    textPanel.add(edgeHighSlider);
	    
	    previewButton = new JButton("Preview");
	    previewButton.setLocation(100, 600);
	    previewButton.setSize(100, 50);
	    previewButton.addActionListener(this);
	    buttonPanel.add(previewButton);
	    
	    progressButton = new JButton("Next Pic");
	    progressButton.setLocation(300, 600);
	    progressButton.setSize(100, 50);
	    progressButton.addActionListener(this);
	    buttonPanel.add(progressButton);
	    	
	    //content panes must be opaque
	    entireGUI.setOpaque(true);
	    return entireGUI;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == previewButton) {
			// add image preview
						
			int edgeLowThres = edgeLowSlider.getValue();
			int edgeHighThres = edgeHighSlider.getValue();
			System.out.println(edgeLowThres);
			float lowThres = edgeLowThres * 1.0f;
			float highThres = edgeHighThres * 1.0f;
		    if (lowThres > highThres) {
		    	JOptionPane.showMessageDialog(edgeFrame, "The low threshold needs to be lower than the high threshold!", "Try again", JOptionPane.ERROR_MESSAGE);
			} else {
		    	try {
		    		LoadProcessing createEdgePreview = new LoadProcessing();
		    		createEdgePreview.getEdges(filename, nameExt, index, files, lowThres, highThres);

		    		JFrame f = new JFrame("Load Image Sample");
		    		f.setAlwaysOnTop(true);
		    	    f.add(new LoadImageApp(nameExt));
		    	    f.pack();
		    	    
		            // Get size of the screen
		            Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
		            
		            // Determine the new location of the window
		            int boxw = f.getSize().width;
		            int boxh = f.getSize().height;
		            int coordx = (screendim.width-boxw)/2;
		            int coordy = (screendim.height-boxh)/2;
		             
		            // Move the window
		            f.setLocation(coordx, coordy);
		    	    f.setVisible(true);     
		    	} catch (IOException e1) {
		    		 // TODO Auto-generated catch block
		    		e1.printStackTrace();
		    	}
		    }	
		}
		
		else if(e.getSource() == progressButton) {
			int edgeLowThres = edgeLowSlider.getValue();
			int edgeHighThres = edgeHighSlider.getValue();
			System.out.println(edgeLowThres);
			float lowThres = edgeLowThres * 1.0f;
			float highThres = edgeHighThres * 1.0f;
		    if (lowThres > highThres) {
		    	JOptionPane.showMessageDialog(edgeFrame, "The low threshold needs to be lower than the high threshold!", "Try again", JOptionPane.ERROR_MESSAGE);
			} else {
		    	edgeFrame.dispose();
		    	try {
		    		LoadProcessing createDots = new LoadProcessing();
		    		createDots.loadProcessing(filename, nameExt, index, files, lowThres, highThres);	
		    	} catch (IOException e1) {
		    		 // TODO Auto-generated catch block
		    		e1.printStackTrace();
		    	}
		    }
		 } 
	 }
}
