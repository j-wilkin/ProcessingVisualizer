import traer.physics.*;
 
float MIN_MASS = 0.4;      // the minimum mass of a particle
float MAX_MASS = 0.8;      // the maximum mass of a particle

/* FOR JASON. CHANGE THESE VARS FOR WIDTH AND HEIGHT OF SCREEN */
int W = 500;
int H = 336;

int NTH_PARTICLE = 7;
boolean TRAIL = true;
int TR_LEN = 90;
boolean RAINBOW = false;

//-------------------------------
int PALPH = 255;
int WALPH = 0;
int TIME, TIME2;
boolean WAVE = false;
boolean MOUSE = false;
float WEIGHT = 5;
//------------------------------^

Particle mouse;            // particle on mouse position
Particle[] particles;      // the moving particle
Particle[] orgParticles;   // original particles - fixed
color[] colors;            // color values from the image
ParticleSystem physics;    // the particle system
float[][] locations; // 2d array of particle locations
String[] input;
int LENGTH;
 
void setup() {
  // GRABS THE LOCATIONS OF PARTICLES FROM THE EDGE-DETECTED PICTURE
  int[] res;
  input = loadStrings("particlesColor.txt");
  LENGTH = input.length;
  locations = new float[2][LENGTH];
  colors = new color[LENGTH];
  res = readInput(input);  
  W = res[0];
  H = res[1];
  TIME = 0;
  TIME2 = W/2;
   
  // Processing Setup
  size(W, H); 
  fill(0, 255);
  rect(0, 0, W, H);
  noStroke();
  ellipseMode(CENTER);
  smooth();
 
  // Particle System + Detect Colors
  physics = new ParticleSystem(0, 0.05);
  //---------------------------------
  physics.setIntegrator(ParticleSystem.MODIFIED_EULER);
  //--------------------------------^
  mouse = physics.makeParticle();            // create a particle for the mouse
  mouse.makeFixed();                         // don't let forces move it
  particles = new Particle[LENGTH];
  orgParticles = new Particle[LENGTH];
  
  // Makes the visible and anchor particles
  for(int i=0; i<LENGTH; i++) {
    particles[i] = physics.makeParticle(random(MIN_MASS, MAX_MASS), locations[0][i], locations[1][i], 0);
    orgParticles[i] = physics.makeParticle(random(MIN_MASS, MAX_MASS), locations[0][i], locations[1][i], 0);
    orgParticles[i].makeFixed();
    // make the moving particles go to their former positions (creates the springs)
    physics.makeSpring(particles[i], orgParticles[i], 0.007, 0.1, 0);
    // make the moving particles get away from the mouse
    physics.makeAttraction(particles[i], mouse, -5000, 0.1);
  }
}
 
void draw() {
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
  //println("NTH_PARTICLE: " + NTH_PARTICLE);
  
  mouse.position().set(mouseX, mouseY, 0 );
  physics.tick();
  float posx, posy, wPosy;
  float temp = ((distance(particles[0].position().x(), mouseX, particles[0].position().y(), mouseY))/(distance(0.0, 0.0, (float)W, (float)H)));
  for(int i=0; i<LENGTH; i++) {
    posx = particles[i].position().x();
    posy = particles[i].position().y();
    //-------------------------------
    if (!WAVE && MOUSE) {
      PALPH = (int)((distance(posx, mouseX, posy, mouseY))/(distance(0.0,0.0,(float)W,(float)H))*255);
    }
    //------------------------------^
    if(i%NTH_PARTICLE == 0) {
      
      if (!WAVE) {
        // We're displaying the image particles and perhaps fading out the wave particles
        if (RAINBOW) {
          // rainbow fill for image particle
          //fill(rainbowColor(mouseX),PALPH);
          // test, rainbow based on x location of particle
          fill(rainbowColor(posx),PALPH);
        }
        // image particle fill
        else {
          // else we're using colors from input
          fill(colors[i],PALPH);
        }
        ellipse(posx,posy,WEIGHT,WEIGHT);
        if (WALPH > 0) {
          if (RAINBOW) {
            // rainbow fill for wave particles
            //fill(rainbowColor(mouseX),WALPH);
            fill(rainbowColor((float)i/LENGTH*W),WALPH);
          }
          else {
            // else we're using colors from input
            fill(colors[i],WALPH);
          }
          ellipse(TIME,waveLocation(TIME,i),WEIGHT,WEIGHT);
          // second set of wave particles
          ellipse(TIME2-25,waveLocation(TIME2,i),WEIGHT,WEIGHT);
        }
      }
      else {
        // We're displaying the wave particles and perhaps fading out the image particles
        if (RAINBOW) {
          // rainbow fill for image particle
          //fill(rainbowColor(mouseX),WALPH);
          // test, rainbow based on x location of particle
          fill(rainbowColor((float)i/LENGTH*W),WALPH);
        }
        // image particle fill
        else {
          // else we're using colors from input
          fill(colors[i],WALPH);
        }
        ellipse(TIME,waveLocation(TIME,i),WEIGHT,WEIGHT);
        // second set of wave particles
        ellipse(TIME2-25,waveLocation(TIME2,i),WEIGHT,WEIGHT);
        if (PALPH > 0) {
          if (RAINBOW) {
            // rainbow fill for wave particles
            //fill(rainbowColor(mouseX),PALPH);
            fill(rainbowColor(posx),PALPH);
          }
          else {
            // else we're using colors from input
            fill(colors[i],PALPH);
          }
          ellipse(posx,posy,WEIGHT,WEIGHT);
        }
      }
      /*if (RAINBOW) {
        fill(rainbowColor(mouseX), ALPH);
      }
      else {
        //fill(255, ALPH);
        fill(colors[i],ALPH);
      }
      //-----------------------------
      if (WAVE) {
      wPosy = waveLocation(TIME,i);
      ellipse(TIME,wPosy, w, w);
      // SECOND SET OF WAVE PARTICLES
      ellipse(TIME+100,wPosy+noise(i)*50, w, w);
      //----------------------------^
      }
      else {
        // Normal particle arrangement
        ellipse(posx, posy, w, w);
      }*/
    }
  }
  //------------------
  if (!WAVE) {
    if (PALPH < 255) {PALPH += 5;}
    if (WALPH > 0) {WALPH -= 5;}
  }
  else {
    if (WALPH < 255) {WALPH += 5;}
    if (PALPH > 0) {PALPH -= 5;}
  }
  if (TIME > W) {
    TIME = 0;
  }
  else {
  TIME += 1;
  }
  if (TIME2 > W) {
    TIME2 = 0;
  }
  else {
  TIME2 += 1;
  }
  //-----------------^
}

