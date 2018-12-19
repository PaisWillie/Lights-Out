import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.JApplet;

public class LightsOut extends JApplet implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	StringBuffer strBuffer;

	final int intBoardSize = 5;

	// Initializes 2D array to determine if each light is on.
	boolean[][] boolLightState = new boolean[intBoardSize][intBoardSize];

	// Initializes 2D array to track illuminated state when mouse hovers over light.
	boolean[][] boolLightHover = new boolean[intBoardSize][intBoardSize];

	// Initializes 2D array to track which light needs to be pressed for solution.
	boolean[][] boolLightSolution = new boolean[intBoardSize][intBoardSize];

	// Initializes 2D array for the x & y coordinates for each light.
	int[][][] intLightPosition = new int[2][intBoardSize][intBoardSize];

	final int intLightRadius = 31;

	Font fntSerif, fntSansSerif, fntMonospaced, fntDialog;

	Image imgLightOff, imgLightHoverOn, imgLightHoverOff, imgLightOn, imgTitle;

	boolean boolRequestSolution = false;

	boolean boolGameWin = false;

	public void init() {

		addMouseListener(this);
		addMouseMotionListener(this);

		fntSerif = new Font("Serif", Font.BOLD, 14);
		fntSansSerif = new Font("SansSerif", Font.BOLD, 14);
		fntMonospaced = new Font("Monospaced", Font.BOLD, 14);
		fntDialog = new Font("Dialog", Font.BOLD, 14);

		imgLightOn = getImage(getDocumentBase(), "lightOn.png");
		imgLightHoverOn = getImage(getDocumentBase(), "lightHoverOn.png");
		imgLightHoverOff = getImage(getDocumentBase(), "lightHoverOff.png");
		imgLightOff = getImage(getDocumentBase(), "lightOff.png");
		imgTitle = getImage(getDocumentBase(), "Title.png");

	}

	public void start() {

		// Set's the applet's display resolution size.
		this.setSize(1280, 720);
		setBackground(Color.black);

		// Read high score files
		// Add x & y coordinates of lights
		final int intLightSpacing = 75;
		int intBoard_x;

		final int intBoard_y = 250; // 175

		if (intBoardSize % 2 == 1)
			intBoard_x = (getWidth() / 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2) * (int) Math.floor(intBoardSize / 2))
					- intLightRadius;
		else
			intBoard_x = (getWidth() / 2) - ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2)
					* (int) Math.floor(intBoardSize / 2));

		for (int depth = 0; depth < 2; depth++)
			for (int row = 0; row < intBoardSize; row++)
				for (int column = 0; column < intBoardSize; column++)
					if (depth == 1)
						intLightPosition[depth][row][column] = intBoard_y + column * intLightSpacing;
					else
						intLightPosition[depth][row][column] = intBoard_x + row * intLightSpacing;

		boardRandomize();

	}

	public void stop() {

		// Save high score files

	}

	public void destroy() {

	}

	public void paint(Graphics g) {

		final int circleWidth = 4;

		Graphics2D gCircle = (Graphics2D) g;
		gCircle.setStroke((new BasicStroke(circleWidth)));

		// Draw a Rectangle around the applet's display area.
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		/* TESTING CIRCLE BORDER */
//		gCircle.drawOval(intLightPosition[0][0][0] - circleWidth, intLightPosition[1][0][0] - circleWidth, (intLightRadius + circleWidth) * 2, (intLightRadius + circleWidth) * 2);

		/* TESTING MIDDLE LINE */
//		g.setColor(Color.cyan);
//		g.drawLine(getWidth() / 2, 0, getWidth() / 2, 1280);

		if (boolGameWin == false) {

			// Draw Title
			g.drawImage(imgTitle, getWidth() / 2 - 205, 75, this);

			for (int row = 0; row < intBoardSize; row++) {
				for (int column = 0; column < intBoardSize; column++) {
					if (boolLightState[row][column] == false && boolLightHover[row][column] == false)
						g.drawImage(imgLightOff, intLightPosition[0][row][column], intLightPosition[1][row][column],
								this);
					else if (boolLightState[row][column] && boolLightHover[row][column] == false)
						g.drawImage(imgLightOn, intLightPosition[0][row][column], intLightPosition[1][row][column],
								this);
					else if (boolLightState[row][column] && boolLightHover[row][column])
						g.drawImage(imgLightHoverOn, intLightPosition[0][row][column], intLightPosition[1][row][column],
								this);
					else
						g.drawImage(imgLightHoverOff, intLightPosition[0][row][column],
								intLightPosition[1][row][column], this);
					if (boolRequestSolution && boolLightSolution[row][column]) {
						g.setColor(Color.red);
						gCircle.drawOval(intLightPosition[0][row][column] - circleWidth,
								intLightPosition[1][row][column] - circleWidth, (intLightRadius + circleWidth) * 2,
								(intLightRadius + circleWidth) * 2);
					} else {
						g.setColor(Color.black);
						gCircle.drawOval(intLightPosition[0][row][column] - circleWidth,
								intLightPosition[1][row][column] - circleWidth, (intLightRadius + circleWidth) * 2,
								(intLightRadius + circleWidth) * 2);
					}

				}
			}
		} else {

			for (int row = 0; row < intBoardSize; row++)
				for (int column = 0; column < intBoardSize; column++)
					g.drawImage(imgLightOff, intLightPosition[0][row][column], intLightPosition[1][row][column], this);

		}
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

		if (boolGameWin == false) {
			mouseToggleLight(intx, inty);

			boardSolved();
			if (boardSolved())
				boolGameWin = true;

			if (boolRequestSolution)
				solution();

		}

		repaint();

	}

	public void mouseClicked(MouseEvent event) {

	}

	public void mouseMoved(MouseEvent event) {

		int intx = event.getX();
		int inty = event.getY();

		if (boolGameWin == false)
			mouseHoverLight(intx, inty);

	}

	public void mouseDragged(MouseEvent event) {

	}

	public void boardRandomize() {

		do {

			for (int row = 0; row < intBoardSize; row++)
				for (int column = 0; column < intBoardSize; column++)
					boolLightState[row][column] = false;

			int randomRow, randomColumn;

			for (int randomCount = 0; randomCount <= intBoardSize * 10; randomCount++) {
				randomRow = (int) (Math.random() * intBoardSize);
				randomColumn = (int) (Math.random() * intBoardSize);
				toggleAdjacentLights(randomRow, randomColumn);
			}

		} while (boardSolved());

	}

	public void mouseToggleLight(int x, int y) {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if ((Math.sqrt(Math.pow((y - intLightPosition[1][row][column] - intLightRadius), 2)
						+ Math.pow((x - intLightPosition[0][row][column] - intLightRadius), 2))) < intLightRadius)
					toggleAdjacentLights(row, column);

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

	public void mouseHoverLight(int x, int y) {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if ((Math.sqrt(Math.pow((y - intLightPosition[1][row][column] - intLightRadius), 2)
						+ Math.pow((x - intLightPosition[0][row][column] - intLightRadius), 2))) < intLightRadius)
					boolLightHover[row][column] = true;
				else
					boolLightHover[row][column] = false;

		repaint();

	}

	public boolean boardSolved() {

		boolean boolBoardSolved = true;

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[row][column])
					boolBoardSolved = false;

		return boolBoardSolved;

	}

	public void solution() {

		for (int row = 0; row < intBoardSize - 1; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[row][column])
					boolLightSolution[row + 1][column] = true;
				else
					boolLightSolution[row + 1][column] = false;

	}
}