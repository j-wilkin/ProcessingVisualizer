package visualizer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
@SuppressWarnings({ "serial", "unused" })
public class DisplayFrame extends javax.swing.JFrame {
    public DisplayFrame(){
        // Get size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);

    	// Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(null);
        
        this.setLocation(0, 0);
        this.setPreferredSize(new Dimension(dim.width, dim.height)); //The window Dimensions
        processing.core.PApplet sketch = new Sketch();

        panel.add(sketch);
        this.add(panel);
        sketch.init(); //this is the function used to start the execution of the sketch
        this.setVisible(true);

	    
    }
}