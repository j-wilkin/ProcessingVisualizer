package visualizer;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import themidibus.MidiBus;

public class UploadGUI implements ActionListener {
	JFrame welcomeFrame;
	JButton nextButton;
	JButton useDefault;
	JButton recordButton;
	JButton stopButton;
	static int numFiles = 0;
	ScreenRecorder screenRecorder;
	boolean recording = false;
	BufferedImage image1, image2;
	
	// Welcome window
    @SuppressWarnings("unused")
	public UploadGUI(){
    	
    	welcomeFrame = new JFrame("Welcome to the Hamulizer 1.0");
    	
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
        
        MidiBus.list();
		MidiBus guiBus = new MidiBus(this, 0, 0);
    }
    

    public void controllerChange(int channel, int number, int value){
    	System.out.println("Channel: " + channel);
    	System.out.println("Number: "+number);
    	System.out.println("Value: "+value);
	
		if (channel == 1 && number == 17){
			if (!recording){
				System.out.println("Recording now!!!");
				try {
					beginRecord();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				recording = true;
			}
			else{
				System.out.println("Encoding now!!!");
	    		try {
					screenRecorder.stop();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		recording = false;
			}
		}	
    }
    

    public void fileUpload() throws AWTException, IOException {    	
    	JFileChooser chooser = new JFileChooser();
    	chooser.setMultiSelectionEnabled(true);
    	chooser.setFileFilter(new ImageFilter());
    	chooser.setAcceptAllFileFilterUsed(false);
		
		if (chooser.showOpenDialog(welcomeFrame) == JFileChooser.APPROVE_OPTION) {
			// Check to make sure files are jpgs and not txt or anything else
			File[] files = chooser.getSelectedFiles();
			numFiles = files.length; 	
			EdgeDetectFlow walkthrough = new EdgeDetectFlow(files[0].getAbsolutePath(), Integer.toString(0), 0, files);
			walkthrough.determineEdgeDetect(Integer.toString(0)); 
		} else {
			welcomeFrame.setVisible(true);
		}
    }
    public void imagePreviews() {
    	
    }
    
    public void startSketch() {
    	new DisplayFrame().setVisible(true);
    }
    
    // Clean up the UI for this
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
    	
    	JLabel welcomeText = new JLabel("The Hamulizer");
    	JLabel welcomeText1 = new JLabel("Transform images into Processing particle sketches, controllable via");
    	JLabel welcomeText1A = new JLabel("iPad with a built-in physics library.");
    	JLabel welcomeText2 = new JLabel("Upload up to 7 unique images or use the default set!");
    	welcomeText.setLocation(0, 0);
    	welcomeText.setSize(500, 100);
    	welcomeText.setFont(new Font("Serif", Font.PLAIN, 30));
    	welcomeText.setHorizontalAlignment(0);
    	welcomeText1.setLocation(0, 100);
    	welcomeText1.setSize(500, 30);
    	welcomeText1.setHorizontalAlignment(0);
    	welcomeText1A.setLocation(0, 130);
    	welcomeText1A.setSize(500, 30);
    	welcomeText1A.setHorizontalAlignment(0);
    	welcomeText2.setLocation(0, 180);
    	welcomeText2.setSize(500, 50);
    	welcomeText2.setHorizontalAlignment(0);
    	textPanel.add(welcomeText);
    	textPanel.add(welcomeText1);
    	textPanel.add(welcomeText1A);
    	textPanel.add(welcomeText2);	
    	
    	nextButton = new JButton("Upload Images");
    	nextButton.setLocation(300, 350);
    	nextButton.setSize(150, 50);
    	nextButton.addActionListener(this);
    	buttonPanel.add(nextButton);

    	useDefault = new JButton("Use Default");
    	useDefault.setLocation(50, 350);
    	useDefault.setSize(150, 50);
    	useDefault.addActionListener(this);
    	buttonPanel.add(useDefault);
   	
    	//content panes must be opaque
    	entireGUI.setOpaque(true);
    	return entireGUI;
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == nextButton)
    	{
    		//welcomeFrame.dispose();
    		welcomeFrame.setVisible(false);
    		try {
				fileUpload();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	} else if(e.getSource() == useDefault) {
    		welcomeFrame.dispose();
	    	LoadProcessing useDefaultImages = new LoadProcessing();
	    	useDefaultImages.useDefaultImages();
    	}
    }
    
    
    public void beginRecord() throws IOException, AWTException{
        
        //Create a instance of GraphicsConfiguration to get the Graphics configuration
        //of the Screen. This is needed for ScreenRecorder class.
        GraphicsConfiguration gc = GraphicsEnvironment//
        .getLocalGraphicsEnvironment()//
        .getDefaultScreenDevice()//
        .getDefaultConfiguration();
 
        //Create a instance of ScreenRecorder with the required configurations
        screenRecorder = new ScreenRecorder(gc,
        new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
        new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
        DepthKey, (int)24, FrameRateKey, Rational.valueOf(60),
        QualityKey, 1.0f,
        KeyFrameIntervalKey, (int) (15 * 55)),
        null,
        null);

 
	    //Call the start method of ScreenRecorder to begin recording
	    screenRecorder.start();
    	
    }
    
    public int getNumFiles(){
    	return numFiles;
    }
}