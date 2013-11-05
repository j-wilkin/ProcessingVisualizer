package visualizer;

import java.util.ArrayList;

public class DisplayFrame extends javax.swing.JFrame {
    public DisplayFrame(){
        this.setSize(800, 800); //The window Dimensions
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBounds(0, 0, 50, 50);
        processing.core.PApplet sketch = new Sketch();
        panel.add(sketch);
        this.add(panel);
        sketch.init(); //this is the function used to start the execution of the sketch
        this.setVisible(true);
    }
}