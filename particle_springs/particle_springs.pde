import traer.physics.*;
 
float MIN_MASS = 0.4;      // the minimum mass of a particle
float MAX_MASS = 0.8;      // the maximum mass of a particle

/* FOR JASON. CHANGE THESE VARS FOR WIDTH AND HEIGHT OF SCREEN */
int W = 500;
int H = 336;

int NTH_PARTICLE = 7;
color PARTICLE_COLOR;
boolean TRAIL = true;
int TR_LEN = 90;
boolean RAINBOW = false;

//-------------------------------
boolean FADE = false;
int ALPH = 255;
int TIME;
boolean WAVE = false;
boolean MOUSE = false;
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
  input = loadStrings("particlesSpaceNew.txt");
  //input = loadStrings("particlesRacecarNew.txt");
  LENGTH = input.length;
  locations = new float[2][LENGTH];
  res = readInput(input);  
  W = res[0];
  H = res[1];
   
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
  colors = new color[LENGTH];
  
  //------------------------
  int rand1;
  int rand2;
  int rand3;
  //-----------------------^
  
  // Makes the visible and anchor particles
  for(int i=0; i<LENGTH; i++) {
    particles[i] = physics.makeParticle(random(MIN_MASS, MAX_MASS), locations[0][i], locations[1][i], 0);
    orgParticles[i] = physics.makeParticle(random(MIN_MASS, MAX_MASS), locations[0][i], locations[1][i], 0);
    orgParticles[i].makeFixed();
    // make the moving particles go to their former positions (creates the springs)
    physics.makeSpring(particles[i], orgParticles[i], 0.007, 0.1, 0);
    // make the moving particles get away from the mouse
    physics.makeAttraction(particles[i], mouse, -5000, 0.1);
    //------------------------------
    rand1 = (int)random(0,50);
    rand2 = (int)random(0,50);
    rand3 = (int)random(0,50);
    colors[i] = color(200-rand1,200-rand1,255-rand3);
    //-----------------------------^
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
  println("NTH_PARTICLE: " + NTH_PARTICLE);
  
  mouse.position().set(mouseX, mouseY, 0 );
  PARTICLE_COLOR = setPartColor(mouseX);
  physics.tick();
  float w;
  float posx, posy, wPosy;
  float temp = ((distance(particles[0].position().x(), mouseX, particles[0].position().y(), mouseY))/(distance(0.0, 0.0, (float)W, (float)H)));
  for(int i=0; i<LENGTH; i++) {
    posx = particles[i].position().x();
    posy = particles[i].position().y();
    w = 2;
    //-------------------------------
    if (!WAVE && MOUSE) {
      ALPH = (int)((distance(posx, mouseX, posy, mouseY))/(distance(0.0,0.0,(float)W,(float)H))*255);
    }
    //------------------------------^
    if(i%NTH_PARTICLE == 0) {
      if (RAINBOW) {
        fill(PARTICLE_COLOR, ALPH);
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
        ellipse(posx, posy, w, w);
        println(ALPH);
      }
    }
  }
  //------------------
  if (FADE && ALPH > 0) {
    ALPH -= 2;
  }
  else if (ALPH < 255) {
    ALPH += 2;
  }
  else if (ALPH > 255) {
    ALPH = 255;
  }
  
  if (TIME > W) {
    TIME = 0;
  }
  else {
  TIME += 1;
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
  
  // make things fade out
  if (key == 'f') {
    if (FADE) {
      FADE = false;
      ALPH += 2;
    }
    else {
      FADE = true;
    }
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
  return sin((float)t/50)*noise(num)*80+((float)num/LENGTH)*H;
}

// changes the color of the particles based on mouse location, follows a rainbow pattern
color setPartColor(float mouseX) {
  float r;
  float g;
  float b;
  // sector 1
  if (mouseX < W/6) {
    g = mouseX/(W/6) * 127;
    return color(255,g,0);
  }
  // sector 2
  if (mouseX >= W/6 && mouseX < W/3) {
    g = 128 + (mouseX-(W/6))/(W/6)*127;
    return color(255,g,0);
  }
  // sector 3
  if (mouseX >= W/3 && mouseX < W/2) {
    r = 255 - (mouseX-(W/3))/(W/6)*255;
    return color(r,255,0);
  }
  // sector 4
  if (mouseX >= W/2 && mouseX < 2*W/3) {
    g = 255 - (mouseX-(W/2))/(W/6)*255;
    b = (mouseX-(W/2))/(W/6)*255;
    return color(0,g,b);
  }
  // sector 5
  if (mouseX >= 2*W/3 && mouseX < 5*W/6) {
    r = (mouseX-(2*W/3))/(W/6)*75;
    b = 255 - (mouseX-(2*W/3))/(W/6)*125;
    return color(r,0,b);
  }
  // else we must be in sector 6
  else {
    r = 75 + (mouseX-(5*W/6))/(W/6)*68;
    b = 130 + (mouseX-(5*W/6))/(W/6)*125;
    return color(r,0,b);
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
