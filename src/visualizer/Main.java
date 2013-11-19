package visualizer;

import java.awt.Dimension;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import net.coobird.thumbnailator.Thumbnails;

@SuppressWarnings("unused")
public class Main {
	
	public static int[] getPixelRGB(int pixel) {
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    return new int[] {red, green, blue};
	  }
	
	public static int getPixelSum(int pixel){
		int[] pixArray = getPixelRGB(pixel);
		return pixArray[0] + pixArray[1] + pixArray[2];
		
	}
	
	public static void loadProcessing(String filename, String nameExt) throws IOException {
		JFrame edgeDetectFrame;
		edgeDetectFrame = new JFrame("Edge Detection for Image" + nameExt);
        edgeDetectFrame.setSize(500, 500); //The window Dimensions
        edgeDetectFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Get size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Determine the new location of the window
        int w = edgeDetectFrame.getSize().width;
        int h = edgeDetectFrame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
         
        // Move the window
        //edgeDetectFrame.setLocation(x, y);
        //edgeDetectFrame.setContentPane(createEdgeDetectPane());
        //edgeDetectFrame.setVisible(true);
		
		BufferedImage image = ImageIO.read( new File( filename ) );
		visualizer.CannyEdgeDetector detector = new visualizer.CannyEdgeDetector();
		detector.setSourceImage(image);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
    	
    	int startW = edges.getWidth();
    	int startH = edges.getHeight();
    	System.out.println(startW);
    	System.out.println(startH);
    	float ratio = 1;
    	System.out.println(ratio);
    	int newW = Math.round(startW * ratio);
    	int newH = Math.round(startH * ratio);
    	System.out.println(newW);
    	System.out.println(newH);
    	BufferedImage thumb = Thumbnails.of(edges).size(newW, newH).asBufferedImage();
    	BufferedImage orig = Thumbnails.of(image).size(newW, newH).asBufferedImage();
    	PrintStream out = new PrintStream(new FileOutputStream("particles" + nameExt + ".txt"));
    	System.setOut(out);
    	out.println(newW + " " + newH);
    	
    	for( int i = 0; i < thumb.getWidth(); i++ )
    	    for( int j = 0; j < thumb.getHeight(); j++ ){
    	    	int pix = thumb.getRGB( i, j );
    	    	int colorPix = orig.getRGB( i, j );
    	        if (getPixelSum(pix) > 25){
    	        	int[] pixArray = getPixelRGB(colorPix);
    	        	String tup = "(" + pixArray[0] + ", " + pixArray[1] + ", " +
    	        			pixArray[2] + ")";
    	        	String coord = i + " " + j + " " + tup;
    	        	out.println(coord);
    	        }
    	    }
    	out.close();
    	
    	//new DisplayFrame().setVisible(true);
	}
	
	 public JPanel createEdgeDetectPane() {
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
	    	JButton nextButton;
	    	nextButton = new JButton("Next");
	    	nextButton.setLocation(200, 350);
	    	nextButton.setSize(100, 50);
	    	//nextButton.addActionListener(this);
	    	buttonPanel.add(nextButton);
	    
	    	
	    	//content panes must be opaque
	    	entireGUI.setOpaque(true);
	    	return entireGUI;
	    }
	
	public static void main(String[] args) {
		//Open a file
		
		UploadGUI intro = new UploadGUI();
		
		//CheatSheet box = new CheatSheet();
		//box.setVisible(true);
		
		// Ask for input - how many files would user like to upload? Give a range
		
		//new FileUploadTest();
		//for (int i = 0; i < numImages; i++) {
		//	new FileUploadTest(i);
		//}	
	}
}