void keyPressed() {
  // If the user presses up on the keyboard, fewer particles will be drawn
  // and vice versa
  if (key == CODED) {
    if (keyCode == UP && NTH_PARTICLE < 50) {
      // arbitrary cap on how much the particles are decreased by
      NTH_PARTICLE++;
    }
    else if (keyCode == DOWN && NTH_PARTICLE > 1) {
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
    }
    else {
      TRAIL = true;
    }
  }
  
  // turn rainbow mode on or off
  if (key == 'r') {
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

  // turn on waves/turn off image particles
  if (key == 'w') {
    if (WAVE) {
      WAVE = false;
    }
    else {
      WAVE = true;
    }
  }
  
  // turn on/off having particles respond to mouse location, only available in picture mode
  if (key == 'm') {
    if (MOUSE) {
      MOUSE = false;
    }
    else {
      MOUSE = true;
    }
  }
}

float distance(float x1, float y1, float x2, float y2) {
  return sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
}

float waveLocation(int t, int num) {
  // uses a sin function to calculate position of particles, returns y
  return sin((float)t/70)*noise(num)*150+((float)num/LENGTH)*H;
}

// changes the color of the particles based on mouse location, follows a rainbow pattern
color rainbowColor(float x) {
  float r;
  float g;
  float b;
  // sector 1
  if (x < W/6) {
    g = x/(W/6) * 127;
    return color(255,g,0);
  }
  // sector 2
  if (x >= W/6 && x < W/3) {
    g = 128 + (x-(W/6))/(W/6)*127;
    return color(255,g,0);
  }
  // sector 3
  if (x >= W/3 && x < W/2) {
    r = 255 - (x-(W/3))/(W/6)*255;
    return color(r,255,0);
  }
  // sector 4
  if (x >= W/2 && x < 2*W/3) {
    g = 255 - (x-(W/2))/(W/6)*255;
    b = (x-(W/2))/(W/6)*255;
    return color(0,g,b);
  }
  // sector 5
  if (x >= 2*W/3 && x < 5*W/6) {
    r = (x-(2*W/3))/(W/6)*75;
    b = 255 - (x-(2*W/3))/(W/6)*125;
    return color(r,0,b);
  }
  // else we must be in sector 6
  else {
    r = 75 + (x-(5*W/6))/(W/6)*68;
    b = 130 + (x-(5*W/6))/(W/6)*125;
    return color(r,0,b);
  }
}

int[] readInput(String[] input) {
  String[] temp;
  int[] res = new int[2];
  // Convert coordinates from String to Float
  for(int i=1; i<LENGTH; i++) {
    //------------------------------------------
    input[i] = input[i].replace("(","");
    input[i] = input[i].replace(")","");
    input[i] = input[i].replace(",","");
    temp = input[i].split(" ");
    colors[i] = color(Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]));
    //-----------------------------------------^
    locations[0][i-1] = Float.parseFloat(temp[0]);
    locations[1][i-1] = Float.parseFloat(temp[1]);
  }
  // Get/return the resolution of the image
  temp = input[0].split(" ");
  res[0] = Integer.parseInt(temp[0]);
  res[1] = Integer.parseInt(temp[1]);
  return res;
}
