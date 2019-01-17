import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JApplet;

public class StartingLights extends JApplet {

	private static final long serialVersionUID = 1L;

	Image imgTitle, imgCenterLine;
	Image imgOptionsOff, imgOptionsOn;
	Image imgStartingLightsOff, imgStartingLightsOn;
	Image imgBoardSizeOff, imgBoardSizeOn;
	Image imgSkinsOff, imgSkinsOn;
	Image imgBackOff, imgBackOn;
	Image imgDecreaseOff, imgDecreaseOn;
	Image imgIncreaseOff, imgIncreaseOn;
	
	public void init() {
		
		imgTitle = getImage(getDocumentBase(), "Images/Menu/Title.png");
		imgCenterLine = getImage(getDocumentBase(), "Images/Menu/CenterLine.png");
		imgStartingLightsOn = getImage(getDocumentBase(), "Images/Menu/StartingLightsOn.png");
		imgOptionsOn = getImage(getDocumentBase(), "Images/Menu/OptionsOn.png");
		imgBoardSizeOff = getImage(getDocumentBase(), "Images/Menu/BoardSizeOff.png");
		imgSkinsOff = getImage(getDocumentBase(), "Images/Menu/BoardSkinsOff.png");
		imgBackOff = getImage(getDocumentBase(), "Images/Menu/BackOff.png");
		imgDecreaseOff = getImage(getDocumentBase(), "Images/Menu/DecreaseOff.png");
		imgDecreaseOn = getImage(getDocumentBase(), "Images/Menu/DecreaseOn.png");
		imgIncreaseOff = getImage(getDocumentBase(), "Images/Menu/IncreaseOff.png");
		imgIncreaseOn = getImage(getDocumentBase(), "Images/Menu/IncreaseOn.png");
		
	}
	
	public void start() {
	
		this.setSize(1280,720);
		setBackground(Color.black);
		
	}
	
	public void paint(Graphics g) {

		g.drawImage(imgCenterLine, getWidth()/ 2 - 12, getHeight() / 2 - 180, this);
		g.drawImage(imgStartingLightsOn, getWidth() / 2 - 500, getHeight() / 2 - 50, this);
		g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 85, this);
		g.drawImage(imgDecreaseOff, getWidth() / 2 + 50, getHeight() / 2 - 50, this);
		g.drawImage(imgIncreaseOff, getWidth() / 2 + 300, getHeight() / 2 - 50, this);
		
//		g.setColor(Color.white);
//		g.drawLine(getWidth()/2, 0, getWidth()/2, 720);
		
	}

}
