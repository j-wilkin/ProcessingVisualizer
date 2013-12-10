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
	JButton recordButton;
	JButton stopButton;
	static int numFiles = 0;
	ScreenRecorder screenRecorder;
	boolean recording = false;
	
	// Welcome window
    @SuppressWarnings("unused")
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
    
	public void fileUpload() throws AWTException {
    	
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
			
			// MAYBE THE SOLUTION
			//processing.core.PApplet sketch = new Sketch();
	        processing.core.PApplet.main(new String[] {"--present", "visualizer.Sketch"});
			//new FullScreenTest().main();
			//new DisplayFrame().setVisible(true);
			//new DisplayFrame();
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
    	
    	JLabel welcomeText = new JLabel("Processing Visualizer");
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

//    		welcomeFrame.dispose();
    		try {
				fileUpload();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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