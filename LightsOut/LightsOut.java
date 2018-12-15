import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JApplet;

public class LightsOut extends JApplet implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	StringBuffer strBuffer;

	final int intBoardSize = 5;
	
	// Initializes 2D array to determine if each light is on.
	boolean[][] boolLightState = new boolean[intBoardSize][intBoardSize];

	//Initializes 2D array to track illuminated state when mouse hovers over light.
	boolean[][] boolLightHover = new boolean[intBoardSize][intBoardSize];
	
	//Initializes 2D array to track which light needs to be pressed for solution.
	boolean[][] boolLightSolution = new boolean[intBoardSize][intBoardSize];
	
	// Initializes 2D array for the x & y coordinates for each light.
	int[][][] intLightPosition = new int[2][intBoardSize][intBoardSize];
	
	final int intLightRadius = 31;

	Font fntSerif, fntSansSerif, fntMonospaced, fntDialog;
	
	Image imgLightOff, imgLightHover, imgLightOn;
	
	boolean requestSolution = false;
	
	public void init() {

		addMouseListener(this);
		addMouseMotionListener(this);

		strBuffer = new StringBuffer();
		
		fntSerif = new Font("Serif", Font.BOLD, 14);
		fntSansSerif = new Font("SansSerif", Font.BOLD, 14);
		fntMonospaced = new Font("Monospaced", Font.BOLD, 14);
		fntDialog = new Font("Dialog", Font.BOLD, 14);
		
		imgLightOn = getImage(getDocumentBase(), "lightOn.png");
		imgLightHover = getImage(getDocumentBase(), "lightHover.png");
		imgLightOff = getImage(getDocumentBase(), "lightOff.png");
		
	}

	public void start() {

		// Read high score files
		// Add x & y coordinates of lights		
		final int board_x = 475;
		final int board_y = 175;
		final int lightSpacing = 75;
		
		for (int depth = 0; depth < 2; depth++)
			for (int row = 0; row < intBoardSize; row++)
				for (int column = 0; column < intBoardSize; column++)
					if (depth == 1)
						intLightPosition[depth][row][column] = board_y + column * lightSpacing;
					else
						intLightPosition[depth][row][column] = board_x + row * lightSpacing;

		boardRandomize();

	}

	public void stop() {

		// Save high score files

	}

	public void destroy() {

	}

	void addItem(String word) {

		System.out.println(word);
		strBuffer.append(word);

		repaint();

	}

	public void paint(Graphics g) {

		final int circleWidth = 4;
		
		Graphics2D gCircle = (Graphics2D) g;
		gCircle.setStroke((new BasicStroke(circleWidth)));
		
		setBackground(Color.black);

		// Set's the applet's display resolution size.
		this.setSize(1280, 720);

		g.setColor(Color.red);

		// Draw a Rectangle around the applet's display area.
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		/* TESTING CIRCLE BORDER */
//		gCircle.drawOval(intLightPosition[0][0][0] - circleWidth, intLightPosition[1][0][0] - circleWidth, (intLightRadius + circleWidth) * 2, (intLightRadius + circleWidth) * 2);
		
		for (int row = 0; row < intBoardSize; row++) {
			for (int column = 0; column < intBoardSize; column++) {
				if (boolLightState[row][column] == false && boolLightHover[row][column] == false)
					g.drawImage(imgLightOff, intLightPosition[0][row][column], intLightPosition[1][row][column], this);
				else if (boolLightState[row][column] && boolLightHover[row][column] == false)
					g.drawImage(imgLightOn, intLightPosition[0][row][column], intLightPosition[1][row][column], this);
				else
					g.drawImage(imgLightHover, intLightPosition[0][row][column], intLightPosition[1][row][column], this);
				if (requestSolution) {
//					boolLightSolution[row][column]
				}
			}
		}

		// display the string inside the rectangle.
		g.drawString(strBuffer.toString(), 10, 20);

	}

	public void mouseEntered(MouseEvent event) {

	}

	public void mouseExited(MouseEvent event) {

	}

	public void mousePressed(MouseEvent event) {

	}

	public void mouseReleased(MouseEvent event) {
		
		int intx = event.getX();
		int inty = event.getY();

		// Test if all lights are off

		mouseToggleLight(intx, inty);
		
		repaint();

		
	}

	public void mouseClicked(MouseEvent event) {

	}

	public void mouseMoved(MouseEvent event) {

		int intx = event.getX();
		int inty = event.getY();

		// Counted loop for testing if mouse coordinates are inside board locations

		mouseHoverLight(intx, inty);
		
	}

	public void mouseDragged(MouseEvent event) {

	}

	public void boardRandomize() {
		
		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++) {
					boolLightState[row][column] = true;
			}
		
		int randomRow, randomColumn;
		
		for (int randomCount = 0; randomCount <= 50; randomCount++) {
			randomRow = (int) (Math.random() * 5);
			randomColumn = (int) (Math.random() * 5);
			toggleLight(randomRow, randomColumn);
		}

	}
	
	public void mouseToggleLight (int x, int y) {
		
		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if ((Math.sqrt (Math.pow ((y - intLightPosition [1][row][column] - intLightRadius), 2) + Math.pow((x - intLightPosition [0][row][column] - intLightRadius), 2))) < intLightRadius)
					toggleAdjacentLights(row, column);
		
	}
	
	public int[] mouseOverLight (int x, int y) {
		
		int[] coordinates = new int[2];
		
		return coordinates;
		
	}
	
	public void toggleAdjacentLights(int row, int column) {
		
		toggleLight(row, column);
		
		if (row + 1 >= 0 && row + 1 < intBoardSize)
			toggleLight(row + 1, column);
		
		if (row - 1 >= 0 && row - 1 < intBoardSize)
			toggleLight(row - 1, column);
		
		if (column + 1 >= 0 && column + 1 < intBoardSize)
			toggleLight(row, column + 1);
		
		if (column - 1 >= 0 && column - 1 < intBoardSize)
			toggleLight(row, column - 1);
		
	}
	
	public void toggleLight(int row, int column) {
		
		if (boolLightState[row][column] == false) 
			boolLightState[row][column] = true;
		else
			boolLightState[row][column] = false;
		
		repaint();
		
	}
	
	public void mouseHoverLight (int x, int y) {
		
		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if ((Math.sqrt (Math.pow ((y - intLightPosition [1][row][column] - intLightRadius), 2) + Math.pow((x - intLightPosition [0][row][column] - intLightRadius), 2))) < intLightRadius)
					boolLightHover[row][column] = true;
				else
					boolLightHover[row][column] = false;
		
		repaint();
		
	}
	
	public void solution () {
		
		for (int row = 0; row < intBoardSize; row++ )
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[row][column]) {
					boolLightSolution[row + 1][column] = true;
				}
		
	}

}