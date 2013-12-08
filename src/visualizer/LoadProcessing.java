package visualizer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import net.coobird.thumbnailator.Thumbnails;

public class LoadProcessing {
	JFrame welcomeFrame;
	JButton nextButton;
	
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
	
	public void getEdges(String filename, String nameExt, int index, File[] files, float lowThres, float highThres) throws IOException {
		// Get the image
		BufferedImage image = ImageIO.read( new File( filename ) );
			
		// EDGE DETECTION
		visualizer.CannyEdgeDetector detector = new visualizer.CannyEdgeDetector();
		detector.setLowThreshold(lowThres);
		detector.setHighThreshold(highThres);
		detector.setSourceImage(image);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();

		File outputfile = new File("image" + nameExt + ".jpg");
		ImageIO.write(edges, "jpg", outputfile);
	}
	
	public void loadProcessing(String filename, String nameExt, int index, File[] files, float lowThres, float highThres) throws IOException {
		
		// Get the image
		BufferedImage image = ImageIO.read( new File( filename ) );
		
		// EDGE DETECTION
		visualizer.CannyEdgeDetector detector = new visualizer.CannyEdgeDetector();
		detector.setLowThreshold(lowThres);
		detector.setHighThreshold(highThres);
		detector.setSourceImage(image);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
    	
		// print the dimensions of the photo
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
    	
    	// attempted cropping that's currently not doing anything
    	// TODO set up cropping/resizing
    	BufferedImage thumb = Thumbnails.of(edges).size(newW, newH).asBufferedImage();
    	BufferedImage orig = Thumbnails.of(image).size(newW, newH).asBufferedImage();
    	
    	File outputfile = new File("image" + nameExt + ".jpg");
    	ImageIO.write(edges, "jpg", outputfile);
    	
    	
    	PrintStream out = new PrintStream(new FileOutputStream("particles" + nameExt + ".txt"));
    	System.setOut(out);
    	out.println(newW + " " + newH);
    	
    	// get the colors for each coordinate in the file
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
    	
    	if (index < (files.length - 1)) {
    		EdgeDetectFlow walkthrough = new EdgeDetectFlow(files[index + 1].getAbsolutePath(), Integer.toString(index + 1), index + 1, files);
			walkthrough.determineEdgeDetect(Integer.toString(index + 1));
    	} else {
    		new DisplayFrame().setVisible(true);
    	}
	}
}