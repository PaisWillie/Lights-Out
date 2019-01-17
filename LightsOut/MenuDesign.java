import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JApplet;

public class MenuDesign extends JApplet {

	private static final long serialVersionUID = 1L;

	Image imgTitle, imgCenterLine;
	Image imgPlayOff, imgMenuPlayOn;
	Image imgOptionsOff, imgMenuOptionsOn;
	Image imgHighScoresOff, imgMenuHighScoreOn;
	
	public void init() {
		
		imgTitle = getImage(getDocumentBase(), "Images/Menu/Title.png");
		imgCenterLine = getImage(getDocumentBase(), "Images/Menu/CenterLine.png");
		imgPlayOff = getImage(getDocumentBase(), "Images/Menu/PlayOff.png");
		imgOptionsOff = getImage(getDocumentBase(), "Images/Menu/OptionsOff.png");
		imgHighScoresOff = getImage(getDocumentBase(), "Images/Menu/HighScoresOff.png");
		
	}
	
	public void start() {
	
		this.setSize(1280,720);
		setBackground(Color.black);
		
	}
	
	public void paint(Graphics g) {
	
		g.drawImage(imgTitle, getWidth() / 2 - 425, getHeight() / 2 - 66, this);
		g.drawImage(imgCenterLine, getWidth()/ 2 - 12, getHeight() / 2 - 180, this);
		g.drawImage(imgPlayOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
		g.drawImage(imgOptionsOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
		g.drawImage(imgHighScoresOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
		
		g.setColor(Color.white);
		// Play button
		g.drawRect(getWidth() / 2 + 35, getHeight() / 2 - 140,  155, 80);
		
		// Options
		g.drawRect(getWidth() / 2 + 35, getHeight() / 2 - 40, 260, 80);
		
		// High scores
		g.drawRect(getWidth() / 2 + 35, getHeight() / 2 + 60, 370, 80);
		
//		g.setColor(Color.white);
//		g.drawLine(getWidth()/2, 0, getWidth()/2, 720);
		
	}

}
