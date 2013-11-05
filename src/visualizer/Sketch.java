package visualizer;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;

import processing.core.*;
import traer.physics.*;


public class Sketch extends PApplet {

	// Recording variables
	// IMediaWriter, from Xuggler
	IMediaWriter imw;
	// IStreamCoder, from Xuggler
	IStreamCoder isc;
	BufferedImage bgr;
	Image img;
	//File f;
	PImage p;
	int vidRate = 30;
	long sTime;
	long fTime;
	long[] timeStamps;

	boolean recording;
	
	//Sketch variables
	
	float MIN_MASS = 0.4f;      // the minimum mass of a particle
	float MAX_MASS = 0.8f;      // the maximum mass of a particle

	int W;
	int H;

	int NTH_PARTICLE = 7;
	int[] PARTICLE_COLOR;
	boolean TRAIL = true;
	int TR_LEN = 90;
	boolean RAINBOW = false;
	
	boolean FADEOUT = false;
    boolean FADEIN = false;

    int PALPH = 255;
    int WALPH = 0;
    int TIME, TIME2;
    boolean WAVE = false;
    boolean MOUSE = false;
    boolean SPRNG = true;
    float WEIGHT = 5;

	Particle mouse;            // particle on mouse position
	Particle[] particles;      // the moving particle
	Particle[] orgParticles;   // original particles - fixed
	Spring[] springs;
	int[][] Colors;            // Color values from the image
	ParticleSystem physics;    // the particle system
	float[][] locations; // 2d array of particle locations
	String[] input;
	int LENGTH;
	
	public void setImage(String extName){                
        int[] res;
        input = loadStrings("particles" + extName + ".txt");
        LENGTH = input.length;
        locations = new float[2][LENGTH];
        Colors = new int[LENGTH][3];
        res = readInput(input);
        W = res[0];
        H = res[1];
        TIME = 0;
        TIME2 = W / 2;
        
        // Particle System + Detect Colors
        physics = new ParticleSystem(0, 0.05f);
        physics.setIntegrator(ParticleSystem.MODIFIED_EULER);
        mouse = physics.makeParticle(); // create a particle for the mouse
        mouse.makeFixed(); // don't let forces move it
        particles = new Particle[LENGTH];
        orgParticles = new Particle[LENGTH];
        springs = new Spring[LENGTH];

        // Makes the visible and anchor particles
        for (int i = 0; i < LENGTH; i++) {
                particles[i] = physics.makeParticle(random(MIN_MASS, MAX_MASS),
                                locations[0][i], locations[1][i], 0);
                orgParticles[i] = physics.makeParticle(random(MIN_MASS, MAX_MASS),
                                locations[0][i], locations[1][i], 0);
                orgParticles[i].makeFixed();
                // make the moving particles go to their former positions (creates
                // the springs)
                springs[i] = physics.makeSpring(particles[i], orgParticles[i], 0.007f, 0.1f, 0);
                // make the moving particles get away from the mouse
                physics.makeAttraction(particles[i], mouse, -5000, 0.1f);
        }
        
        fill(0, 255);
        rect(0, 0, W, H);
        // Processing Setup
        size(W, H);
        noStroke();
        ellipseMode(CENTER);
        smooth();
}
	 
	public void setup() {
		// set up an array to hold the time stamps for each frame
        timeStamps = new long[100000];
        
        // GRABS THE LOCATIONS OF PARTICLES FROM THE EDGE-DETECTED PICTURE
        setImage("0");
        frameRate(30);
	}
	 
	//@SuppressWarnings("deprecation")
	public void draw() {
	  //background(0);
	  // Causes particle trails
	  noStroke();
	  if (TRAIL == true) {
	    fill(0, TR_LEN);
	  }
	  else {
	    fill(0,255);
	  }
	  rect(0, 0, W, H);
	  
	  println("framerate: " + frameRate);
	  println("NTH_PARTICLE: " + NTH_PARTICLE);
	  
	  mouse.position().set(mouseX, mouseY, 0 );
	  PARTICLE_COLOR = setPartColor(mouseX);
	  physics.tick();
	  float w;
	  float posx, posy;
	  for(int i=0; i<LENGTH; i++) {
	    posx = particles[i].position().x();
	    posy = particles[i].position().y();
	    w = 2;
	    if(i%NTH_PARTICLE == 0) {
	      if (RAINBOW) {
	        fill(PARTICLE_COLOR[0], PARTICLE_COLOR[1], PARTICLE_COLOR[2]);
	      }
	      else {
	        fill(255);
	      }
	      ellipse(posx, posy, w, w);
	    }
	  }
	
	  if (recording) {
	      // the IMediaWriter should be open from avRecorderSetup(), which is called when 'r' is pressed
	      if (imw.isOpen()) {
	        // set cTime 
	        // (not sure what cTime does, or if this if statement is necessary, havent removed to check)
	        long cTime = System.nanoTime()-fTime;
	        if (cTime >= (double)1000/vidRate) {
	          
	          // save image to file
	          save("img.jpg");
	          // load image to PImage
	          p = loadImage("img.jpg");          
	          // convert PImage to Image
	          img = p.getImage();
	          // convert Image to BufferedImage
	          bgr.getGraphics().drawImage(img, 0, 0, new ImageObserver() {
	              public boolean imageUpdate(Image i, int a, int b, int c, int d, int e) {
	                return true;
	              }
	            }
	          );
	          
	          // encode video
	          imw.encodeVideo(0, bgr, System.nanoTime()-sTime, TimeUnit.NANOSECONDS);
	          
	          // set fTime
	          // again, not sure what fTime does, scared to mess with it, I'll be mucking around with it eventually
	          fTime = System.nanoTime();
	        }
	      }
	    }
	
	}

