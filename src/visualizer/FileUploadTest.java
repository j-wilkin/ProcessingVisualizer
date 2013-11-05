package visualizer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
public class FileUploadTest extends Frame implements ActionListener
{
	FileDialog fd1;
	Button openPlease;
	Label lab1;
	TextArea ta1;
	public FileUploadTest()
	{
		fd1 = new FileDialog(this, "Select File to Open");
		openPlease = new Button("Open File");
		openPlease.setBackground(Color.pink);
		lab1 = new Label("Complete path of the selected file");
		//ta1 = new TextArea(40, 20);
		
		add(openPlease, "South");
		//add(ta1, "Center");
		add(lab1, "North");
		openPlease.addActionListener(this);
		
		setTitle("FileDialog Practice");
		setSize(300, 150);
		setVisible(true);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void actionPerformed(ActionEvent e)
	{
		fd1.setVisible(true);
		lab1.setText("Directory: " + fd1.getDirectory());
		display(fd1.getDirectory() + fd1.getFile());
		//display(fd1.getDirectory() + fd1.getFile(), numImage);
	}
	public void display(String fname)
	//public void display(String fname, int numImage)
	{
		try
		{
			Main.loadProcessing(fname, "0");
			//Main.loadProcessing(fname, numImage);
		}
		catch(IOException e)
		{
			System.exit(0);
		}
	}
}