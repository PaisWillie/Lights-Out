import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JApplet;

public class OptionsDesign extends JApplet {

	private static final long serialVersionUID = 1L;

	Image imgTitle, imgCenterLine;
	Image imgOptionsOff, imgOptionsOn;
	Image imgStartingLightsOff, imgStartingLightsOn;
	Image imgBoardSizeOff, imgBoardSizeOn;
	Image imgBackOff, imgBackOn;
	Image imgBoardSkinsOff, imgBoardSkinsOn;
	
	public void init() {
		
		imgOptionsOn = getImage(getDocumentBase(), "Images/Menu/OptionsOn.png");
		imgCenterLine = getImage(getDocumentBase(), "Images/Menu/CenterLine.png");
		imgStartingLightsOff = getImage(getDocumentBase(), "Images/Menu/StartingLightsOff.png");
		imgBoardSizeOff = getImage(getDocumentBase(), "Images/Menu/BoardSizeOff.png");
		imgBoardSkinsOff = getImage(getDocumentBase(), "Images/Menu/BoardSkinsOff.png");
		imgBackOff = getImage(getDocumentBase(), "Images/Menu/BackOff.png");
		
	}
	
	public void start() {
	
		this.setSize(1280,720);
		setBackground(Color.black);
		
	}
	
	public void paint(Graphics g) {

		g.drawImage(imgCenterLine, getWidth()/ 2 - 12, getHeight() / 2 - 180, this);
		g.drawImage(imgOptionsOn, getWidth() / 2 - 300, getHeight() / 2 - 50, this);
		g.drawImage(imgStartingLightsOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
		g.drawImage(imgBoardSizeOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
		g.drawImage(imgBoardSkinsOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
		g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 85, this);
		
		g.setColor(Color.white);
		g.drawRect(getWidth() / 2 - 115, getHeight() / 2 + 95, 85, 30);
		
		// Starting Lights
		g.drawRect(getWidth() / 2 + 40, getHeight() / 2 - 135, 450, 75);
		
		// Board Size
		g.drawRect(getWidth() / 2 + 40, getHeight() / 2 - 35, 335, 60);
		
//		g.setColor(Color.white);
//		g.drawLine(getWidth()/2, 0, getWidth()/2, 720);
		
	}

}
