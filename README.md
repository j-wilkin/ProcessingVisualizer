ProcessingVisualizer/Hamulizer
====================
A java applet built to create visualizations for music. This applet first performs edge detection on an image and then displays the representation as many small moveable particles (built using Processing open source graphics and visual media language). Comes with an iPad controller and documentation.

## Installation (Computer)

Hamulizer uses two external applications for Midi support: [OSCulator](http://www.osculator.net/) and [TouchOSC](http://hexler.net/software/touchosc) Bridge. Before running the visualizer applet, it is necessary to have both these applications running. 
After ensuring both these applications are running, open the Mouse Pad OSCulator file. This should open in OSCulator and immediately start listening for Midi input. You can be sure TouchOSC Bridge is running by observing the icon in the menu bar.


## Installation (iPad)

The TouchOSC iOS app is necessary to control the visualizer and can be downloaded from the App Store. Once it is installed, the VisualizerController.touchosc must be transferred to the iPad. This can be done through iTunes or through the TouchOSC Editor Mac App. Be sure to configure the MIDI Bridge and OSC connections on the iPad (see Documentation).

## Controls

**Esc**

The “Esc” key exits the application.


**P**

"Springs" is one of the main features of the application. The "Springs" feature allows for the particles to spring back to their original spot. This feature is on by default may be disabled and enabled using the 'P' key. When "Springs" is toggled off, the particles will free float around the canvas.


**M**

The "Mouse" feature is another key feature of the application. This feature allows for the user to repel particles away from the mouse. This feature is also on by default but may be disabled and enabled by using the 'M' key.


**1 2 3 4 5 6 7**

The "Image Transitions" feature allows the user to transition processing between two or more (up to 7) images. By clicking the '1' key, the user can transition into processing the image in Slot 1, and so on for the rest of the numbers.


**Up and Down**

The "Particle Amount" feature allows the user to control how many particles (pixels) are being processed. 'Up' increases the amount of particles and 'Down' decreases the amount of particles.


**[ ]**

The "Particle Size" feature allows the user to control the size/thickness of the particles (pixels) being processed. '[' raises the particles' sizes and ']' lowers the particles' sizes.

￼￼￼￼
**F**

The "Force Particles" feature allows the user create four seperate particles that bounce around the screen, pushing all other particles they come across. This is an On/Off feature, so pressing 'F' again will remove the four force particles.

**T - =**

The "Trails" feature allows for the processed particles to leave an trails behind as they move around. This is also an On/Off feature, so pressing 'T' a second time will remove the trails. '-' decreases the length of the trails, and '=' increases the length of the trails.


**G H J K**

The "Shift" feature allows the user to shift all particles in a certain direction, as dictated by the key pressed. The particles cannot be shifted in a direction twice. The shift is On/Off, so pressing a shift key twice will shift the particles in a direction, and then shift them back to the center.


**/**

The "Rainbow" feature is a fun feature that has been in since the beginning. This feature allows for the particles to take on colors of the rainbow instead of their original pixel colors. This is another On/Off feature, so pressing '/' once will turn it on, and pressing '/' a second time will disable it.
