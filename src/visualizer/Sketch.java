package visualizer;

import java.awt.Color;
import java.awt.Image;
import java.io.File;

import processing.core.*;
import traer.physics.*;

public class Sketch extends PApplet {

	// Recording variables
	Image img;
	double vidRate = 30;
	int frames = 0;
	long[] timeStamps;
	long fTime;
	long sTime;
	//String stri;    // for debugging
	boolean recording;

	// Sketch variables

	float MIN_MASS = 0.4f; // the minimum mass of a particle
	float MAX_MASS = 0.8f; // the maximum mass of a particle

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

	Particle mouse; // particle on mouse position
	Particle[] particles; // the moving particle
	Particle[] orgParticles; // original particles - fixed
	Spring[] springs;
	int[][] Colors; // Color values from the image
	ParticleSystem physics; // the particle system
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

	// @SuppressWarnings("deprecation")
	public void draw() {
		// background(0);
		// Causes particle trails
		noStroke();
		if (TRAIL == true) {
			fill(0, TR_LEN);
		} else {
			fill(0, 255);
		}
		rect(0, 0, W, H);

		println("framerate: " + frameRate);
		println("NTH_PARTICLE: " + NTH_PARTICLE);

		mouse.position().set(mouseX, mouseY, 0);
		PARTICLE_COLOR = rainbowColor(mouseX);
		physics.tick();
		float posx, posy, wPosy;
		for (int i = 0; i < LENGTH; i++) {
			posx = particles[i].position().x();
			posy = particles[i].position().y();
			if (!WAVE && MOUSE) {
				PALPH = (int) ((distance(posx, mouseX, posy, mouseY))
						/ (distance(0f, 0f, (float) W, (float) H)) * 255);
			}
			if (i % NTH_PARTICLE == 0) {
				if (!SPRNG) {
					// particles are bouncing around without springs
					if (posx < 0) {
						particles[i].position().set(0,posy,0);
						particles[i].velocity().add(-2 * particles[i].velocity().x(),0,0);
					}
					if (posx > W) {
						particles[i].position().set(W,posy,0);
						particles[i].velocity().add(-2 * particles[i].velocity().x(),0,0);
					}
					if (posy < 0) {
						particles[i].position().set(posx,0,0);
						particles[i].velocity().add(0,-2 * particles[i].velocity().y(),0);
					}
					if (posy > H) {
						particles[i].position().set(posx,H,0);
						particles[i].velocity().add(0,-2 * particles[i].velocity().y(),0);
					}
				}
				if (!WAVE) {
					// We're displaying the image particles and perhaps fading
					// out the wave particles
					if (RAINBOW) {
						// rainbow fill for image particle
						// fill(rainbowColor(mouseX),PALPH);
						// test, rainbow based on x location of particle
						PARTICLE_COLOR = rainbowColor(posx);
						fill(PARTICLE_COLOR[0], PARTICLE_COLOR[1], PARTICLE_COLOR[2], PALPH);
					}
					// image particle fill
					else {
						// else we're using colors from input
						fill(Colors[i][0], Colors[i][1], Colors[i][2], PALPH);
					}
					ellipse(posx, posy, WEIGHT, WEIGHT);
					if (WALPH > 0) {
						if (RAINBOW) {
							// rainbow fill for wave particles
							// fill(rainbowColor(mouseX),WALPH);
							PARTICLE_COLOR = rainbowColor((float) i / LENGTH * W);
							fill(PARTICLE_COLOR[0], PARTICLE_COLOR[1], PARTICLE_COLOR[2], WALPH);
						} else {
							// else we're using colors from input
							fill(Colors[i][0], Colors[i][1], Colors[i][2], WALPH);
						}
						ellipse(TIME, waveLocation(TIME, i), WEIGHT, WEIGHT);
						// second set of wave particles
						ellipse(TIME2 - 25, waveLocation(TIME2, i), WEIGHT,
								WEIGHT);
					}
				} else {
					// We're displaying the wave particles and perhaps fading
					// out the image particles
					if (RAINBOW) {
						// rainbow fill for image particle
						// fill(rainbowColor(mouseX),WALPH);
						// test, rainbow based on x location of particle
						PARTICLE_COLOR = rainbowColor((float) i / LENGTH * W);
						fill(PARTICLE_COLOR[0], PARTICLE_COLOR[1], PARTICLE_COLOR[2], WALPH);
					}
					// image particle fill
					else {
						// else we're using colors from input
						fill(Colors[i][0], Colors[i][1], Colors[i][2], WALPH);
					}
					ellipse(TIME, waveLocation(TIME, i), WEIGHT, WEIGHT);
					// second set of wave particles
					ellipse(TIME2 - 25, waveLocation(TIME2, i), WEIGHT, WEIGHT);
					if (PALPH > 0) {
						if (RAINBOW) {
							// rainbow fill for wave particles
							// fill(rainbowColor(mouseX),PALPH);
							PARTICLE_COLOR = rainbowColor(posx);
							fill(PARTICLE_COLOR[0], PARTICLE_COLOR[1], PARTICLE_COLOR[2], PALPH);
						} else {
							// else we're using colors from input
							fill(Colors[i][0], Colors[i][1], Colors[i][2], PALPH);
						}
						ellipse(posx, posy, WEIGHT, WEIGHT);
					}
				}
			}
		}
		if (!WAVE) {
			if (PALPH < 255) {
				PALPH += 5;
			}
			if (WALPH > 0) {
				WALPH -= 5;
			}
		} else {
			if (WALPH < 255) {
				WALPH += 5;
			}
			if (PALPH > 0) {
				PALPH -= 5;
			}
		}
		if (TIME > W) {
			TIME = 0;
		} else {
			TIME += 1;
		}
		if (TIME2 > W) {
			TIME2 = 0;
		} else {
			TIME2 += 1;
		}

		if (recording) {
	        // set up current time
			long cTime = System.nanoTime()-fTime;
	        
	        // for debugging
	        //stri = String.valueOf(frames);
	        //text(stri,10,10);
	        
	        if (cTime >= (double)1000/vidRate) { 
	        	// save image to file
	            save("pics/img"+frames+".jpg");
	            // store time stamp of each frame
	            timeStamps[frames] = System.nanoTime();
	            frames++;
	        }
	        fTime = System.nanoTime();
	  }
		
		// ---------------Not working!--------------------------
		if (FADEOUT) {
			PALPH -= 5;
			if (PALPH == 0) {
				FADEOUT = false;
				FADEIN = true;
			}
		}
		if (FADEIN){
			PALPH += 5;
			if (PALPH == 255) {
				FADEIN = false;
			}
		}
		//-----------------------------------------------------

	}

