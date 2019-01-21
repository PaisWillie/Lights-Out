import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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

	// Initializes 2D array to determine if each light is on.
	boolean[][] boolLightState;

	// Initalizes
	boolean[][] boolOriginalBoard;

	// Initializes 2D array to track illuminated state when mouse hovers over light.
	boolean[][] boolLightHover;

	// Initializes 2D array to track which light needs to be pressed for solution.
	boolean[][] boolLightSolution;

	// Initializes 3D array for the x & y coordinates for each light.
	int[][][] intLightPosition;

	// Initializes 1D array for the top 10 scores on the High Score list.
	int[] intHighScoreValue = new int[10];
	
	boolean[] boolLightSolutionTop;

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


	boolean boolRequestSolution;

	boolean boolUsedSolution;

	boolean boolClearScreen;

	boolean boolClearText;

	boolean boolNameInput;

	// Constant for the radius size of the light.
	final int intLightRadius = 31;
	
	int intBoardSize;
	
	int intStartingLights;

	int intLightsToggled;

	// Initializes string to keep track of what information should be displayed:
	// Menu, Board, Options, StartingLights, BoardSize, BoardSkins, HighScoreList,
	// HighScoreInput, GameWin.
	String strDisplay;

	String strRequirementError;

	String strName;

	Image imgLightOff, imgLightHoverOff, imgLightHoverOn, imgLightOn, imgLightWinOff;
	Image imgCenterLine, imgHorizontalLine, imgTitle, imgCredits, imgMoves, imgWin, imgSpaceContinue, imgBoardSizeWarning, imgStartingLightsWarning;
	Image imgBackToMenuOff, imgBackToMenuOn;
	Image imgShowSolutionOff, imgShowSolutionOn;
	Image imgPlayOff, imgPlayOn;
	Image imgOptionsOff, imgOptionsOn;
	Image imgHighScoresOff, imgHighScoresOn;
	Image imgNewHighScore, imgNoNewHighScore, imgRequirementScore, imgRequirementStartingLights, imgRequirementSolution,
			imgTypeName, imgEnterContinue, imgRequirementBoardSize;
	Image imgStartingLightsOff, imgStartingLightsOn;
	Image imgBoardSizeOff, imgBoardSizeOn;
	Image imgBoardSkinsOff, imgBoardSkinsOn;
	Image imgBackOff, imgBackOn;
	Image imgIncreaseOff, imgIncreaseOn;
	Image imgDecreaseOff, imgDecreaseOn;
	Image imgRank1, imgRank2, imgRank3, imgRank4, imgRank5, imgRank6, imgRank7, imgRank8, imgRank9, imgRank10;
	Image imgDigit0, imgDigit1, imgDigit2, imgDigit3, imgDigit4, imgDigit5, imgDigit6, imgDigit7, imgDigit8, imgDigit9;

	boolean boolPlayButtonState, boolOptionsButtonState, boolHighScoresButtonState, boolBackButtonState,
			boolHighScoresBackButtonState, boolStartingLightsButtonState, boolBoardSizeButtonState,
			boolIncreaseButtonState, boolDecreaseButtonState, boolBackToMenuButtonState;

	public void init() {

		setFocusable(true);

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		imgLightOn = getImage(getDocumentBase(), "Images/Board/lightOn.png");
		imgLightHoverOn = getImage(getDocumentBase(), "Images/Board/lightHoverOn.png");
		imgLightHoverOff = getImage(getDocumentBase(), "Images/Board/lightHoverOff.png");
		imgLightOff = getImage(getDocumentBase(), "Images/Board/lightOff.png");
		imgLightWinOff = getImage(getDocumentBase(), "Images/Board/lightWinOff.png");
		imgMoves = getImage(getDocumentBase(), "Images/Board/Moves.png");
		imgTitle = getImage(getDocumentBase(), "Images/Menu/Title.png");
		imgCredits = getImage(getDocumentBase(), "Images/Menu/Credits.png");
		imgBackToMenuOff = getImage(getDocumentBase(), "Images/Board/BackToMenuOff.png");
		imgBackToMenuOn = getImage(getDocumentBase(), "Images/Board/BackToMenuOn.png");
		imgShowSolutionOff = getImage(getDocumentBase(), "Images/Board/ShowSolutionOff.png");
		imgShowSolutionOn = getImage(getDocumentBase(), "Images/Board/ShowSolutionOn.png");
		imgWin = getImage(getDocumentBase(), "Images/Board/Win.png");
		imgSpaceContinue = getImage(getDocumentBase(), "Images/Board/Continue.png");
		imgEnterContinue = getImage(getDocumentBase(), "Images/HighScores/Continue.png");
		imgCenterLine = getImage(getDocumentBase(), "Images/Menu/CenterLine.png");
		imgHorizontalLine = getImage(getDocumentBase(), "Images/HighScores/HorizontalLine.png");
		imgPlayOff = getImage(getDocumentBase(), "Images/Menu/PlayOff.png");
		imgPlayOn = getImage(getDocumentBase(), "Images/Menu/PlayOn.png");
		imgOptionsOff = getImage(getDocumentBase(), "Images/Menu/OptionsOff.png");
		imgOptionsOn = getImage(getDocumentBase(), "Images/Menu/OptionsOn.png");
		imgHighScoresOff = getImage(getDocumentBase(), "Images/Menu/HighScoresOff.png");
		imgHighScoresOn = getImage(getDocumentBase(), "Images/Menu/HighScoresOn.png");
		imgNewHighScore = getImage(getDocumentBase(), "Images/HighScores/NewHighScore.png");
		imgNoNewHighScore = getImage(getDocumentBase(), "Images/HighScores/NoNewHighScore.png");
		imgRequirementScore = getImage(getDocumentBase(), "Images/HighScores/RequirementScore.png");
		imgRequirementStartingLights = getImage(getDocumentBase(), "Images/HighScores/RequirementStartingLights.png");
		imgRequirementSolution = getImage(getDocumentBase(), "Images/HighScores/RequirementSolution.png");
		imgRequirementBoardSize = getImage(getDocumentBase(), "Images/HighScores/RequirementBoardSize.png");
		imgTypeName = getImage(getDocumentBase(), "Images/HighScores/TypeName.png");
		imgStartingLightsOff = getImage(getDocumentBase(), "Images/Menu/StartingLightsOff.png");
		imgStartingLightsOn = getImage(getDocumentBase(), "Images/Menu/StartingLightsOn.png");
		imgStartingLightsWarning = getImage(getDocumentBase(), "Images/Menu/StartingLightsWarning.png");
		imgBoardSizeOff = getImage(getDocumentBase(), "Images/Menu/BoardSizeOff.png");
		imgBoardSizeOn = getImage(getDocumentBase(), "Images/Menu/BoardSizeOn.png");
		imgBoardSizeWarning = getImage(getDocumentBase(), "Images/Menu/BoardSizeWarning.png");
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
		// Set's the applet's display resolution size.
		this.setSize(1280, 720);
		setBackground(Color.black);

		intBoardSize = 5;
		
		arraySetup();
		
		resetButtons();

		strDisplay = "Menu";
		
		intStartingLights = 15;

		boolClearScreen = boolClearText = false;

		boolNameInput = false;

		// Read high score files
		try {
			createHighScore();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (int column = 0; column < intBoardSize; column++)
			boolLightSolutionTop[column] = false;
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

	public void boardSetup() {
		intLightsToggled = 0;

		strName = "";

		strRequirementError = "";

		boolRequestSolution = false;

		boolUsedSolution = false;

		boardRandomize();

		gameSolution();

		boolOriginalBoard = boolLightState.clone();
	}
	
	public void arraySetup() {
		
		boolLightState = new boolean[intBoardSize][intBoardSize];

		boolOriginalBoard = new boolean[intBoardSize][intBoardSize];

		// Initializes 2D array to track illuminated state when mouse hovers over light.
		boolLightHover = new boolean[intBoardSize][intBoardSize];

		// Initializes 2D array to track which light needs to be pressed for solution.
		boolLightSolution = new boolean[intBoardSize][intBoardSize];

		// Initializes 3D array for the x & y coordinates for each light.
		intLightPosition = new int[2][intBoardSize][intBoardSize];
		
		boolLightSolutionTop = new boolean[intBoardSize];
		
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

			if (boolBoardSizeButtonState)
				g.drawImage(imgBoardSizeOn, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			else
				g.drawImage(imgBoardSizeOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);

			g.drawImage(imgBoardSkinsOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			if (boolBackButtonState)
				g.drawImage(imgBackOn, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			else
				g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 80, this);

			break;

		case "StartingLights":
			if (boolClearText) {
				g.clearRect(getWidth() / 2 + 150, getHeight() / 2 - 50, getWidth() / 2 + 250, getHeight() / 2 - 150);
				boolClearText = false;
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

			if (intStartingLights != 15)
				g.drawImage(imgStartingLightsWarning, getWidth() / 2 - 324, getHeight() / 2 + 200, this);
			else {
				g.setColor(Color.black);
				g.fillRect(getWidth() / 2 - 324, getHeight() / 2 + 200, 648, 130);
			}
			
			if (intStartingLights >= 20) {
				g.drawImage(imgDigit2, getWidth() / 2 + 140, getHeight() / 2 - 52, this);

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
			if (boolClearText) {
				g.clearRect(getWidth() / 2 + 150, getHeight() / 2 - 50, getWidth() / 2 + 250, getHeight() / 2 - 150);
				boolClearText = false;
			}

			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgStartingLightsOn, getWidth() / 2 - 500, getHeight() / 2 - 50, this);

			if (intBoardSize != 5)
				g.drawImage(imgBoardSizeWarning, getWidth() / 2 - 315, getHeight() / 2 + 200, this);
			else {
				g.setColor(Color.black);
				g.fillRect(getWidth() / 2 - 315, getHeight() / 2 + 200, 631, 130);
			}
			
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
			
			switch (intBoardSize) {
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
			}
			
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
			if (intBoardSize > 5)
				g.drawImage(imgTitle, getWidth() / 2 - 205, getHeight() / 2 - 385, this);
			else
				// Draw Lights Out title
				g.drawImage(imgTitle, getWidth() / 2 - 205, getHeight() / 2 - 325, this);

			// Draw Show Solution button
			if (boolRequestSolution && intBoardSize == 5)
				g.drawImage(imgShowSolutionOn, getWidth() / 2 + 250, getHeight() / 2 - 55, this);
			else if (intBoardSize == 5)
				g.drawImage(imgShowSolutionOff, getWidth() / 2 + 250, getHeight() / 2 - 55, this);

			if (boolBackToMenuButtonState)
				g.drawImage(imgBackToMenuOn, getWidth() / 2 - 460, getHeight() / 2 - 55, this);
			else
				g.drawImage(imgBackToMenuOff, getWidth() / 2 - 460, getHeight() / 2 - 55, this);

			if (intBoardSize > 5)
				g.drawImage(imgMoves, getWidth() / 2 - 125, getHeight() / 2 + 265, this);
			else
			g.drawImage(imgMoves, getWidth() / 2 - 125, getHeight() / 2 + 225, this);

			if (intLightsToggled == 0) {
				g.setColor(Color.cyan);
				g.setFont(new Font("Unispace", Font.BOLD, 30));
				if (intBoardSize > 5)
					g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 300);
				else
				g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 260);
			} else if (boolClearText) {
				boolClearText = false;
				g.setColor(Color.black);
				if (intBoardSize > 5)
					g.fillRect(getWidth() / 2 + 50, getHeight() / 2 + 275, 100, 25);
				else
					g.fillRect(getWidth() / 2 + 50, getHeight() / 2 + 235, 100, 25);
				g.setColor(Color.cyan);
				g.setFont(new Font("Unispace", Font.BOLD, 30));
				if (intBoardSize > 5)
					g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 300);
				else
				g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 260);
			}

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

			break;

		case "Game Win":
			g.drawImage(imgWin, getWidth() / 2 - 165, getHeight() / 2 - 150, this);
			g.drawImage(imgSpaceContinue, getWidth() / 2 - 210, getHeight() / 2 + 50, this);
			g.drawImage(imgMoves, getWidth() / 2 - 125, getHeight() / 2 - 25, this);
			g.setColor(Color.cyan);
			g.setFont(new Font("Unispace", Font.BOLD, 30));
			g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 10);

			break;

		case "New HighScore":
			g.drawImage(imgHorizontalLine, getWidth() / 2 - 360, getHeight() / 2 - 12, this);
			g.drawImage(imgNewHighScore, getWidth() / 2 - 276, getHeight() / 2 - 125, this);
			g.drawImage(imgTypeName, getWidth() / 2 - 282, getHeight() / 2 + 25, this);

			if (boolClearText) {
				boolClearText = false;
				g.setColor(Color.black);
				g.fillRect(getWidth() / 2 - 75, getHeight() / 2 + 125, 150, 50);
			}

			g.setColor(Color.cyan);
			g.setFont(new Font("Unispace", Font.BOLD, 30));
			g.drawString(strName, getWidth() / 2 - 75, getHeight() / 2 + 150);
			break;

		case "No HighScore":
			g.drawImage(imgHorizontalLine, getWidth() / 2 - 360, getHeight() / 2 - 12, this);
			g.drawImage(imgNoNewHighScore, getWidth() / 2 - 313, getHeight() / 2 - 125, this);
			g.drawImage(imgEnterContinue, getWidth() / 2 - 202, getHeight() / 2 + 125, this);

			switch (strRequirementError) {
			case "Score":
				g.drawImage(imgRequirementScore, getWidth() / 2 - 288, getHeight() / 2 + 25, this);
				break;
			case "Solution":
				g.drawImage(imgRequirementSolution, getWidth() / 2 - 350, getHeight() / 2 + 25, this);
				break;
			case "StartingLights":
				g.drawImage(imgRequirementStartingLights, getWidth() / 2 - 350, getHeight() / 2 + 25, this);
			case "BoardSize":
				g.drawImage(imgRequirementBoardSize, getWidth() / 2 - 222, getHeight() / 2 + 25, this);
				break;
			}

			break;
		}

	}

	public void actionPerformed(ActionEvent action) {

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

		switch (strDisplay) {

		case "Menu":
			if (playButton(intx, inty)) {
				strDisplay = "Board";
				boardSetup();
				if (intBoardSize != 5)
					strRequirementError = "BoardSize";
				else if (intStartingLights != 15)
					strRequirementError = "StartingLights";
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

			if (startingLightsButton(intx, inty) && intBoardSize == 5)
				strDisplay = "StartingLights";

			if (boardSizeButton(intx, inty))
				strDisplay = "BoardSize";
			
			if (backButton(intx, inty) || startingLightsButton(intx, inty) || boardSizeButton(intx, inty)) {
				resetButtons();
				boolClearScreen = true;
			}

			break;

		case "StartingLights":
			if (decreaseButton(intx, inty) && intStartingLights > 1) {
				boolClearText = true;
				intStartingLights -= 1;
			}

			if (increaseButton(intx, inty) && intStartingLights < 25) {
				boolClearText = true;
				intStartingLights += 1;
			}

			if (backButton(intx, inty)) {
				strDisplay = "Options";
				boolClearScreen = true;
				resetButtons();
			}

			break;

		case "BoardSize":
			if (decreaseButton(intx, inty) && intBoardSize > 2) {
				boolClearText = true;
				intBoardSize--;
				arraySetup();
			}

			if (increaseButton(intx, inty) && intBoardSize < 7) {
				boolClearText = true;
				intBoardSize++;
				arraySetup();
			}

			if (backButton(intx, inty)) {
				strDisplay = "Options";
				boolClearScreen = true;
				resetButtons();
			}
			break;

		case "HighScores":
			if (backButton(intx, inty))
				strDisplay = "Menu";

			if (highScoresBackButton(intx, inty))
				strDisplay = "Menu";

			if (highScoresBackButton(intx, inty)) {
				boolClearScreen = true;
				resetButtons();
			}

			break;

		case "Board":
			mouseToggleLight(intx, inty);

			if (boardSolved()) {
				strDisplay = "Game Win";
				boolClearScreen = true;
			}

			if (showSolutionButton(intx, inty) && intBoardSize == 5) {
				boolRequestSolution = !boolRequestSolution;
				strRequirementError = "Solution";
			}

			if (backToMenuButton(intx, inty)) {
				strDisplay = "Menu";
				boolRequestSolution = false;
				boolClearScreen = true;
			}

			caseSolutionTop(intx, inty);

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
			mouseHoverLight(intx, inty);

			if (backToMenuButton(intx, inty))
				boolBackToMenuButtonState = true;
			else
				boolBackToMenuButtonState = false;

			break;

		case "Options":
			if (startingLightsButton(intx, inty) && intBoardSize == 5)
				boolStartingLightsButtonState = true;
			else
				boolStartingLightsButtonState = false;

			if (boardSizeButton(intx, inty))
				boolBoardSizeButtonState = true;
			else
				boolBoardSizeButtonState = false;

			if (backButton(intx, inty))
				boolBackButtonState = true;
			else
				boolBackButtonState = false;

			break;

		case "StartingLights":
		case "BoardSize":
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
		char key = event.getKeyChar();
		switch (strDisplay) {
		case "Game Win":
			if (key == KeyEvent.VK_SPACE) {
				try {
					if (testHighScore(intLightsToggled) && strRequirementError.equals("")) {
						boolNameInput = true;
						strDisplay = "New HighScore";
						boolClearScreen = true;
					} else {
						if (!testHighScore(intLightsToggled))
							strRequirementError = "Score";
						strDisplay = "No HighScore";
						boolClearScreen = true;
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				break;
			}
		case "New HighScore":
			if (key == KeyEvent.VK_ENTER) {
				boolNameInput = false;
				addHighScore(strName, intLightsToggled);
				strDisplay = "HighScores";
				boolClearScreen = true;
			} else if (key == KeyEvent.VK_BACK_SPACE && strName.length() > 0) {
				System.out.println("BackSpace");
				strName = strName.substring(0, strName.length() - 1);
				boolClearText = true;
			} else if (key != KeyEvent.CHAR_UNDEFINED && key != KeyEvent.VK_SPACE && key != KeyEvent.VK_BACK_SPACE
					&& strName.length() < 8)
				strName += key;
			break;
		case "No HighScore":
			if (key == KeyEvent.VK_ENTER) {
				strDisplay = "Menu";
				boolClearScreen = true;
			}
		}
		repaint();
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
			intBoard_y = (getHeight() / 2 + intLightRadius * 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius) * (int) Math.floor(intBoardSize / 2));
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

		// Board randomized 25 times before checking for number of lights on to prevent
		// easily solvable board layouts
		for (int count = 0; count < 25; count++)
			toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));

		if (intBoardSize == 5)
			do {
				toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));
				// Repeats loop if the number of starting lights are not equal to user's request
				// amount
			} while (lightsOn() != intStartingLights);
		else
			do {
				toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));
				// Repeats loop if the number of starting lights are not equal to user's request
				// amount
			} while (boardSolved());

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
					boolClearText = true;
					intLightsToggled++;
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

		if (lightCase(boolLights) >= 0 && intBoardSize == 5)
			for (int column = 0; column < intBoardSize; column++)
				boolLightSolution[column][0] = boolLightCaseSolution[lightCase(boolLights)][column];
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
		if (intBoardSize == 5) {

			boolean flag;

			for (int num = 0; num < 7; num++) {
				flag = true;
				for (int row = 0; row < intBoardSize; row++)
					if (boolLightCase[num][row] != array[row][intBoardSize - 1])
						flag = false;
				if (flag == true)
					return num;
			}
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
	public void createHighScore() throws IOException {
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
			if (score < intHighScoreValue[rank] || intHighScoreValue[rank] == -1) {
				for (int moveRank = 9; moveRank > rank; moveRank--) {
					strHighScoreName[moveRank] = strHighScoreName[moveRank - 1];
					intHighScoreValue[moveRank] = intHighScoreValue[moveRank - 1];
				}

				strHighScoreName[rank] = name;
				intHighScoreValue[rank] = score;

				break;
			}
	}

	public boolean testHighScore(int score) throws IOException {
		int highestScore = intHighScoreValue[0];

		for (int rank = 1; rank < 10; rank++)
			if (intHighScoreValue[rank] > highestScore) {
				highestScore = intHighScoreValue[rank];
			} else if (intHighScoreValue[rank] == -1)
				return true;

		if (score < highestScore)
			return true;

		return true;
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
		if (x >= getWidth() / 2 + 40 && x <= getWidth() / 2 + 375 && y >= getHeight() / 2 - 35
				&& y <= getHeight() / 2 + 30)
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
		if (x >= getWidth() / 2 - 450 && x <= getWidth() / 2 - 270 && y >= getHeight() / 2 - 45
				&& y <= getHeight() / 2 + 55)
			return true;

		return false;
	}

	public void resetButtons() {
		boolIncreaseButtonState = boolStartingLightsButtonState = boolPlayButtonState = boolOptionsButtonState = boolHighScoresButtonState = boolBackButtonState = boolHighScoresBackButtonState = boolBackToMenuButtonState = false;
	}

}