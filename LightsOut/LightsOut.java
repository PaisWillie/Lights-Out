import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JApplet;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

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

	// Initializes 3D array for the x & y coordinates for each light.
	int[][][] intLightPosition = new int[2][intBoardSize][intBoardSize];

	// Initializes 1D array for the top 10 scores on the High Score list
	int[] intHighScoreValue = new int[10];
	
	// Initalizes 1D array for the top 10 names on the High Score list
	String[] strHighScoreName = new String[10];

	// Initializes 2D array for different light state cases for game solution.
	final boolean[][] boolLightCase = { { true, false, false, false, true }, { false, true, false, true, false },
			{ true, true, true, false, false }, { false, false, true, true, true }, { true, false, true, true, false },
			{ false, true, true, false, true }, { true, true, false, true, true } };

	final boolean[][] boolLightCaseSolution = { { true, true, false, false, false },
			{ true, false, false, true, false }, { false, true, false, false, false },
			{ false, false, false, true, false }, { false, false, false, false, true },
			{ true, false, false, false, false }, { false, false, true, false, false } };

	boolean[] boolLightSolutionTop = new boolean[intBoardSize];

	boolean boolRequestSolution = true;

	boolean boolGameWin = false;

	final int intLightRadius = 31;

	int intLightsOn = 5;

	Font fntSerif, fntSansSerif, fntMonospaced, fntDialog;

	Image imgLightOff, imgLightHoverOn, imgLightHoverOff, imgLightOn, imgTitle, imgMenu, imgShowSolution;

	Dimension screenSize;

	// Initializes string to keep track of what information should be displayed
	// Options:
	// - Board
	// - Menu
	// - Options
	// - Highscore list
	// - Highscore input
	// - Restart game
	// - Game win screen
	String strDisplay = "Board";

	public void init() {

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		addMouseListener(this);
		addMouseMotionListener(this);

		fntSerif = new Font("Serif", Font.BOLD, 14);
		fntSansSerif = new Font("SansSerif", Font.BOLD, 14);
		fntMonospaced = new Font("Monospaced", Font.BOLD, 14);
		fntDialog = new Font("Dialog", Font.BOLD, 14);

		imgLightOn = getImage(getDocumentBase(), "Images/lightOn.png");
		imgLightHoverOn = getImage(getDocumentBase(), "Images/lightHoverOn.png");
		imgLightHoverOff = getImage(getDocumentBase(), "Images/lightHoverOff.png");
		imgLightOff = getImage(getDocumentBase(), "Images/lightOff.png");
		imgTitle = getImage(getDocumentBase(), "Images/Title.png");
		imgMenu = getImage(getDocumentBase(), "Images/Menu.png");
		imgShowSolution = getImage(getDocumentBase(), "Images/ShowSolution.png");

	}

	public void start() {

		// Set's the applet's display resolution size.
		this.setSize(1280, 720);
//		this.setSize((int) screenSize.getWidth() / 2, (int) screenSize.getHeight());
		setBackground(Color.black);

		// Read high score files
		try {
			readHighScore();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (int column = 0; column < intBoardSize; column++)
			boolLightSolutionTop[column] = false;

		try {
			testHighScore();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		boardRandomize();

	}

	public void stop() {

		// Save high score files

	}

	public void destroy() {

	}

	public void paint(Graphics g) {

		// Fixes position of lights if screen resolution has changed by user while running
		lightPosition();
		
		final int circleWidth = 4;

		Graphics2D gCircle = (Graphics2D) g;
		gCircle.setStroke((new BasicStroke(circleWidth)));

		// Draw a Rectangle around the applet's display area.
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		/* TESTING CIRCLE BORDER */
//		g.setColor(Color.red);
//		gCircle.drawOval(intLightPosition[0][0][0] - circleWidth, intLightPosition[1][0][0] - circleWidth, (intLightRadius + circleWidth) * 2, (intLightRadius + circleWidth) * 2);

		/* TESTING MIDDLE LINE */
//		g.setColor(Color.cyan);
//		g.drawLine(getWidth() / 2, 0, getWidth() / 2, 1280);

		if (strDisplay == "Board")
			if (boolGameWin == false) {

				// Draw Lights Out title
				g.drawImage(imgTitle, getWidth() / 2 - 205, getHeight() / 2 - 325, this);

				// Draw Back To Menu button
				g.drawImage(imgMenu, getWidth() / 2 - 400, getHeight() / 2 - 55, this);

				// Draw Show Solution button
				g.drawImage(imgShowSolution, getWidth() / 2 + 200, getHeight() / 2 - 55, this);

				// Boolean flag to prevent drawing more than one solution circle
				boolean boolShowSolution = true;

				if (boolRequestSolution)
					solution();

				for (int row = 0; row < intBoardSize; row++) {
					for (int column = 0; column < intBoardSize; column++) {
						if (boolLightState[column][row] == false && boolLightHover[column][row] == false)
							g.drawImage(imgLightOff, intLightPosition[0][column][row], intLightPosition[1][column][row],
									this);
						else if (boolLightState[column][row] && boolLightHover[column][row] == false)
							g.drawImage(imgLightOn, intLightPosition[0][column][row], intLightPosition[1][column][row],
									this);
						else if (boolLightState[column][row] && boolLightHover[column][row])
							g.drawImage(imgLightHoverOn, intLightPosition[0][column][row],
									intLightPosition[1][column][row], this);
						else
							g.drawImage(imgLightHoverOff, intLightPosition[0][column][row],
									intLightPosition[1][column][row], this);

						// Tests if the user requests solution, if the value of the array
						if (boolRequestSolution && boolLightSolution[column][row] & boolShowSolution) {

							// Sets boolean value to false to prevent other solution circles from drawing
							boolShowSolution = false;
							g.setColor(Color.red);
							gCircle.drawOval(intLightPosition[0][column][row] - circleWidth,
									intLightPosition[1][column][row] - circleWidth, (intLightRadius + circleWidth) * 2,
									(intLightRadius + circleWidth) * 2);

							// Performs if light does not have a solution request
						} else {

							// Sets graphics color to black
							g.setColor(Color.black);
							// Draws a black circle over the existing red circles
							gCircle.drawOval(intLightPosition[0][column][row] - circleWidth,
									intLightPosition[1][column][row] - circleWidth, (intLightRadius + circleWidth) * 2,
									(intLightRadius + circleWidth) * 2);
						}
					}
				}

				// Runs if game is finished and display is set to board
			} else {

				for (int row = 0; row < intBoardSize; row++)
					for (int column = 0; column < intBoardSize; column++) {
						g.drawImage(imgLightOff, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
						g.setColor(Color.black);
						gCircle.drawOval(intLightPosition[0][column][row] - circleWidth,
								intLightPosition[1][column][row] - circleWidth, (intLightRadius + circleWidth) * 2,
								(intLightRadius + circleWidth) * 2);
					}

			}

		else if (strDisplay == "Menu") {

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

		if (strDisplay.equals("Menu"))
//			if (menuStart())
				System.out.println("Starting game");
		else if (strDisplay.equals("Board"))
			if (boolGameWin == false) {
				mouseToggleLight(intx, inty);

				boardSolved();
				
				if (boardSolved())
					boolGameWin = true;

				if (lightsOnBottom() && lightCase() >= 0)
					caseSolution();

				caseSolutionTop(intx, inty);

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

	public void lightPosition() {
		// Add x & y coordinates of lights
		final int intLightSpacing = 75;
		int intBoard_x;

//		final int intBoard_y = 175; // 175
		int intBoard_y;

		if (intBoardSize % 2 == 1) {
			intBoard_x = (getWidth() / 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2) * (int) Math.floor(intBoardSize / 2))
					- intLightRadius;
			intBoard_y = (getHeight() / 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2) * (int) Math.floor(intBoardSize / 2))
					- intLightRadius;
		} else {
			intBoard_x = (getWidth() / 2) - ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2)
					* (int) Math.floor(intBoardSize / 2));
			intBoard_y = (getHeight() / 2) - ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2)
					* (int) Math.floor(intBoardSize / 2));
		}

		for (int depth = 0; depth < 2; depth++)
			for (int row = 0; row < intBoardSize; row++)
				for (int column = 0; column < intBoardSize; column++)
					if (depth == 1)
						intLightPosition[depth][column][row] = intBoard_y + row * intLightSpacing;
					else
						intLightPosition[depth][column][row] = intBoard_x + column * intLightSpacing;
	}
	
	public void soundClick() {
		try {
			AudioStream click = new AudioStream(new FileInputStream("click.wav"));
			AudioPlayer.player.start(click);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void boardRandomize() {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				boolLightState[column][row] = false;

		int randomRow, randomColumn;

		do {

			randomRow = (int) (Math.random() * intBoardSize);
			randomColumn = (int) (Math.random() * intBoardSize);
			toggleAdjacentLights(randomRow, randomColumn);

		} while (lightsOn() != intLightsOn);

		repaint();

	}

	public int lightsOn() {

		int intLightsOn = 0;

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[column][row] == true)
					intLightsOn++;

		return intLightsOn;

	}

	public void mouseToggleLight(int x, int y) {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (insideLight(x, y, row, column)) {
					toggleAdjacentLights(row, column);
					soundClick();
				}
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

		boolLightState[column][row] = !boolLightState[column][row];

	}

	public void mouseHoverLight(int x, int y) {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (insideLight(x, y, row, column))
					boolLightHover[column][row] = true;
				else
					boolLightHover[column][row] = false;

		repaint();

	}

	public boolean boardSolved() {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[column][row])
					return false;

		return true;

	}

	public void solution() {

		for (int row = 0; row < intBoardSize - 1; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[column][row]) {
					boolLightSolution[column][row + 1] = true;
				} else
					boolLightSolution[column][row + 1] = false;

	}

	public void caseSolution() {

		for (int column = 0; column < intBoardSize; column++)
			boolLightSolution[column][0] = boolLightCaseSolution[lightCase()][column];

	}

	public void caseSolutionTop(int x, int y) {

		for (int column = 0; column < intBoardSize; column++)
			if (insideLight(x, y, 0, column) & boolLightSolution[column][0])
				boolLightSolution[column][0] = false;
			else if (insideLight(x, y, 0, column))
				boolLightSolution[column][0] = true;

	}

	public boolean lightsOnBottom() {

		for (int row = 0; row < intBoardSize - 1; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[column][row])
					return false;

		return true;

	}

	public int lightCase() {

		boolean flag;

		for (int num = 0; num < 7; num++) {
			flag = true;
			for (int row = 0; row < intBoardSize; row++)
				if (boolLightCase[num][row] != boolLightState[row][intBoardSize - 1])
					flag = false;
			if (flag == true)
				return num;
		}

		return -1;

	}

	public boolean insideLight(int x, int y, int row, int column) {

		if ((Math.sqrt(Math.pow((y - intLightPosition[1][column][row] - intLightRadius), 2)
				+ Math.pow((x - intLightPosition[0][column][row] - intLightRadius), 2))) < intLightRadius)
			return true;
		else
			return false;
	}

	// Checks to see if there is a High-Score list file
	public void testHighScore() throws IOException {

		File highScore = new File("HighScore.txt");

		// Tests if HighScore.txt exists
		if (highScore.exists())
			// Read existing High-Score list
			readHighScore();
		// Runs if HighScore.txt does not exist
		else
			// Create new High-Score list
			resetHighScore();

	}

	public void readHighScore() throws IOException {

		BufferedReader fileInput = new BufferedReader(new FileReader("HighScore.txt"));

		for (int rank = 0; rank < 10; rank++)
			intHighScoreValue[rank] = Integer.parseInt(fileInput.readLine());

		fileInput.close();

	}

	public void resetHighScore() throws IOException {

		for (int rank = 0; rank < 10; rank++)
			intHighScoreValue[rank] = -1;

		writeHighScore();

	}

	public void writeHighScore() throws IOException {

		PrintWriter fileOutput = new PrintWriter(new FileWriter("HighScore.txt"));

		for (int rank = 0; rank < 10; rank++)
			fileOutput.println(intHighScoreValue[rank]);

		fileOutput.close();

	}

	public void addHighScore() throws IOException {

	}

}

//	System.out.println(testHighScore());
//	if (testHighScore() == false)
//		resetHighScore();