	public void keyPressed() {
		// If the user presses up on the keyboard, fewer particles will be drawn
		// and vice versa
		if (key == CODED) {
			if (keyCode == UP && NTH_PARTICLE < 25) {
				// arbitrary cap on how much the particles are decreased by
				NTH_PARTICLE++;
			} else if (keyCode == DOWN && NTH_PARTICLE > 1) {
				// avoiding math issues
				NTH_PARTICLE--;
			}
		}

		// change particle size
		if (key == '[' && WEIGHT > 1) {
			WEIGHT--;
		}
		if (key == ']' && WEIGHT < 20) {
			WEIGHT++;
		}

		// turn trails on or off
		if (key == 't') {
			if (TRAIL) {
				TRAIL = false;
			} else {
				TRAIL = true;
			}
		}

		// turn rainbow mode on or off
		if (key == '/') {
			if (RAINBOW) {
				RAINBOW = false;
			} else {
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

		// turn on waves/turn off image particles
		if (key == 'w') {
			if (WAVE) {
				WAVE = false;
			} else {
				WAVE = true;
			}
		}

		// turn on/off having particles respond to mouse location, only
		// available in picture mode
		if (key == 'm') {
			if (MOUSE) {
				MOUSE = false;
			} else {
				MOUSE = true;
			}
		}
		
		// press 'r' to start/stop recording
		if (key == 'r') {
		     if(!recording){
		        recording = true;
		        boolean success = (new File("pics")).mkdir();
		        frames = 0;
		        sTime = fTime = System.nanoTime();
		     }
		     else{
		    	recording = false;
		     }
		  }
		// press 's' to export
		if (key == 's') {
		     if(recording)
		        recording = false;
		     for(int i=0; i<11; i++){
		    	 new encodeVideo(frames, vidRate, sTime, timeStamps);
		     }
		}


		if (key == '1'){
			FADEOUT = true;
			setImage("0");
		}
		if (key == '2'){
			FADEOUT = true;
			setImage("1");
		}		
		if (key == '3'){
			FADEOUT = true;
			setImage("2");
		}
		if (key == '4'){
			FADEOUT = true;
			setImage("3");
		}	
		if (key == '5'){
			FADEOUT = true;
			setImage("4");
		}
		if (key == '6'){
			FADEOUT = true;
			setImage("5");
		}	
		if (key == '7'){
			FADEOUT = true;
			setImage("6");
		}	

		// turn the springs on or off, if springs are off the particles will bounce off the walls
		if (key == 'p') {
			if (SPRNG) {
				SPRNG = false;
			    for (int i=0; i<LENGTH; i++) {
			    	springs[i].turnOff();
			    }
			}
			else {
				SPRNG = true;
				for (int i=0; i<LENGTH; i++) {
			    	springs[i].turnOn();
			    }
			}
		}

	}

	float distance(float x1, float y1, float x2, float y2) {
		return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	float waveLocation(int t, int num) {
		// uses a sin function to calculate position of particles, returns y
		return sin((float) t / 70) * noise(num) * 150 + ((float) num / LENGTH)
				* H;
	}

	// changes the Color of the particles based on mouse location, follows a
	// rainbow pattern
	int[] rainbowColor(float mouseX) {
		int r;
		int g;
		int b;
		// sector 1
		if (mouseX < W / 6) {
			g = (int) (mouseX / (W / 6) * 127);
			return new int[] { 255, g, 0 };

		}
		// sector 2
		else if (mouseX >= W / 6 && mouseX < W / 3) {
			g = 128 + (int) (mouseX - (W / 6)) / (W / 6) * 127;
			return new int[] { 255, g, 0 };
		}
		// sector 3
		else if (mouseX >= W / 3 && mouseX < W / 2) {
			r = 255 - (int) (mouseX - (W / 3)) / (W / 6) * 255;
			return new int[] { r, 255, 0 };
		}
		// sector 4
		else if (mouseX >= W / 2 && mouseX < 2 * W / 3) {
			g = 255 - (int) (mouseX - (W / 2)) / (W / 6) * 255;
			b = (int) (mouseX - (W / 2)) / (W / 6) * 255;
			return new int[] { 0, g, b };
		}
		// sector 5
		else if (mouseX >= 2 * W / 3 && mouseX < 5 * W / 6) {
			r = (int) (mouseX - (2 * W / 3)) / (W / 6) * 75;
			b = 255 - (int) (mouseX - (2 * W / 3)) / (W / 6) * 125;
			return new int[] { r, 0, b };
		}
		// else we must be in sector 6
		else {
			r = 75 + (int) (mouseX - (5 * W / 6)) / (W / 6) * 68;
			b = 130 + (int) (mouseX - (5 * W / 6)) / (W / 6) * 125;
			return new int[] { r, 0, b };
		}
	}

	int[] readInput(String[] input) {
		String[] temp;
		int[] res = new int[2];
		// Convert coordinates from String to Float
		for (int i = 1; i < LENGTH; i++) {
			input[i] = input[i].replace("(", "");
			input[i] = input[i].replace(")", "");
			input[i] = input[i].replace(",", "");
			temp = input[i].split(" ");
			Colors[i] = new int[]{Integer.parseInt(temp[2]),
					Integer.parseInt(temp[3]), Integer.parseInt(temp[4])};
			locations[0][i - 1] = Float.parseFloat(temp[0]);
			locations[1][i - 1] = Float.parseFloat(temp[1]);
		}
		// Get/return the resolution of the image
		temp = input[0].split(" ");
		res[0] = Integer.parseInt(temp[0]);
		res[1] = Integer.parseInt(temp[1]);
		return res;
	}
}
