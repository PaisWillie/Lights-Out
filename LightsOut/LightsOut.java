import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.JApplet;

public class LightsOut extends JApplet implements MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	StringBuffer strBuffer;

	int intBoardSize = 5;

	// Initializes 2D array to determine if each light is on.
	boolean[][] boolLightState = new boolean[intBoardSize][intBoardSize];

	// Initializes 2D array to track illuminated state when mouse hovers over light.
	boolean[][] boolLightHover = new boolean[intBoardSize][intBoardSize];

	// Initializes 2D array to track which light needs to be pressed for solution.
	boolean[][] boolLightSolution = new boolean[intBoardSize][intBoardSize];

	// Initializes 3D array for the x & y coordinates for each light.
	int[][][] intLightPosition = new int[2][intBoardSize][intBoardSize];

	// Initializes 1D array for the top 10 scores on the High Score list.
	int[] intHighScoreValue = new int[10];

	// Initializes 1D array for the top 10 names on the High Score list.
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

	boolean boolClearScreen;
	
	boolean boolClearNumber;

	// Constant for the radius size of the light.
	final int intLightRadius = 31;

	int intStartingLights;

	String string = "";

	Font fntSerif, fntSansSerif, fntMonospaced, fntDialog, fntBauHaus;

	Image imgLightOff, imgLightHoverOff, imgLightHoverOn, imgLightOn;
	Image imgCenterLine, imgTitle, imgCredits;
	Image imgBackToMenuOff, imgBackToMenuOn;
	Image imgShowSolutionOff, imgShowSolutionOn;
	Image imgPlayOff, imgPlayOn;
	Image imgOptionsOff, imgOptionsOn;
	Image imgHighScoresOff, imgHighScoresOn;
	Image imgStartingLightsOff, imgStartingLightsOn;
	Image imgBoardSizeOff, imgBoardSizeOn;
	Image imgBoardSkinsOff, imgBoardSkinsOn;
	Image imgBackOff, imgBackOn;
	Image imgIncreaseOff, imgIncreaseOn;
	Image imgDecreaseOff, imgDecreaseOn;
	Image imgRank1, imgRank2, imgRank3, imgRank4, imgRank5, imgRank6, imgRank7, imgRank8, imgRank9, imgRank10;
	Image imgDigit0, imgDigit1, imgDigit2, imgDigit3, imgDigit4, imgDigit5, imgDigit6, imgDigit7, imgDigit8, imgDigit9;

	boolean boolPlayButtonState, boolOptionsButtonState, boolHighScoresButtonState, boolBackButtonState,
			boolHighScoresBackButtonState, boolStartingLightsButtonState, boolIncreaseButtonState,
			boolDecreaseButtonState, boolBackToMenuButtonState;

	Dimension screenSize;

	// Initializes string to keep track of what information should be displayed:
	// Menu, Board, Options, StartingLights, BoardSize, BoardSkins, HighScoreList,
	// HighScoreInput, GameWin.
	String strDisplay;

	public void init() {

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		fntSerif = new Font("Serif", Font.BOLD, 30);
		fntSansSerif = new Font("SansSerif", Font.BOLD, 30);
		fntMonospaced = new Font("Monospaced", Font.BOLD, 30);
		fntDialog = new Font("Dialog", Font.BOLD, 30);
		fntBauHaus = new Font("Bauhaus 93", Font.BOLD, 30);

		imgLightOn = getImage(getDocumentBase(), "Images/Board/lightOn.png");
		imgLightHoverOn = getImage(getDocumentBase(), "Images/Board/lightHoverOn.png");
		imgLightHoverOff = getImage(getDocumentBase(), "Images/Board/lightHoverOff.png");
		imgLightOff = getImage(getDocumentBase(), "Images/Board/lightOff.png");
		imgTitle = getImage(getDocumentBase(), "Images/Menu/Title.png");
		imgCredits = getImage(getDocumentBase(), "Images/Menu/Credits.png");
		imgBackToMenuOff = getImage(getDocumentBase(), "Images/Board/BackToMenuOff.png");
		imgBackToMenuOn = getImage(getDocumentBase(), "Images/Board/BackToMenuOn.png");
		imgShowSolutionOff = getImage(getDocumentBase(), "Images/Board/ShowSolutionOff.png");
		imgShowSolutionOn = getImage(getDocumentBase(), "Images/Board/ShowSolutionOn.png");
		imgCenterLine = getImage(getDocumentBase(), "Images/Menu/CenterLine.png");
		imgPlayOff = getImage(getDocumentBase(), "Images/Menu/PlayOff.png");
		imgPlayOn = getImage(getDocumentBase(), "Images/Menu/PlayOn.png");
		imgOptionsOff = getImage(getDocumentBase(), "Images/Menu/OptionsOff.png");
		imgOptionsOn = getImage(getDocumentBase(), "Images/Menu/OptionsOn.png");
		imgHighScoresOff = getImage(getDocumentBase(), "Images/Menu/HighScoresOff.png");
		imgHighScoresOn = getImage(getDocumentBase(), "Images/Menu/HighScoresOn.png");
		imgStartingLightsOff = getImage(getDocumentBase(), "Images/Menu/StartingLightsOff.png");
		imgStartingLightsOn = getImage(getDocumentBase(), "Images/Menu/StartingLightsOn.png");
		imgBoardSizeOff = getImage(getDocumentBase(), "Images/Menu/BoardSizeOff.png");
		imgBoardSizeOn = getImage(getDocumentBase(), "Images/Menu/BoardSizeOn.png");
		imgBoardSkinsOff = getImage(getDocumentBase(), "Images/Menu/BoardSkinsOff.png");
		imgBoardSkinsOn = getImage(getDocumentBase(), "Images/Menu/BoardSkinsOn.png");
		imgBackOff = getImage(getDocumentBase(), "Images/Menu/BackOff.png");
		imgBackOn = getImage(getDocumentBase(), "Images/Menu/BackOn.png");
		imgRank1 = getImage(getDocumentBase(), "Images/HighScores/List1On.png");
		imgRank2 = getImage(getDocumentBase(), "Images/HighScores/List2On.png");
		imgRank3 = getImage(getDocumentBase(), "Images/HighScores/List3On.png");
		imgRank4 = getImage(getDocumentBase(), "Images/HighScores/List4On.png");
		imgRank5 = getImage(getDocumentBase(), "Images/HighScores/List5On.png");
		imgRank6 = getImage(getDocumentBase(), "Images/HighScores/List6On.png");
		imgRank7 = getImage(getDocumentBase(), "Images/HighScores/List7On.png");
		imgRank8 = getImage(getDocumentBase(), "Images/HighScores/List8On.png");
		imgRank9 = getImage(getDocumentBase(), "Images/HighScores/List9On.png");
		imgRank10 = getImage(getDocumentBase(), "Images/HighScores/List10On.png");
		imgIncreaseOff = getImage(getDocumentBase(), "Images/Menu/IncreaseOff.png");
		imgIncreaseOn = getImage(getDocumentBase(), "Images/Menu/IncreaseOn.png");
		imgDecreaseOff = getImage(getDocumentBase(), "Images/Menu/DecreaseOff.png");
		imgDecreaseOn = getImage(getDocumentBase(), "Images/Menu/DecreaseOn.png");
		imgDigit0 = getImage(getDocumentBase(), "Images/Numbers/Digit0.png");
		imgDigit1 = getImage(getDocumentBase(), "Images/Numbers/Digit1.png");
		imgDigit2 = getImage(getDocumentBase(), "Images/Numbers/Digit2.png");
		imgDigit3 = getImage(getDocumentBase(), "Images/Numbers/Digit3.png");
		imgDigit4 = getImage(getDocumentBase(), "Images/Numbers/Digit4.png");
		imgDigit5 = getImage(getDocumentBase(), "Images/Numbers/Digit5.png");
		imgDigit6 = getImage(getDocumentBase(), "Images/Numbers/Digit6.png");
		imgDigit7 = getImage(getDocumentBase(), "Images/Numbers/Digit7.png");
		imgDigit8 = getImage(getDocumentBase(), "Images/Numbers/Digit8.png");
		imgDigit9 = getImage(getDocumentBase(), "Images/Numbers/Digit9.png");

	}

	public void start() {
		
//		String fonts[] = 
//			      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//
//			    for ( int i = 0; i < fonts.length; i++ )
//			    {
//			      System.out.println(fonts[i]);
//			    }
		
		// Set's the applet's display resolution size.
		this.setSize(1280, 720);
		// this.setSize((int) screenSize.getWidth() / 2, (int) screenSize.getHeight());
		setBackground(Color.black);

		resetButtons();

		intStartingLights = 10;

		boolClearScreen = boolClearNumber = false;

		// Read high score files
		try {

			testHighScore();

		} catch (IOException e) {

			throw new RuntimeException(e);

		}

		for (int column = 0; column < intBoardSize; column++)
			boolLightSolutionTop[column] = false;

		setup();

	}

	public void stop() {

		try {

			writeHighScore();

		} catch (IOException e) {

			throw new RuntimeException(e);

		}

	}

	public void destroy() {

	}

	public void setup() {

		strDisplay = "Menu";

		boolRequestSolution = false;

	}

	public void paint(Graphics g) {
		
		if (boolClearScreen) {

			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());

			boolClearScreen = false;

		}

		// Fixes position of lights if screen resolution has changed by user while
		// running
		lightPosition();

		final int circleWidth = 4;

		Graphics2D gCircle = (Graphics2D) g;
		gCircle.setStroke((new BasicStroke(circleWidth)));

		switch (strDisplay) {

		case "Menu":

			g.drawImage(imgTitle, getWidth() / 2 - 425, getHeight() / 2 - 66, this);
			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgCredits, getWidth() / 2 - 175, getHeight() / 2 + 60, this);

			if (boolPlayButtonState)
				g.drawImage(imgPlayOn, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			else
				g.drawImage(imgPlayOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			if (boolOptionsButtonState)
				g.drawImage(imgOptionsOn, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			else
				g.drawImage(imgOptionsOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			if (boolHighScoresButtonState)
				g.drawImage(imgHighScoresOn, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			else
				g.drawImage(imgHighScoresOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);

			break;

		case "Options":

			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgOptionsOn, getWidth() / 2 - 300, getHeight() / 2 - 50, this);
			if (boolStartingLightsButtonState)
				g.drawImage(imgStartingLightsOn, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			else
				g.drawImage(imgStartingLightsOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			g.drawImage(imgBoardSizeOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			g.drawImage(imgBoardSkinsOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			if (boolBackButtonState)
				g.drawImage(imgBackOn, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			else
				g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 80, this);

			break;

		case "StartingLights":
			
			if (boolClearNumber) {
				
				g.clearRect(getWidth() / 2 + 150, getHeight() / 2 - 50, getWidth () / 2 + 250, getHeight() / 2 - 150);
				boolClearNumber = false;
				
			}

			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgStartingLightsOn, getWidth() / 2 - 500, getHeight() / 2 - 50, this);

			if (boolDecreaseButtonState)
				g.drawImage(imgDecreaseOn, getWidth() / 2 + 50, getHeight() / 2 - 50, this);
			else
				g.drawImage(imgDecreaseOff, getWidth() / 2 + 50, getHeight() / 2 - 50, this);

			if (boolIncreaseButtonState)
				g.drawImage(imgIncreaseOn, getWidth() / 2 + 300, getHeight() / 2 - 50, this);
			else
				g.drawImage(imgIncreaseOff, getWidth() / 2 + 300, getHeight() / 2 - 50, this);

			if (boolBackButtonState)
				g.drawImage(imgBackOn, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			else
				g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 80, this);

			if (intStartingLights >= 20) {
				g.drawImage(imgDigit2, getWidth() / 2 + 150, getHeight() / 2 - 52, this);
				switch (intStartingLights) {

				case 20:

					g.drawImage(imgDigit0, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 21:

					g.drawImage(imgDigit1, getWidth() / 2 + 210, getHeight() / 2 - 52, this);
					break;

				case 22:

					g.drawImage(imgDigit2, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 23:

					g.drawImage(imgDigit3, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 24:

					g.drawImage(imgDigit4, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 25:

					g.drawImage(imgDigit5, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 26:

					g.drawImage(imgDigit6, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 27:

					g.drawImage(imgDigit7, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 28:

					g.drawImage(imgDigit8, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 29:

					g.drawImage(imgDigit9, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				}
			} else if (intStartingLights >= 10) {
				g.drawImage(imgDigit1, getWidth() / 2 + 150, getHeight() / 2 - 52, this);
				switch (intStartingLights) {

				case 10:

					g.drawImage(imgDigit0, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 11:

					g.drawImage(imgDigit1, getWidth() / 2 + 210, getHeight() / 2 - 52, this);
					break;

				case 12:

					g.drawImage(imgDigit2, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 13:

					g.drawImage(imgDigit3, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 14:

					g.drawImage(imgDigit4, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 15:

					g.drawImage(imgDigit5, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 16:

					g.drawImage(imgDigit6, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 17:

					g.drawImage(imgDigit7, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 18:

					g.drawImage(imgDigit8, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				case 19:

					g.drawImage(imgDigit9, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					break;

				}
			} else
				switch (intStartingLights) {

				case 1:

					g.drawImage(imgDigit1, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 2:

					g.drawImage(imgDigit2, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 3:

					g.drawImage(imgDigit3, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 4:

					g.drawImage(imgDigit4, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 5:

					g.drawImage(imgDigit5, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 6:

					g.drawImage(imgDigit6, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 7:

					g.drawImage(imgDigit7, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 8:

					g.drawImage(imgDigit8, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				case 9:

					g.drawImage(imgDigit9, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					break;

				}

			break;

		case "BoardSize":

			break;

		case "HighScores":

			g.drawImage(imgHighScoresOn, getWidth() / 2 - 625, getHeight() / 2 - 50, this);

			if (boolHighScoresBackButtonState)
				g.drawImage(imgBackOn, getWidth() / 2 - 350, getHeight() / 2 + 125, this);
			else
				g.drawImage(imgBackOff, getWidth() / 2 - 350, getHeight() / 2 + 125, this);

			g.drawImage(imgCenterLine, getWidth() / 2 - 225, getHeight() / 2 - 180, this);
			g.drawImage(imgRank1, getWidth() / 2 - 200, getHeight() / 2 - 185, this);
			g.drawImage(imgRank2, getWidth() / 2 - 200, getHeight() / 2 - 110, this);
			g.drawImage(imgRank3, getWidth() / 2 - 200, getHeight() / 2 - 35, this);
			g.drawImage(imgRank4, getWidth() / 2 - 200, getHeight() / 2 + 40, this);
			g.drawImage(imgRank5, getWidth() / 2 - 200, getHeight() / 2 + 115, this);
			g.drawImage(imgCenterLine, getWidth() / 2 + 175, getHeight() / 2 - 180, this);
			g.drawImage(imgRank6, getWidth() / 2 + 200, getHeight() / 2 - 185, this);
			g.drawImage(imgRank7, getWidth() / 2 + 200, getHeight() / 2 - 110, this);
			g.drawImage(imgRank8, getWidth() / 2 + 200, getHeight() / 2 - 35, this);
			g.drawImage(imgRank9, getWidth() / 2 + 200, getHeight() / 2 + 40, this);
			g.drawImage(imgRank10, getWidth() / 2 + 200, getHeight() / 2 + 115, this);

			g.setColor(Color.cyan);
			g.setFont(new Font("Unispace", Font.BOLD, 30));

			for (int rank = 0; rank < 10; rank++)
				if (rank < 5 && intHighScoreValue[rank] > 0) {

					g.drawString(strHighScoreName[rank], getWidth() / 2 - 100, getHeight() / 2 + rank * 70 - 123);
					g.drawString(Integer.toString(intHighScoreValue[rank]), getWidth() / 2 + 100,
							getHeight() / 2 + rank * 70 - 123);

				} else if (intHighScoreValue[rank] > 0) {

					g.drawString(strHighScoreName[rank], getWidth() / 2 + 310, getHeight() / 2 + (rank - 5) * 70 - 123);
					g.drawString(Integer.toString(intHighScoreValue[rank]), getWidth() / 2 + 510,
							getHeight() / 2 + (rank - 5) * 70 - 123);

				}

			break;

		case "Board":

//			if (boolGameWin == false) {

			// Draw Lights Out title
			g.drawImage(imgTitle, getWidth() / 2 - 205, getHeight() / 2 - 325, this);

			// Draw Show Solution button
			if (boolRequestSolution)
				g.drawImage(imgShowSolutionOn, getWidth() / 2 + 250, getHeight() / 2 - 55, this);
			else
				g.drawImage(imgShowSolutionOff, getWidth() / 2 + 250, getHeight() / 2 - 55, this);

			if (boolBackToMenuButtonState)
				g.drawImage(imgBackToMenuOn, getWidth() / 2 - 450, getHeight() / 2 - 55, this);
			else
				g.drawImage(imgBackToMenuOff, getWidth() / 2 - 450, getHeight() / 2 - 55, this);

			// Boolean flag to prevent drawing more than one solution circle
			boolean boolShowSolution = true;

			if (boolRequestSolution)
				displaySolution();

			for (int row = 0; row < intBoardSize; row++) {
				for (int column = 0; column < intBoardSize; column++) {
					if (boolLightState[column][row] == false && boolLightHover[column][row] == false)
						g.drawImage(imgLightOff, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
					else if (boolLightState[column][row] && boolLightHover[column][row] == false)
						g.drawImage(imgLightOn, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
					else if (boolLightState[column][row] && boolLightHover[column][row])
						g.drawImage(imgLightHoverOn, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
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

			// Runs if game is finished
//			} else {
//
//				for (int row = 0; row < intBoardSize; row++)
//					for (int column = 0; column < intBoardSize; column++) {
//
//						g.drawImage(imgLightOff, intLightPosition[0][column][row], intLightPosition[1][column][row],
//								this);
//						g.setColor(Color.black);
//						gCircle.drawOval(intLightPosition[0][column][row] - circleWidth,
//								intLightPosition[1][column][row] - circleWidth, (intLightRadius + circleWidth) * 2,
//								(intLightRadius + circleWidth) * 2);
//					}
//
//			}

			break;

		}

	}

	public void mouseEntered(MouseEvent event) {

	}

	public void mouseExited(MouseEvent event) {

	}

	public void mousePressed(MouseEvent event) {

	}

	public void mouseReleased(MouseEvent event) {

		gameSolution();

		int intx = event.getX();
		int inty = event.getY();

		switch (strDisplay) {

		case "Menu":

			if (playButton(intx, inty)) {

				strDisplay = "Board";
				boardRandomize();

			}

			if (optionsButton(intx, inty))
				strDisplay = "Options";

			if (highScoresButton(intx, inty))
				strDisplay = "HighScores";

			if (playButton(intx, inty) || (optionsButton(intx, inty)) || highScoresButton(intx, inty)) {

				resetButtons();
				boolClearScreen = true;

			}

			break;

		case "Options":

			if (backButton(intx, inty))
				strDisplay = "Menu";

			if (startingLightsButton(intx, inty))
				strDisplay = "StartingLights";

			if (backButton(intx, inty) || startingLightsButton(intx, inty))
				boolClearScreen = true;

			break;

		case "StartingLights":

			if (decreaseButton(intx, inty) && intStartingLights > 1) {
				
				boolClearNumber = true;
				intStartingLights -= 1;
				
			}
			
			if (increaseButton(intx, inty) && intStartingLights < 25) {
				
				boolClearNumber = true;
				intStartingLights += 1;
				
			}
			
			if (backButton(intx, inty)) {
				
				strDisplay = "Options";
				boolClearScreen = true;
				
			}

			break;

		case "HighScores":

			if (backButton(intx, inty))
				strDisplay = "Menu";

			if (highScoresBackButton(intx, inty))
				strDisplay = "Menu";

			if (backButton(intx, inty) || highScoresBackButton(intx, inty))
				boolClearScreen = true;

			break;

		case "Board":

//			if (boolGameWin == false) {
			mouseToggleLight(intx, inty);

			boardSolved();

			if (boardSolved())
				boolGameWin = true;

			if (lightsOnBottom() && lightCase(boolLightState) >= 0 && intBoardSize == 5)
				caseSolution();

			if (showSolutionButton(intx, inty))
				boolRequestSolution = !boolRequestSolution;

			if (backToMenuButton(intx, inty)) {

				strDisplay = "Menu";
				boolRequestSolution = false;
				boolClearScreen = true;

			}

			caseSolutionTop(intx, inty);

//			}

			break;

		}

		repaint();

	}

	public void mouseClicked(MouseEvent event) {

	}

	public void mouseMoved(MouseEvent event) {

		int intx = event.getX();
		int inty = event.getY();

		switch (strDisplay) {

		case "Menu":

			if (playButton(intx, inty))
				boolPlayButtonState = true;
			else
				boolPlayButtonState = false;

			if (optionsButton(intx, inty))
				boolOptionsButtonState = true;
			else
				boolOptionsButtonState = false;

			if (highScoresButton(intx, inty))
				boolHighScoresButtonState = true;
			else
				boolHighScoresButtonState = false;

			break;

		case "Board":

			if (boolGameWin == false)
				mouseHoverLight(intx, inty);

			if (backToMenuButton(intx, inty))
				boolBackToMenuButtonState = true;
			else
				boolBackToMenuButtonState = false;

			break;

		case "Options":

			if (startingLightsButton(intx, inty))
				boolStartingLightsButtonState = true;
			else
				boolStartingLightsButtonState = false;

			if (backButton(intx, inty))
				boolBackButtonState = true;
			else
				boolBackButtonState = false;

			break;

		case "StartingLights":

			if (backButton(intx, inty))
				boolBackButtonState = true;
			else
				boolBackButtonState = false;

			if (increaseButton(intx, inty))
				boolIncreaseButtonState = true;
			else
				boolIncreaseButtonState = false;
			
			if (decreaseButton(intx, inty))
				boolDecreaseButtonState = true;
			else
				boolDecreaseButtonState = false;
			
			break;

		case "HighScores":

			if (highScoresBackButton(intx, inty))
				boolHighScoresBackButtonState = true;
			else
				boolHighScoresBackButtonState = false;

			break;

		}

		repaint();

	}

	public void mouseDragged(MouseEvent event) {

	}

	public void keyPressed(KeyEvent event) {

	}

	public void keyReleased(KeyEvent event) {

	}

	public void keyTyped(KeyEvent event) {

		char c = event.getKeyChar();

		if (c != KeyEvent.CHAR_UNDEFINED) {

			string += String.valueOf(c);
			System.out.println(string);
		}

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
//			AudioStream click = new AudioStream(new FileInputStream("Audio/click.wav"));
//			AudioPlayer.player.start(click);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Public procedural method to randomize the board correctly
	public void boardRandomize() {

		for (int row = 0; row < intBoardSize; row++)
			for (int column = 0; column < intBoardSize; column++)
				boolLightState[column][row] = false;

		// Board randomized 5 times before checking for number of lights on to prevent
		// easily solvable board layouts
		for (int count = 0; count < 5; count++)
			toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));

		do {

			toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));

			// Repeats loop if the number of starting lights are not equal to user's request
			// amount
		} while (lightsOn() != intStartingLights);

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

	public void displaySolution() {

		for (int row = 0; row < intBoardSize - 1; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLightState[column][row]) {
					boolLightSolution[column][row + 1] = true;
				} else
					boolLightSolution[column][row + 1] = false;

	}

	public void gameSolution() {

		boolean[][] boolLights = new boolean[intBoardSize][intBoardSize];

		for (int i = 0; i < intBoardSize; i++)
			boolLights[i] = Arrays.copyOf(boolLightState[i], boolLightState[i].length);

		int toggleRow, toggleColumn;

		for (int row = 0; row < intBoardSize - 1; row++)
			for (int column = 0; column < intBoardSize; column++)
				if (boolLights[column][row]) {

					toggleRow = row + 1;
					toggleColumn = column;

					boolLights[toggleColumn][toggleRow] = !boolLights[toggleColumn][toggleRow];

					if (toggleRow + 1 >= 0 && toggleRow + 1 < intBoardSize)
						boolLights[toggleColumn][toggleRow + 1] = !boolLights[toggleColumn][toggleRow + 1];

					if (toggleRow - 1 >= 0 && toggleRow - 1 < intBoardSize)
						boolLights[toggleColumn][toggleRow - 1] = !boolLights[toggleColumn][toggleRow - 1];

					if (toggleColumn + 1 >= 0 && toggleColumn + 1 < intBoardSize)
						boolLights[toggleColumn + 1][toggleRow] = !boolLights[toggleColumn + 1][toggleRow];

					if (toggleColumn - 1 >= 0 && toggleColumn - 1 < intBoardSize)
						boolLights[toggleColumn - 1][toggleRow] = !boolLights[toggleColumn - 1][toggleRow];

				}

//		System.out.println(lightCase(boolLights));

	}

	public void caseSolution() {

		for (int column = 0; column < intBoardSize; column++)
			boolLightSolution[column][0] = boolLightCaseSolution[lightCase(boolLightState)][column];

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

	public int lightCase(boolean[][] array) {

		boolean flag;

		for (int num = 0; num < 7; num++) {
			flag = true;
			for (int row = 0; row < intBoardSize; row++)
				if (boolLightCase[num][row] != array[row][intBoardSize - 1])
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

		for (int rank = 0; rank < 10; rank++) {

			String[] strHighScore = fileInput.readLine().split(" ", 2);
			strHighScoreName[rank] = strHighScore[0];
			intHighScoreValue[rank] = Integer.parseInt(strHighScore[1]);

		}

		fileInput.close();

	}

	public void resetHighScore() throws IOException {

		for (int rank = 0; rank < 10; rank++) {

			intHighScoreValue[rank] = -1;
			strHighScoreName[rank] = "";

		}

		writeHighScore();

	}

	public void writeHighScore() throws IOException {

		PrintWriter fileOutput = new PrintWriter(new FileWriter("HighScore.txt"));

		for (int rank = 0; rank < 10; rank++)
			fileOutput.println(strHighScoreName[rank] + " " + intHighScoreValue[rank]);

		fileOutput.close();

	}

	public void addHighScore(String name, int score) {

		for (int rank = 0; rank < 10; rank++)
			if (score < intHighScoreValue[rank]) {
				for (int moveRank = 9; moveRank > rank; moveRank--) {

					strHighScoreName[moveRank] = strHighScoreName[moveRank - 1];
					intHighScoreValue[moveRank] = intHighScoreValue[moveRank - 1];

				}

				strHighScoreName[rank] = name;
				intHighScoreValue[rank] = score;

			}

	}

	public boolean playButton(int x, int y) {

		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 190 && y >= getHeight() / 2 - 140
				&& y <= getHeight() / 2 - 60)
			return true;

		return false;
	}

	public boolean optionsButton(int x, int y) {

		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 295 && y >= getHeight() / 2 - 40
				&& y <= getHeight() / 2 + 40)
			return true;

		return false;

	}

	public boolean highScoresButton(int x, int y) {

		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 405 && y >= getHeight() / 2 + 60
				&& y <= getHeight() / 2 + 140)
			return true;

		return false;

	}

	public boolean startingLightsButton(int x, int y) {

		if (x >= getWidth() / 2 + 40 && x <= getWidth() / 2 + 500 && y >= getHeight() / 2 - 135
				&& y <= getHeight() / 2 - 60)
			return true;

		return false;

	}

	public boolean boardSizeButton(int x, int y) {

		/* Unfinished */

		if (true)
			return true;

		return false;

	}

	public boolean boardSkinsButton(int x, int y) {

		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 190 && y >= getHeight() / 2 - 140
				&& y <= getHeight() / 2 - 60)
			return true;

		return false;

	}

	public boolean backButton(int x, int y) {

		if (x >= getWidth() / 2 - 115 & x <= getWidth() / 2 - 30 && y >= getHeight() / 2 + 90
				&& y <= getHeight() / 2 + 120)
			return true;

		return false;

	}

	public boolean highScoresBackButton(int x, int y) {

		if (x >= getWidth() / 2 - 340 && x <= getWidth() / 2 - 255 && y >= getHeight() / 2 + 135
				&& y <= getHeight() / 2 + 165)
			return true;

		return false;

	}

	// g.drawRect(getWidth() / 2 + 320, getHeight() / 2 - 30, 30, 45); Increased
	// g.drawRect(getWidth() / 2 + 70, getHeight() / 2 - 30, 30, 45); Decreased

	public boolean decreaseButton(int x, int y) {

		if (x >= getWidth() / 2 + 70 && x <= getWidth() / 2 + 100 && y >= getHeight() / 2 - 30
				&& y <= getHeight() / 2 + 15)
			return true;

		return false;

	}

	public boolean increaseButton(int x, int y) {

		if (x >= getWidth() / 2 + 320 && x <= getWidth() / 2 + 350 && y >= getHeight() / 2 - 30
				&& y <= getHeight() / 2 + 15)
			return true;

		return false;

	}

	public boolean showSolutionButton(int x, int y) {

		if (x >= getWidth() / 2 + 260 && x <= getWidth() / 2 + 435 && y >= getHeight() / 2 - 45
				&& y <= getHeight() / 2 + 55)
			return true;

		return false;

	}

	public boolean backToMenuButton(int x, int y) {

		if (x >= getWidth() / 2 - 440 && x <= getWidth() / 2 - 260 && y >= getHeight() / 2 - 45
				&& y <= getHeight() / 2 + 55)
			return true;

		return false;

	}

	public void resetButtons() {

		boolIncreaseButtonState = boolStartingLightsButtonState = boolPlayButtonState = boolOptionsButtonState = boolHighScoresButtonState = boolBackButtonState = boolHighScoresBackButtonState = boolBackToMenuButtonState = false;

	}

	public void drawDigit(int digit, int x, int y, Graphics g) {

		switch (digit) {

		case 0:

			g.drawImage(imgDigit0, x, y, this);

			break;

		case 1:

			break;

		case 2:

			break;

		case 3:

			break;

		case 4:

			break;

		case 5:

			break;

		case 6:

			break;

		case 7:

			break;

		case 8:

			break;

		case 9:

			break;

		}

	}

}