	public void keyPressed() {
	  // If the user presses up on the keyboard, fewer particles will be drawn
	  // and vice versa
	  if (key == CODED) {
	    if (keyCode == UP && NTH_PARTICLE < 25) {
	      // arbitrary cap on how much the particles are decreased by
	      NTH_PARTICLE++;
	    }
	    else if (keyCode == DOWN && NTH_PARTICLE > 1) {
	      // avoiding math issues
	      NTH_PARTICLE--;
	    }
	  }
	  
	  // turn trails on or off
	  if (key == 't') {
	    if (TRAIL) {
	      TRAIL = false;
	    }
	    else {
	      TRAIL = true;
	    }
	  }
	  
	  // turn rainbow mode on or off
	  if (key == '/') {
	    if (RAINBOW) {
	      RAINBOW = false;
	    }
	    else {
	      RAINBOW = true;
	    }
	  }
	  
	  // increase or decrease trail length
	  if (key == '-' && TR_LEN > 4) {
	    TR_LEN -= 5;
	  }
	  if (key == '=' && TR_LEN < 251) {
	    TR_LEN += 5;
	  }
	  
	  if (key == '1'){
          FADEOUT = true;
          setImage("0");
	  }
	  if (key == '2'){
          FADEOUT = true;
          setImage("1");
	  }                
// Video recording keys	  
//	  // if 'r' key is pressed while not recording...
//	  if (key == 'r') {
//	     if(!recording){
//	        // Begin the recording process
//	        println("Recording...");
//	        // avRecorderSetup() is located right below
//	        avRecorderSetup(); 
//	        recording = true;  
//	     }
//	  }
//	  // if 's' key is pressed while recording...
//	  if (key == 's') {
//	     if(recording){
//	        // stop the recording process and save the recording
//	        println("Saving...");
//	        imw.flush();
//	        imw.close();
//	        recording = false;
//	        // haven't figured this out just yet, but I'd like to delete img.jpg after all is said and done
//	        //f = new File("img.jpg");
//	        //f.delete();
//	        println("Saved.");
//	     }
//	  }
	  
	  
	}

	// changes the Color of the particles based on mouse location, follows a rainbow pattern
	int[] setPartColor(float mouseX) {
	  int r;
	  int g;
	  int b;
	  // sector 1
	  if (mouseX < W/6) {
	    g = (int)(mouseX/(W/6) * 127);
	    return new int[]{255,g,0};
	    
	  }
	  // sector 2
	  else if (mouseX >= W/6 && mouseX < W/3) {
	    g = 128 + (int)(mouseX-(W/6))/(W/6)*127;
	    return new int[]{255,g,0};
	  }
	  // sector 3
	  else if (mouseX >= W/3 && mouseX < W/2) {
	    r = 255 - (int)(mouseX-(W/3))/(W/6)*255;
	    return new int[]{r,255,0};
	  }
	  // sector 4
	  else if (mouseX >= W/2 && mouseX < 2*W/3) {
	    g = 255 - (int)(mouseX-(W/2))/(W/6)*255;
	    b = (int)(mouseX-(W/2))/(W/6)*255;
	    return new int[]{0,g,b};
	  }
	  // sector 5
	  else if (mouseX >= 2*W/3 && mouseX < 5*W/6) {
	    r = (int)(mouseX-(2*W/3))/(W/6)*75;
	    b = 255 - (int)(mouseX-(2*W/3))/(W/6)*125;
	    return new int[]{r,0,b};
	  }
	  // else we must be in sector 6
	  else {
	    r = 75 + (int)(mouseX-(5*W/6))/(W/6)*68;
	    b = 130 + (int)(mouseX-(5*W/6))/(W/6)*125;
	    return new int[]{r,0,b};
	  }
	}

	int[] readInput(String[] input) {
	  String[] temp;
	  int[] res = new int[2];
	  // Convert coordinates from String to Float
	  for(int i=1; i<LENGTH; i++) {
	    temp = input[i].split(" ");
	    locations[0][i-1] = Float.parseFloat(temp[0]);
	    locations[1][i-1] = Float.parseFloat(temp[1]);
	  }
	  // Get/return the resolution of the image
	  temp = input[0].split(" ");
	  res[0] = Integer.parseInt(temp[0]);
	  res[1] = Integer.parseInt(temp[1]);
	  return res;
	}
	
	public void avRecorderSetup(){
		  // set the IMediaWriter to write to (and create) processingSketch.mp4
		  imw = ToolFactory.makeWriter(sketchPath("processingSketch.mp4"));
		  // open processingSketch.mp4
		  imw.open();
		  // ??
		  imw.setForceInterleave(true);
		  // start adding the stream
		  imw.addVideoStream(0,0,IRational.make((double)vidRate),width,height);
		  // ??
		  isc = imw.getContainer().getStream(0).getStreamCoder();
		  // ??
		  bgr = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		  // ??
		  sTime = fTime = System.nanoTime();
		}
	
	
}