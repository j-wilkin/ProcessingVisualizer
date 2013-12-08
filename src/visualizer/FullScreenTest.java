package visualizer;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/** @see http://stackoverflow.com/questions/7456227 */
public class FullScreenTest extends JPanel {

    private static final String EXIT = "Exit";
    private JFrame f = new JFrame("FullScreenTest");
    private Action exit = new AbstractAction(EXIT) {

            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispatchEvent(new WindowEvent(
                    f, WindowEvent.WINDOW_CLOSING));
            }
        };

    public FullScreenTest() {
    	System.out.println("Test");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), EXIT);
        this.getActionMap().put(EXIT, exit);
    }

    private void display() {
        //GraphicsEnvironment env =
            //GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice dev = env.getDefaultScreenDevice();
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.setBackground(Color.pink);
        //this.setBackground(Color.pink);
        //f.setResizable(false);
        //f.setUndecorated(true);
        //this.setLayout(null);
        //this.setLocation(0, 0);
    	processing.core.PApplet sketch = new Sketch();
        //this.add(sketch);
        processing.core.PApplet.main(new String[] {"--present", "visualizer.Sketch"});
        //f.add(this);
        //sketch.init();
        //f.pack();
        //dev.setFullScreenWindow(f);
    }

    public static void main() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new FullScreenTest().display();
            }
        });
    }
}
