package visualizer;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IRational;

public class encodeVideo {
	public encodeVideo(int frames, double vidRate, long sTime, long[] timeStamps){
		IMediaWriter writer;
		
		writer = ToolFactory.makeWriter("processingSketch.mov");
		writer.open();
		writer.setForceInterleave(true);
		writer.addVideoStream(0,0,IRational.make((double)vidRate),1080,720);
		BufferedImage bgr = new BufferedImage(1080,720,BufferedImage.TYPE_3BYTE_BGR);
		  
		for(int i = 0; i<frames; i++){
		     Image img = Toolkit.getDefaultToolkit().getImage("pics/img"+i+".jpg");
		     bgr.getGraphics().drawImage(img, 0, 0, new ImageObserver() {
		          public boolean imageUpdate(Image i, int a, int b, int c, int d, int e) {
		              return true;
		          }
		       }
		     );
		     writer.encodeVideo(0,bgr,timeStamps[i]-sTime,TimeUnit.NANOSECONDS);
		}
		writer.flush();
		writer.close();
	}	
}