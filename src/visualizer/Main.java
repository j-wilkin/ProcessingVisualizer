package visualizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

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
	
	public static void main(String[] args) throws IOException {
	        
	    	procImage("input/pic0.jpg", "0");
	    	procImage("input/pic1.jpg", "1");
	    	procImage("input/pic2.jpg", "2");
	    	procImage("input/pic3.jpg", "3");
	    	procImage("input/pic4.jpg", "4");
	    	procImage("input/pic5.jpg", "5");
	    	procImage("input/pic6.jpg", "6");
	    	new DisplayFrame().setVisible(true);
	    	
	    	
    
	}
	
	public static void procImage(String filename, String nameExt) throws IOException {
		
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
		
	}
	
}