import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JApplet;

public class MenuDesign extends JApplet {

	private static final long serialVersionUID = 1L;

	Image imgMenuTitle, imgMenuCenterLine;
	Image imgMenuPlayOff, imgMenuPlayOn;
	Image imgMenuOptionsOff, imgMenuOptionsOn;
	Image imgMenuHighScoresOff, imgMenuHighScoreOn;
	
	public void init() {
		
		imgMenuTitle = getImage(getDocumentBase(), "Images/Menu/Title.png");
		imgMenuCenterLine = getImage(getDocumentBase(), "Images/Menu/CenterLine.png");
		imgMenuPlayOff = getImage(getDocumentBase(), "Images/Menu/PlayOff.png");
		imgMenuOptionsOff = getImage(getDocumentBase(), "Images/Menu/OptionsOff.png");
		imgMenuHighScoresOff = getImage(getDocumentBase(), "Images/Menu/HighScores.png");
		
	}
	
	public void start() {
	
		this.setSize(1280,720);
		setBackground(Color.black);
		
	}
	
	public void paint(Graphics g) {
	
		g.drawImage(imgMenuTitle, getWidth() / 2 - 425, getHeight() / 2 - 66, this);
		g.drawImage(imgMenuCenterLine, getWidth()/ 2 - 12, getHeight() / 2 - 180, this);
		g.drawImage(imgMenuPlayOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
		g.drawImage(imgMenuOptionsOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
		g.drawImage(imgMenuHighScoresOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
		
//		g.setColor(Color.white);
//		g.drawLine(getWidth()/2, 0, getWidth()/2, 720);
		
	}

}
