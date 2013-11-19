package visualizer;
import java.awt.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class CheatSheet extends JFrame {
	protected JLabel instructions;
	
	public CheatSheet() {
		this.setTitle("Cheat Sheet");
		this.setSize(450, 600);
		this.setLayout(new GridLayout(8, 1));
		
		instructions = new JLabel("Here are the keyboard inputs for the Processing Sketch");
		this.add(instructions);
		
		createJLabel("Up Arrow: fewer particles");
		createJLabel("Down Arrow: more particles");
		createJLabel("T: toggle trails");
		createJLabel("-: decrease trail length");
		createJLabel("+: increase trail length");
		createJLabel("/: rainbow mode");
	}
	
	public void createJLabel(String stringname) {
		JLabel temp = new JLabel(stringname);
		this.add(temp);
	}
}