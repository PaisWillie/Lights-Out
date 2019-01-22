
//=================================================================
// Lights Out
// Willie Pai
// 21 January 2018
// Eclipse Java EE IDE Photon Release (4.8.0)
//=================================================================
// Lights Out game with several extra features:
//
// - Options Menu
// 		- Custom number of starting lights (1 - 25)
//			- Starting lights randomizer
// 		- Custom board size (2x2 - 7x7)
// - Replay new or previous game board
// - Score counter
// - High score board with name and score
// 		- Only saves score under following conditions
// 			- Board size must be 5x5
// 			- Cannot use solution to solve board
// 			- Cannot replay previously solved board
// 			- Score is better than the top 10 scores
// 			- Must have 15 starting lights
//=================================================================  
// Planned Features:
//
// - Custom game skins/texture
// 		- 8-bit texture
//=================================================================

// Imports multiple libraries to add functionality to program.
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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

	// Initializes 2D boolean array to determine if each light is on.
	boolean[][] boolLightState;
	// Initializes 2D boolean array as placeholder to keep original board layout.
	boolean[][] boolOriginalBoard;
	// Initializes 2D boolean array to track illuminated state when mouse hovers
	// over light.
	boolean[][] boolLightHover;
	// Initializes 2D boolean array to track which light needs to be pressed for
	// solution.
	boolean[][] boolLightSolution;
	// Initializes 3D integer array for the x & y coordinates for each light.
	int[][][] intLightPosition;
	// Initializes 1D integer array for the top 10 scores on the High Score list.
	int[] intHighScoreValue = new int[10];
	// Initializes 1D boolean array to keep track of lights toggled on top row of
	// board.
	boolean[] boolLightSolutionTop;
	// Initializes 1D String array for the top 10 names on the High Score list.
	String[] strHighScoreName = new String[10];
	// Initializes 2D constant boolean array for different light cases that can
	// occur on the bottom row of the board.
	final boolean[][] boolLightCase = { { true, false, false, false, true }, { false, true, false, true, false },
			{ true, true, true, false, false }, { false, false, true, true, true }, { true, false, true, true, false },
			{ false, true, true, false, true }, { true, true, false, true, true } };
	// Initializes 2D constant boolean array for different light state cases for
	// game solution.
	final boolean[][] boolLightCaseSolution = { { true, true, false, false, false },
			{ true, false, false, true, false }, { false, true, false, false, false },
			{ false, false, false, true, false }, { false, false, false, false, true },
			{ true, false, false, false, false }, { false, false, true, false, false } };
	// Initializes boolean variable to keep track if user requests solution.
	boolean boolRequestSolution;
	// Initializes boolean variable to keep track if screen requires clearing.
	boolean boolClearScreen;
	// Initializes boolean variable to keep track if specific text on screen needs
	// clearing.
	boolean boolClearText;
	// Initializes boolean variable to keep track if user can enter text .
	boolean boolNameInput;
	// Initializes constant integer for the radius size of the light.
	final int intLightRadius = 31;
	// Initializes integer variable to keep track of size of board.
	int intBoardSize;
	// Initializes integer variable to keep track of number of lights to start on
	// board.
	int intStartingLights;
	// Initializes integer variable to keep track of number of lights toggled.
	int intLightsToggled;
	// Initializes string variable to keep track of what information should be
	// displayed.
	String strDisplay;
	// Initializes string variable to keep track of specific conflict that does not
	// allow user to save high score.
	String strRequirementError;
	// Initializes string variable for name input for high score.
	String strName;
	// Initializes image variables for different lights states.
	Image imgLightOff, imgLightHoverOff, imgLightHoverOn, imgLightOn;
	// Initializes image variables for images on menu GUI.
	Image imgCenterLine, imgHorizontalLine, imgTitle, imgCredits, imgMoves, imgWin, imgSpaceContinue,
			imgBoardSizeWarning, imgStartingLightsWarning, imgNewHighScore, imgNoNewHighScore, imgRequirementScore,
			imgRequirementStartingLights, imgRequirementSolution, imgTypeName, imgEnterContinue,
			imgRequirementBoardSize, imgRequirementReplay, imgReplay, imgRank1, imgRank2, imgRank3, imgRank4, imgRank5,
			imgRank6, imgRank7, imgRank8, imgRank9, imgRank10, imgDigit0, imgDigit1, imgDigit2, imgDigit3, imgDigit4,
			imgDigit5, imgDigit6, imgDigit7, imgDigit8, imgDigit9;
	// Initializes image variables for images that have an on/off state on the menu
	// GUI.
	Image imgMenuOff, imgMenuOn, imgRandomizeOn, imgRandomizeOff, imgBackToMenuOff, imgBackToMenuOn, imgShowSolutionOff,
			imgShowSolutionOn, imgPlayOff, imgPlayOn, imgOptionsOff, imgOptionsOn, imgHighScoresOff, imgHighScoresOn,
			imgPreviousGameOff, imgPreviousGameOn, imgNewGameOff, imgNewGameOn, imgStartingLightsOff,
			imgStartingLightsOn, imgBoardSizeOff, imgBoardSizeOn, imgBoardSkinsOff, imgBoardSkinsOn, imgBackOff,
			imgBackOn, imgIncreaseOff, imgIncreaseOn, imgDecreaseOff, imgDecreaseOn;
	// Initializes boolean variables that keep track of on/off states for images.
	boolean boolPlayButtonState, boolOptionsButtonState, boolHighScoresButtonState, boolBackButtonState,
			boolHighScoresBackButtonState, boolStartingLightsButtonState, boolBoardSizeButtonState,
			boolIncreaseButtonState, boolDecreaseButtonState, boolBackToMenuButtonState, boolRandomizeButtonState,
			boolPreviousGameButtonState, boolNewGameButtonState, boolMenuButtonState;

	// =======================================================================
	// init method
	// Implemented procedural initialization method; first method to run that
	// initializes any objects.
	// No parameters
	// Returns void
	// =======================================================================
	public void init() {
		// Allows JApplet to be focusable; is required for keyboard input.
		setFocusable(true);
		// Allows program to read mouse & keyboard inputs
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		// Declares image variables to specified images in directory.
		imgLightOn = getImage(getDocumentBase(), "Images/Board/lightOn.png");
		imgLightHoverOn = getImage(getDocumentBase(), "Images/Board/lightHoverOn.png");
		imgLightHoverOff = getImage(getDocumentBase(), "Images/Board/lightHoverOff.png");
		imgLightOff = getImage(getDocumentBase(), "Images/Board/lightOff.png");
		imgMoves = getImage(getDocumentBase(), "Images/Board/Moves.png");
		imgTitle = getImage(getDocumentBase(), "Images/Menu/Title.png");
		imgCredits = getImage(getDocumentBase(), "Images/Menu/Credits.png");
		imgBackToMenuOff = getImage(getDocumentBase(), "Images/Board/BackToMenuOff.png");
		imgBackToMenuOn = getImage(getDocumentBase(), "Images/Board/BackToMenuOn.png");
		imgMenuOff = getImage(getDocumentBase(), "Images/Menu/MenuOff.png");
		imgMenuOn = getImage(getDocumentBase(), "Images/Menu/MenuOn.png");
		imgShowSolutionOff = getImage(getDocumentBase(), "Images/Board/ShowSolutionOff.png");
		imgShowSolutionOn = getImage(getDocumentBase(), "Images/Board/ShowSolutionOn.png");
		imgWin = getImage(getDocumentBase(), "Images/Board/Win.png");
		imgPreviousGameOff = getImage(getDocumentBase(), "Images/Menu/PreviousGameOff.png");
		imgPreviousGameOn = getImage(getDocumentBase(), "Images/Menu/PreviousGameOn.png");
		imgNewGameOff = getImage(getDocumentBase(), "Images/Menu/NewGameOff.png");
		imgNewGameOn = getImage(getDocumentBase(), "Images/Menu/NewGameOn.png");
		imgReplay = getImage(getDocumentBase(), "Images/Menu/Replay.png");
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
		imgRequirementReplay = getImage(getDocumentBase(), "Images/HighScores/RequirementReplay.png");
		imgTypeName = getImage(getDocumentBase(), "Images/HighScores/TypeName.png");
		imgStartingLightsOff = getImage(getDocumentBase(), "Images/Menu/StartingLightsOff.png");
		imgStartingLightsOn = getImage(getDocumentBase(), "Images/Menu/StartingLightsOn.png");
		imgStartingLightsWarning = getImage(getDocumentBase(), "Images/Menu/StartingLightsWarning.png");
		imgRandomizeOff = getImage(getDocumentBase(), "Images/Menu/RandomizeOff.png");
		imgRandomizeOn = getImage(getDocumentBase(), "Images/Menu/RandomizeOn.png");
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

	// =======================================================================
	// start method
	// Implemented procedural method to run on program start.
	// No parameters
	// Returns void
	// =======================================================================
	public void start() {
		// Sets the applet's display resolution size.
		this.setSize(1280, 720);
		// Sets the applet's background color to black.
		setBackground(Color.black);
		// Declares integer board size to default 5.
		intBoardSize = 5;
		// Calls arraySetup method; initializes all array to specified size.
		arraySetup();
		// Calls resetButtons method; initializes boolean button state variables to
		// false.
		resetButtons();
		// Declares string variable to start program on game menu.
		strDisplay = "Menu";
		// Declares integer starting lights to default 15.
		intStartingLights = 15;
		// Initializes boolean screen/text clearing and name input variables to false.
		boolClearScreen = boolClearText = boolNameInput = false;
		// Try/catch to create/read high score files.
		try {
			// Calls createHighScore method; creates high score file if it does not
			// exist/read file if it does exist.
			createHighScore();
			// Catches I/O exception.
		} catch (IOException e) {
			// Forces program to ignore runtime exception that may occur.
			throw new RuntimeException(e);
		}
	}

	// =======================================================================
	// stop method
	// Implemented procedural method; runs when program is stopped.
	// No parameters
	// Returns void
	// =======================================================================
	public void stop() {
		// Try/catch to save high score list to file.
		try {
			// Writes high score array to text file.
			writeHighScore();
			// catches I/O exception.
		} catch (IOException e) {
			// Forces program to ignroe runtime exception that may occur.
			throw new RuntimeException(e);
		}
	}

	// =======================================================================
	// destroy method
	// Implemented procedural method; runs when program is destroyed.
	// No parameters
	// Returns void
	// =======================================================================
	public void destroy() {
	}

	// =======================================================================
	// newBoardSetup method
	// Procedural method to reset variables when new game is run.
	// No parameters
	// Returns void
	// =======================================================================
	public void newBoardSetup() {
		// Sets lights toggled counter to 0.
		intLightsToggled = 0;
		// Resets user's name input.
		strName = "";
		// Clears any conflicts that prevents setting high score.
		strRequirementError = "";
		// Turns off solution.
		boolRequestSolution = false;
		// Randomizes game board.
		boardRandomize();
		// Pre-solves game board for solution.
		gameSolution();
		// Saves a copy of the original board if requested to be used again later.
		for (int i = 0; i < intBoardSize; i++)
			boolOriginalBoard[i] = Arrays.copyOf(boolLightState[i], boolLightState[i].length);
	}

	// =======================================================================
	// previousBoardSetup method
	// Procedural method to reset variables when the same game is run.
	// No parameters
	// Returns void
	// =======================================================================
	public void previousBoardSetup() {
		// Resets lights toggled to 0.
		intLightsToggled = 0;
		// Sets string variable to "Replay" to give reason to prevent user from
		// submitting high score.
		strRequirementError = "Replay";
		// Resets the boolean variable to false to disable solution request.
		boolRequestSolution = false;
		// Counted loop for each row of the board.
		for (int i = 0; i < intBoardSize; i++)
			// Copies original/previous board layout to game board to repeat game.
			boolLightState[i] = Arrays.copyOf(boolOriginalBoard[i], boolOriginalBoard[i].length);
		// Pre-solves game board for solution.
		gameSolution();
	}

	// =======================================================================
	// arraySetup method
	// Procedural method to initialize arrays to board size.
	// No parameters
	// Returns void
	// =======================================================================
	public void arraySetup() {
		// Initializes 2D boolean array for light state on game board.
		boolLightState = new boolean[intBoardSize][intBoardSize];
		// Initializes 2D boolean array to keep original board layout.
		boolOriginalBoard = new boolean[intBoardSize][intBoardSize];
		// Initializes 2D array to track illuminated state when mouse hovers over light.
		boolLightHover = new boolean[intBoardSize][intBoardSize];
		// Initializes 2D array to track which light needs to be pressed for solution.
		boolLightSolution = new boolean[intBoardSize][intBoardSize];
		// Initializes 3D array for the x & y coordinates for each light.
		intLightPosition = new int[2][intBoardSize][intBoardSize];
		// Initializes 1D array to keep track of light state of top row of board.
		boolLightSolutionTop = new boolean[intBoardSize];

	}

	// =======================================================================
	// paint method
	// Implemented procedural method to draw objects on screen
	// Graphics g parameter; allows drawing of objects on applet
	// Returns void
	// =======================================================================
	public void paint(Graphics g) {
		// Fixes position of lights if screen resolution is changed by user.
		lightPosition();
		// Constant integer for width of each light circle.
		final int circleWidth = 4;
		// Initializes Graphics2D object for drawing circle rings on board.
		Graphics2D gCircle = (Graphics2D) g;
		// Sets gCircle object stroke size to width of circle ring.
		gCircle.setStroke((new BasicStroke(circleWidth)));
		// Tests if screen requires clearing
		if (boolClearScreen) {
			// Draws clearing rectangle on entire screen
			g.clearRect(0, 0, getWidth(), getHeight());
			// Resets boolean variable back to false; prevent redrawing of clear rectangle.
			boolClearScreen = false;
		}
		// Switch/case to read string variable; determines what is shown on the screen.
		switch (strDisplay) {
		// Tests if screen should display the menu.
		case "Menu":
			// Draws images positioned based on center of screen.
			g.drawImage(imgTitle, getWidth() / 2 - 425, getHeight() / 2 - 66, this);
			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgCredits, getWidth() / 2 - 175, getHeight() / 2 + 60, this);
			// Tests if button state is on or off
			if (boolPlayButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgPlayOn, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgPlayOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			// Tests if button state is on or off
			if (boolOptionsButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgOptionsOn, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgOptionsOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			// Tests if button state is on or off
			if (boolHighScoresButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgHighScoresOn, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgHighScoresOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			// Breaks out of switch/case.
			break;
		// Tests if screen should display the options.
		case "Options":
			// Draws images positioned based on center of screen.
			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgOptionsOn, getWidth() / 2 - 300, getHeight() / 2 - 50, this);
			// Tests if button state is on or off
			if (boolStartingLightsButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgStartingLightsOn, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgStartingLightsOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			// Tests if button state is on or off
			if (boolBoardSizeButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgBoardSizeOn, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgBoardSizeOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			// Draws button off state positioned based on center of screen.
			g.drawImage(imgBoardSkinsOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			// Tests if button state is on or off
			if (boolBackButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgBackOn, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			// Breaks out of switch/case.
			break;
		// Tests if screen should display the starting lights option.
		case "StartingLights":
			// Tests if text requires clearing.
			if (boolClearText) {
				// Clears text based on center position.
				g.clearRect(getWidth() / 2 + 125, getHeight() / 2 - 50, 150, 75);
				// Resets boolean variable to prevent clearing multiple times.
				boolClearText = false;
			}
			// Draws images positioned based on center of screen.
			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgStartingLightsOn, getWidth() / 2 - 500, getHeight() / 2 - 50, this);
			// Tests if button state is on or off
			if (boolRandomizeButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgRandomizeOn, getWidth() / 2 + 87, getHeight() / 2 + 70, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgRandomizeOff, getWidth() / 2 + 87, getHeight() / 2 + 70, this);
			// Tests if button state is on or off
			if (boolDecreaseButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgDecreaseOn, getWidth() / 2 + 50, getHeight() / 2 - 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgDecreaseOff, getWidth() / 2 + 50, getHeight() / 2 - 50, this);
			// Tests if button state is on or off
			if (boolIncreaseButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgIncreaseOn, getWidth() / 2 + 300, getHeight() / 2 - 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgIncreaseOff, getWidth() / 2 + 300, getHeight() / 2 - 50, this);
			// Tests if button state is on or off
			if (boolBackButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgBackOn, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			// Tests if starting lights integer is not equal to 15.
			if (intStartingLights != 15)
				// Draws starting lights warning; warns user that other starting light numbers.
				// do not apply for high score.
				g.drawImage(imgStartingLightsWarning, getWidth() / 2 - 324, getHeight() / 2 + 200, this);
			else {
				// Clears text if starting lights integer equals 15.
				g.clearRect(getWidth() / 2 - 324, getHeight() / 2 + 200, 648, 130);
			}
			// Tests if starting lights integer is greater or equal to 20.
			if (intStartingLights >= 20) {
				// Draws tens digit based on center position of screen.
				g.drawImage(imgDigit2, getWidth() / 2 + 140, getHeight() / 2 - 52, this);
				// Switch/case to read starting lights integer
				switch (intStartingLights) {
				// Tests starting lights integer.
				case 20:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit0, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 21:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit1, getWidth() / 2 + 210, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 22:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit2, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 23:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit3, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 24:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit4, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 25:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit5, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 26:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit6, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 27:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit7, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 28:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit8, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 29:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit9, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				}
			} else if (intStartingLights >= 10) {
				// Draws tens digit based on center position of screen.
				g.drawImage(imgDigit1, getWidth() / 2 + 150, getHeight() / 2 - 52, this);
				switch (intStartingLights) {
				// Tests starting lights integer.
				case 10:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit0, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 11:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit1, getWidth() / 2 + 210, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 12:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit2, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 13:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit3, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 14:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit4, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 15:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit5, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 16:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit6, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 17:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit7, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 18:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit8, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 19:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit9, getWidth() / 2 + 200, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				}
				// Runs if starting lights integer is less than 10.
			} else
				// Switch/case for displaying number of starting lights.
				switch (intStartingLights) {
				// Tests starting lights integer..
				case 1:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit1, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 2:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit2, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 3:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit3, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 4:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit4, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 5:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit5, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 6:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit6, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 7:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit7, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 8:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit8, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				// Tests starting lights integer.
				case 9:
					// Draws ones digit based on center position of screen.
					g.drawImage(imgDigit9, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
					// Breaks out of switch/case.
					break;
				}
			// Breaks out of switch/case.
			break;
		// Tests if screen should display the board size option.
		case "BoardSize":
			// Tests boolean variable if text needs to be cleared from screen.
			if (boolClearText) {
				// Draws clearing rectangle over text.
				g.clearRect(getWidth() / 2 + 125, getHeight() / 2 - 50, 150, 75);
				// Resets boolean variable to false.
				boolClearText = false;
			}
			// Draws images based on center screen position.
			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgStartingLightsOn, getWidth() / 2 - 500, getHeight() / 2 - 50, this);
			// Tests if board size integer is not 5.
			if (intBoardSize != 5)
				// Draws warning for unsupported board size.
				g.drawImage(imgBoardSizeWarning, getWidth() / 2 - 315, getHeight() / 2 + 200, this);
			// Runs if board size is 5.
			else {
				// Clears warning message.
				g.clearRect(getWidth() / 2 - 315, getHeight() / 2 + 200, 631, 130);
			}
			// Tests if button state is on/off
			if (boolDecreaseButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgDecreaseOn, getWidth() / 2 + 50, getHeight() / 2 - 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgDecreaseOff, getWidth() / 2 + 50, getHeight() / 2 - 50, this);
			// Tests if button state is on/off
			if (boolIncreaseButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgIncreaseOn, getWidth() / 2 + 300, getHeight() / 2 - 50, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgIncreaseOff, getWidth() / 2 + 300, getHeight() / 2 - 50, this);
			// Tests if button state is on/off
			if (boolBackButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgBackOn, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgBackOff, getWidth() / 2 - 125, getHeight() / 2 + 80, this);
			// Switch case for drawing board size number.
			switch (intBoardSize) {
			case 2:
				// Draws digit based on center position of screen.
				g.drawImage(imgDigit2, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
				// Breaks out of switch/case.
				break;
			case 3:
				// Draws digit based on center position of screen.
				g.drawImage(imgDigit3, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
				// Breaks out of switch/case.
				break;
			case 4:
				// Draws digit based on center position of screen.
				g.drawImage(imgDigit4, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
				// Breaks out of switch/case.
				break;
			case 5:
				// Draws digit based on center position of screen.
				g.drawImage(imgDigit5, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
				// Breaks out of switch/case.
				break;
			case 6:
				// Draws digit based on center position of screen.
				g.drawImage(imgDigit6, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
				// Breaks out of switch/case.
				break;
			case 7:
				// Draws digit based on center position of screen.
				g.drawImage(imgDigit7, getWidth() / 2 + 165, getHeight() / 2 - 52, this);
				// Breaks out of switch/case.
				break;
			}
			// Breaks out of switch/case.
			break;
		// Tests if screen should display the high scores.
		case "HighScores":
			// Draws image based on center position of screen.
			g.drawImage(imgHighScoresOn, getWidth() / 2 - 625, getHeight() / 2 - 50, this);
			// Tests button state on/off.
			if (boolHighScoresBackButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgBackOn, getWidth() / 2 - 350, getHeight() / 2 + 125, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgBackOff, getWidth() / 2 - 350, getHeight() / 2 + 125, this);
			// Draws ranking numbers on high score list.
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
			// Sets font color and style of text.
			g.setColor(Color.cyan);
			g.setFont(new Font("Unispace", Font.BOLD, 30));
			// Counted loop for going through each rank of high score list.
			for (int rank = 0; rank < 10; rank++)
				// Tests if rank integer is less than 5 and if there is an available score to
				// display.
				if (rank < 5 && intHighScoreValue[rank] > 0) {
					// Displays name and score value of high score list.
					g.drawString(strHighScoreName[rank], getWidth() / 2 - 100, getHeight() / 2 + rank * 70 - 123);
					g.drawString(Integer.toString(intHighScoreValue[rank]), getWidth() / 2 + 100,
							getHeight() / 2 + rank * 70 - 123);
					// Tests if there is an available score to display.
				} else if (intHighScoreValue[rank] > 0) {
					// Displays name and score value of high score list.
					g.drawString(strHighScoreName[rank], getWidth() / 2 + 310, getHeight() / 2 + (rank - 5) * 70 - 123);
					g.drawString(Integer.toString(intHighScoreValue[rank]), getWidth() / 2 + 510,
							getHeight() / 2 + (rank - 5) * 70 - 123);
				}
			// Breaks from switch/case.
			break;
		// Tests if screen should display the game board.
		case "Board":
			// Tests if board size is greater than 5.
			if (intBoardSize > 5)
				// Draws title a little higher to allow for room for bigger boards.
				g.drawImage(imgTitle, getWidth() / 2 - 205, getHeight() / 2 - 385, this);
			else
				// Draw Lights Out title
				g.drawImage(imgTitle, getWidth() / 2 - 205, getHeight() / 2 - 325, this);
			// Draw Show Solution button
			if (boolRequestSolution && intBoardSize == 5)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgShowSolutionOn, getWidth() / 2 + 250, getHeight() / 2 - 55, this);
			// Tests if board size is board size is 5.
			else if (intBoardSize == 5)
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgShowSolutionOff, getWidth() / 2 + 250, getHeight() / 2 - 55, this);
			// Tests button state on/off
			if (boolBackToMenuButtonState)
				// Draws button on state positioned based on center of screen.
				g.drawImage(imgBackToMenuOn, getWidth() / 2 - 460, getHeight() / 2 - 55, this);
			else
				// Draws button off state positioned based on center of screen.
				g.drawImage(imgBackToMenuOff, getWidth() / 2 - 460, getHeight() / 2 - 55, this);
			// Tests if board size is greater than 5.
			if (intBoardSize > 5)
				// Draws moves lower to accumulate for bigger board size.
				g.drawImage(imgMoves, getWidth() / 2 - 125, getHeight() / 2 + 265, this);
			else
				// Draws moves image at bottom of board
				g.drawImage(imgMoves, getWidth() / 2 - 125, getHeight() / 2 + 225, this);
			// Tests if lights toggled integer count is 0.
			if (intLightsToggled == 0) {
				// Sets font color and style
				g.setColor(Color.cyan);
				g.setFont(new Font("Unispace", Font.BOLD, 30));
				// Tests if board size bigger than 5.
				if (intBoardSize > 5)
					// Draws number of lights toggled lower if bigger board size
					g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 300);
				else
					// Draws number of lights toggled
					g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 260);
				// Tests if text needs to be cleared;
			} else if (boolClearText) {
				// Resets boolean variable;
				boolClearText = false;
				// Tests if board size is bigger than 5
				if (intBoardSize > 5)
					// Clears light toggled number to prevent text overlap
					g.clearRect(getWidth() / 2 + 50, getHeight() / 2 + 275, 100, 25);
				else
					// Clears light toggled number to prevent text overlap
					g.clearRect(getWidth() / 2 + 50, getHeight() / 2 + 235, 100, 25);
				// Sets color font and style
				g.setColor(Color.cyan);
				g.setFont(new Font("Unispace", Font.BOLD, 30));
				// Tests if board size is bigger than 5
				if (intBoardSize > 5)
					// Draws number of lights toggled at bottom of board
					g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 300);
				else
					// Draws number of lights toggled at bottom of board
					g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 260);
			}
			// Boolean flag to prevent drawing more than one solution circle
			boolean boolShowSolution = true;
			// Tests if user requests board solution
			if (boolRequestSolution)
				// Calls displaySolution method; changes solution array to show solution lights.
				displaySolution();
			// Counted loop for each row of board
			for (int row = 0; row < intBoardSize; row++) {
				// Counted loop for each column board
				for (int column = 0; column < intBoardSize; column++) {
					// Tests if light is false and cursor is not hovering it
					if (boolLightState[column][row] == false && boolLightHover[column][row] == false)
						// Draws off light
						g.drawImage(imgLightOff, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
					// Tests if light is true and cursor is not hovering it
					else if (boolLightState[column][row] && boolLightHover[column][row] == false)
						// Draws on light
						g.drawImage(imgLightOn, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
					// Tests if light is on and cursor is hovering it
					else if (boolLightState[column][row] && boolLightHover[column][row])
						// Draws hover on light
						g.drawImage(imgLightHoverOn, intLightPosition[0][column][row], intLightPosition[1][column][row],
								this);
					else if (boolLightState[column][row] == false && boolLightHover[column][row])
						// Draws hover off image
						g.drawImage(imgLightHoverOff, intLightPosition[0][column][row],
								intLightPosition[1][column][row], this);
					// Tests if the user requests solution, if the value of the array
					if (boolRequestSolution && boolLightSolution[column][row] & boolShowSolution) {
						// Sets boolean value to false to prevent other solution circles from drawing
						boolShowSolution = false;
						// Sets color of solution circle to red
						g.setColor(Color.red);
						// Draws solution circle around light that needs to be toggled
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
			// Breaks from switch/case.
			break;
		// Tests if screen should display the game win screen.
		case "Game Win":
			// Draws images based on center position of screen
			g.drawImage(imgWin, getWidth() / 2 - 165, getHeight() / 2 - 150, this);
			g.drawImage(imgSpaceContinue, getWidth() / 2 - 210, getHeight() / 2 + 50, this);
			g.drawImage(imgMoves, getWidth() / 2 - 125, getHeight() / 2 - 25, this);
			// Sets font color and style
			g.setColor(Color.cyan);
			g.setFont(new Font("Unispace", Font.BOLD, 30));
			// Draws number of lights toggled
			g.drawString(Integer.toString(intLightsToggled), getWidth() / 2 + 50, getHeight() / 2 + 10);
			// Breaks from switch/case
			break;
		// Tests if screen should display the add new high score screen.
		case "New HighScore":
			// Draws images based on center position of screen.
			g.drawImage(imgHorizontalLine, getWidth() / 2 - 360, getHeight() / 2 - 12, this);
			g.drawImage(imgNewHighScore, getWidth() / 2 - 276, getHeight() / 2 - 125, this);
			g.drawImage(imgTypeName, getWidth() / 2 - 282, getHeight() / 2 + 25, this);
			// Tests if text needs clearing
			if (boolClearText) {
				// Resets boolean clear text variable
				boolClearText = false;
				// Draws clear rectangle to clear text
				g.clearRect(getWidth() / 2 - 75, getHeight() / 2 + 125, 150, 50);
			}
			// Sets font color & style
			g.setColor(Color.cyan);
			g.setFont(new Font("Unispace", Font.BOLD, 30));
			// Draws user's name input on screen
			g.drawString(strName, getWidth() / 2 - 75, getHeight() / 2 + 150);
			// Breaks from switch/case
			break;
		// Tests if screen should display the no new high score screen.
		case "No HighScore":
			// Draws images based on center position of screen.
			g.drawImage(imgHorizontalLine, getWidth() / 2 - 360, getHeight() / 2 - 12, this);
			g.drawImage(imgNoNewHighScore, getWidth() / 2 - 313, getHeight() / 2 - 125, this);
			g.drawImage(imgEnterContinue, getWidth() / 2 - 202, getHeight() / 2 + 125, this);
			// Switch/case for different conflicts with adding new high score
			switch (strRequirementError) {
			// Runs if score is not low enough to appear on high score list
			case "Score":
				g.drawImage(imgRequirementScore, getWidth() / 2 - 288, getHeight() / 2 + 25, this);
				break;
			// Runs if user used solution to solve board
			case "Solution":
				g.drawImage(imgRequirementSolution, getWidth() / 2 - 350, getHeight() / 2 + 25, this);
				break;
			// Runs if board does not start with 15 lights
			case "StartingLights":
				g.drawImage(imgRequirementStartingLights, getWidth() / 2 - 350, getHeight() / 2 + 25, this);
				break;
			// Runs if board size is not 5
			case "BoardSize":
				g.drawImage(imgRequirementBoardSize, getWidth() / 2 - 222, getHeight() / 2 + 25, this);
				break;
			// Runs if user is playing same previous board
			case "Replay":
				g.drawImage(imgRequirementReplay, getWidth() / 2 - 282, getHeight() / 2 + 25, this);
				break;
			}
			break;
		// Tests if screen should display the replay screen.
		case "Replay":
			// Draws images based on center of screen
			g.drawImage(imgCenterLine, getWidth() / 2 - 12, getHeight() / 2 - 180, this);
			g.drawImage(imgReplay, getWidth() / 2 - 300, getHeight() / 2 - 50, this);
			// Tests button state on/off
			if (boolNewGameButtonState)
				// Draws on button state
				g.drawImage(imgNewGameOn, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			else
				// Draws off button state
				g.drawImage(imgNewGameOff, getWidth() / 2 + 25, getHeight() / 2 - 150, this);
			// Tests button state on/off
			if (boolPreviousGameButtonState)
				// Draws on button state
				g.drawImage(imgPreviousGameOn, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			else
				// Draws off button state
				g.drawImage(imgPreviousGameOff, getWidth() / 2 + 25, getHeight() / 2 - 50, this);
			// Tests button state on/off
			if (boolMenuButtonState)
				// Draws on button state
				g.drawImage(imgMenuOn, getWidth() / 2 + 25, getHeight() / 2 + 50, this);
			else
				// Draws off button state
				g.drawImage(imgMenuOff, getWidth() / 2 + 25, getHeight() / 2 + 50, this);

		}

	}

	// =======================================================================
	// mouseEntered method
	// Procedural method that runs when mouse enters applet
	// No parameters
	// Returns void
	// =======================================================================
	public void mouseEntered(MouseEvent event) {

	}

	// =======================================================================
	// mouseExited method
	// Procedural method that runs when mouse exits applet
	// No parameters
	// Returns void
	// =======================================================================
	public void mouseExited(MouseEvent event) {

	}

	// =======================================================================
	// mousePressed method
	// Procedural method to run when mouse is pressed
	// No parameters
	// Returns void
	// =======================================================================
	public void mousePressed(MouseEvent event) {

	}

	// =======================================================================
	// mouseReleased method
	// Procedural method to run when mouse is released
	// No parameters
	// Returns void
	// =======================================================================
	public void mouseReleased(MouseEvent event) {
		// Initializes integer variable for mouse x and y coordinates
		int intx = event.getX();
		int inty = event.getY();
		// Switch case for different screen output displays
		switch (strDisplay) {
		case "Menu":
			// Tests if mouse coordinates are on play button
			if (playButton(intx, inty)) {
				// Changes display to board
				strDisplay = "Board";
				// Calls newBoardSetup method; Creates new board
				newBoardSetup();
				// Tests if new board size is not 5
				if (intBoardSize != 5)
					// Declares high score conflict as board size
					strRequirementError = "BoardSize";
				// Tests if starting lights is not 15
				else if (intStartingLights != 15)
					// Declares high score conflict as starting lights
					strRequirementError = "StartingLights";
			}
			// Tests if mouse is on options button area
			if (optionsButton(intx, inty))
				// Changes display to Options
				strDisplay = "Options";
			// Tests if mouse is on high scores button
			if (highScoresButton(intx, inty))
				// Changes display to high scores
				strDisplay = "HighScores";
			// Tests if any button on menu is clicked
			if (playButton(intx, inty) || (optionsButton(intx, inty)) || highScoresButton(intx, inty)) {
				// Resets state of buttons
				resetButtons();
				// Sets boolean to true to clear screen; prevents image overlap
				boolClearScreen = true;
			}
			// Breaks switch/case
			break;
		case "Options":
			// Tests if clicked back button
			if (backButton(intx, inty))
				// Changes display to menuy
				strDisplay = "Menu";
			// Tests if clicked starting light button and board size is 5
			if (startingLightsButton(intx, inty) && intBoardSize == 5)
				// Changes display to starting lights
				strDisplay = "StartingLights";
			// Tests if clicked board size button
			if (boardSizeButton(intx, inty))
				// Changes display to board size option
				strDisplay = "BoardSize";
			// Tests if any button is clicked
			if (backButton(intx, inty) || startingLightsButton(intx, inty) || boardSizeButton(intx, inty)) {
				resetButtons();
				// Boolean set to true to clear screen
				boolClearScreen = true;
			}
			// Breaks switch/case
			break;
		case "StartingLights":
			// Tests if click decrease button and starting lights is more than 1
			if (decreaseButton(intx, inty) && intStartingLights > 1) {
				// Clears previous number text
				boolClearText = true;
				// Decreases number of starting lights
				intStartingLights -= 1;
			}
			// Tests if click increase button and starting lights is less than 25
			if (increaseButton(intx, inty) && intStartingLights < 25) {
				// Clears previous number text
				boolClearText = true;
				// Increases number of starting lights
				intStartingLights += 1;
			}
			// Tests if click randomize button
			if (randomizeButton(intx, inty)) {
				// Randomizes number of starting lights
				intStartingLights = (int) (Math.random() * 24) + 1;
				// Clears previouis number text
				boolClearText = true;
			}
			// Tests if click back button
			if (backButton(intx, inty)) {
				// Changes display to options
				strDisplay = "Options";
				// Clears screen
				boolClearScreen = true;
				// Resets button state
				resetButtons();
			}
			// Breaks switch/case
			break;
		case "BoardSize":
			// Tests if click decrease button & board size is greater than 2
			if (decreaseButton(intx, inty) && intBoardSize > 2) {
				// Clears text
				boolClearText = true;
				// Decreases board size
				intBoardSize--;
				// Sets array to board size
				arraySetup();
			}
			// Tests if click increase button & board size is less than 7
			if (increaseButton(intx, inty) && intBoardSize < 7) {
				// Clears text
				boolClearText = true;
				// Increases board size
				intBoardSize++;
				// Sets array to board size
				arraySetup();
			}
			// Tests if click back button
			if (backButton(intx, inty)) {
				// Sets display to options
				strDisplay = "Options";
				// Clears screen
				boolClearScreen = true;
				// Resets button states
				resetButtons();
			}
			// Breaks switch/case
			break;
		case "HighScores":
			// Tests if click back button
			if (highScoresBackButton(intx, inty)) {
				// Sets display to menu
				strDisplay = "Menu";
				// Clears screen
				boolClearScreen = true;
				// Resets button states
				resetButtons();
			}
			// Breaks switch/case
			break;
		case "Board":
			// Toggles light where mouse is hovering over
			mouseToggleLight(intx, inty);
			// Tests if board is solved
			if (boardSolved()) {
				// Changes display to win screen
				strDisplay = "Game Win";
				// Clears screen
				boolClearScreen = true;
			}
			// Tests if click solution button and board size is 5
			if (showSolutionButton(intx, inty) && intBoardSize == 5) {
				// Toggles solution request
				boolRequestSolution = !boolRequestSolution;
				// Changes high score conflict to solution
				strRequirementError = "Solution";
			}
			// Tests if back button clicked
			if (backToMenuButton(intx, inty)) {
				// Changes display to menu
				strDisplay = "Menu";
				// Disables solution request
				boolRequestSolution = false;
				// Clears screen
				boolClearScreen = true;
			}
			// Changes solution indicators on top row of board
			caseSolutionTop(intx, inty);
			// Breaks from switch/case
			break;
		case "Replay":
			// Tests if new game button is clicked
			if (newGameButton(intx, inty)) {
				// Sets up new board
				newBoardSetup();
				// Changes display to board
				strDisplay = "Board";
			}
			// Tests if previous game button is clicked
			if (previousGameButton(intx, inty)) {
				// Sets up previous board
				previousBoardSetup();
				// Changes display to board
				strDisplay = "Board";
			}
			// Tests if menu button is clicked
			if (menuButton(intx, inty))
				// Changes display to menu
				strDisplay = "Menu";
			// Tests if any button is clicked
			if (newGameButton(intx, inty) || previousGameButton(intx, inty) || menuButton(intx, inty)) {
				// Clears screen
				boolClearScreen = true;
				// Resets button state
				resetButtons();
			}
		}
		// Repaints screen
		repaint();
	}

	// =======================================================================
	// mouseClicked method
	// Procedural method to run when the mouse is clicked
	// No parameters
	// Returns void
	// =======================================================================
	public void mouseClicked(MouseEvent event) {

	}

	// =======================================================================
	// mouseMoved method
	// Procedural method to run when then mouse is moved
	// No parameters
	// Returns void
	// =======================================================================
	public void mouseMoved(MouseEvent event) {
		// Gets mouse x and y integer coordinates
		int intx = event.getX();
		int inty = event.getY();
		switch (strDisplay) {
		case "Menu":
			// Tests hover over play button
			if (playButton(intx, inty))
				// Button state on
				boolPlayButtonState = true;
			else
				// Button state off
				boolPlayButtonState = false;
			// Tests hover over options button
			if (optionsButton(intx, inty))
				// Button state on
				boolOptionsButtonState = true;
			else
				// Button state off
				boolOptionsButtonState = false;
			// Tests hover over high scores button
			if (highScoresButton(intx, inty))
				// Button state on
				boolHighScoresButtonState = true;
			else
				// Button state off
				boolHighScoresButtonState = false;
			// Breaks switch/case
			break;
		case "Board":
			// Tests if cursor is hovering over lights
			mouseHoverLight(intx, inty);
			// Tests if hovering back to menu button
			if (backToMenuButton(intx, inty))
				// Button state on
				boolBackToMenuButtonState = true;
			else
				// Button state off
				boolBackToMenuButtonState = false;
			// Breaks switch/case
			break;
		case "Options":
			// Tests if hovering starting lights button and board size is 5
			if (startingLightsButton(intx, inty) && intBoardSize == 5)
				// Button state on
				boolStartingLightsButtonState = true;
			else
				// Button state off
				boolStartingLightsButtonState = false;
			// Tests if hovering board size button
			if (boardSizeButton(intx, inty))
				// Button state on
				boolBoardSizeButtonState = true;
			else
				// Button state off
				boolBoardSizeButtonState = false;
			// Tests if hovering back button
			if (backButton(intx, inty))
				// Button state on
				boolBackButtonState = true;
			else
				// Button state off
				boolBackButtonState = false;
			// Breaks switch/casse
			break;
		case "StartingLights":
			// Tests if hovering randomize button
			if (randomizeButton(intx, inty))
				// Button state on
				boolRandomizeButtonState = true;
			else
				// Button state off
				boolRandomizeButtonState = false;
		case "BoardSize":
			// Tests if hovering back button
			if (backButton(intx, inty))
				// Button state on
				boolBackButtonState = true;
			else
				// Button state off
				boolBackButtonState = false;
			// Tests if hovering increase button
			if (increaseButton(intx, inty))
				// Button state on
				boolIncreaseButtonState = true;
			else
				// Button state off
				boolIncreaseButtonState = false;
			// Tests if hovering decrease button
			if (decreaseButton(intx, inty))
				// Button state on
				boolDecreaseButtonState = true;
			else
				// Button state off
				boolDecreaseButtonState = false;
			// Breaks switch/case
			break;
		case "HighScores":
			// Tests if hovering back button
			if (highScoresBackButton(intx, inty))
				// Button state on
				boolHighScoresBackButtonState = true;
			else
				// Button state off
				boolHighScoresBackButtonState = false;
			// Breaks switch/case
			break;
		case "Replay":
			// Tests if hovering new game button
			if (newGameButton(intx, inty))
				// Button state on
				boolNewGameButtonState = true;
			else
				// Buttons state off
				boolNewGameButtonState = false;
			// Tests if hovering previous game button
			if (previousGameButton(intx, inty))
				// Button state on
				boolPreviousGameButtonState = true;
			else
				// Button state off
				boolPreviousGameButtonState = false;
			// Tests if hovering menu button
			if (menuButton(intx, inty))
				// Button state on
				boolMenuButtonState = true;
			else
				// Button state off
				boolMenuButtonState = false;
		}
		// Repaints screen
		repaint();
	}

	// =======================================================================
	// mouseDragged method
	// Procedural method to run when the mouse is dragged
	// No parameters
	// Returns void
	// =======================================================================
	public void mouseDragged(MouseEvent event) {

	}

	// =======================================================================
	// keyPressed method
	// Procedural method to run when a key is pressed
	// No parameters
	// Returns void
	// =======================================================================
	public void keyPressed(KeyEvent event) {

	}

	// =======================================================================
	// keyReleased method
	// Procedural method to run when a key is released
	// No parameters
	// Returns void
	// =======================================================================
	public void keyReleased(KeyEvent event) {

	}

	// =======================================================================
	// keyTyped method
	// Procedural method to run when a key is typed
	// No parameters
	// Returns void
	// =======================================================================
	public void keyTyped(KeyEvent event) {
		// Sets character variable to keyboard input
		char key = event.getKeyChar();
		switch (strDisplay) {
		case "Game Win":
			// Tests if input is spacebar
			if (key == KeyEvent.VK_SPACE) {
				try {
					// Tests if score is applicable for new high score
					if (testHighScore(intLightsToggled) && strRequirementError.equals("")) {
						boolNameInput = true;
						strDisplay = "New HighScore";
						boolClearScreen = true;
					} else {
						// Tests if no conflict but score does not suffice
						if (!testHighScore(intLightsToggled))
							// Sets conflict to too high of score
							strRequirementError = "Score";
						// Changes display to no highscore
						strDisplay = "No HighScore";
						// Clears screen
						boolClearScreen = true;
					}
					// Catches IO Exception
				} catch (IOException e) {
					// Forces program to ignroe runtime exception
					throw new RuntimeException(e);
				}
				// Breaks switch/case
				break;
			}
		case "New HighScore":
			// Tests if input is enter key
			if (key == KeyEvent.VK_ENTER) {
				// Sets boolean to false; stops keyboard input from editing name
				boolNameInput = false;
				// Adds name and score to high score
				addHighScore(strName, intLightsToggled);
				// Changes display to replay menu
				strDisplay = "Replay";
				// Clears screen
				boolClearScreen = true;
				// Tests if input is backspace and length of name is bigger than 0
			} else if (key == KeyEvent.VK_BACK_SPACE && strName.length() > 0) {
				// Deletes one character from name string input
				strName = strName.substring(0, strName.length() - 1);
				// Clears text
				boolClearText = true;
				// Tests input is any defined character
			} else if (key != KeyEvent.CHAR_UNDEFINED && key != KeyEvent.VK_SPACE && key != KeyEvent.VK_BACK_SPACE
					&& strName.length() < 8)
				// Adds character to name input
				strName += key;
			// Breaks switch/case
			break;
		case "No HighScore":
			// Tests if input is enter key
			if (key == KeyEvent.VK_ENTER) {
				// Changes display to replay menu
				strDisplay = "Replay";
				// Clears screen
				boolClearScreen = true;
			}
		}
		// Repaints screen
		repaint();
	}

	// =======================================================================
	// lightPosition method
	//
	// Procedural method to reorganize position of lights
	// No parameters
	// Returns void
	// =======================================================================
	public void lightPosition() {
		// Constant integer for spacing between lights
		final int intLightSpacing = 75;
		// Adds x & y coordinates of lights
		int intBoard_x;
		int intBoard_y;
		// Tests if board size is odd
		if (intBoardSize % 2 == 1) {
			// Calculates location of x coordinate of lights
			intBoard_x = (getWidth() / 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2) * (int) Math.floor(intBoardSize / 2))
					- intLightRadius;
			// Calculates location of y coordinate of lights
			intBoard_y = (getHeight() / 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2) * (int) Math.floor(intBoardSize / 2))
					- intLightRadius;
			// Runs if board size is even
		} else {
			// Calculates location of x coordinate of lights
			intBoard_x = (getWidth() / 2) - ((intLightRadius * 2 + intLightSpacing - intLightRadius * 2)
					* (int) Math.floor(intBoardSize / 2));
			// Calculates location of y coordinate of lights
			intBoard_y = (getHeight() / 2 + intLightRadius * 2)
					- ((intLightRadius * 2 + intLightSpacing - intLightRadius) * (int) Math.floor(intBoardSize / 2));
		}
		// Counted loop for 1st and 2nd layer of array; x and y coordinates
		for (int depth = 0; depth < 2; depth++)
			// Counted loop for row of array
			for (int row = 0; row < intBoardSize; row++)
				// Counted loop for column of array
				for (int column = 0; column < intBoardSize; column++)
					// Reads y coordinates of array
					if (depth == 1)
						// Sets y coordinate of individual lights
						intLightPosition[depth][column][row] = intBoard_y + row * intLightSpacing;
					// Reads x coordinate of array
					else
						// Sets x coordinate of individual lights
						intLightPosition[depth][column][row] = intBoard_x + column * intLightSpacing;
	}

	// =======================================================================
	// boardRandomizze method
	// Procedural method to randomize the starting board layout
	// No parameters
	// Returns void
	// =======================================================================
	// Public procedural method to randomize the board correctly
	public void boardRandomize() {
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Sets all light states to false
				boolLightState[column][row] = false;
		// Board randomized 25 times before checking for number of lights on to prevent
		// easily solvable board layouts
		for (int count = 0; count < 25; count++)
			// Toggles adjacent lights to randomly selected index
			toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));
		// Tests if board size is 5
		if (intBoardSize == 5)
			// Do while loop to keep toggling lights until number of starting lights match
			do {
				// Toggles adjacent lights to randomly selected index
				toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));
				// Repeats loop if the number of starting lights are not equal to user's request
				// amount
			} while (lightsOn() != intStartingLights);
		else
			do {
				// Do while loop to keep toggling lights to prevent board from randomizing to
				// solved state
				// Toggles adjacent lights to randomly selected index
				toggleAdjacentLights((int) (Math.random() * intBoardSize), (int) (Math.random() * intBoardSize));
				// Repeats loop if the number of starting lights are not equal to user's request
				// amount
			} while (boardSolved());
		// Repaints screen
		repaint();
	}

	// =======================================================================
	// lightsOn method
	// Functional integer variable to count number of lights on
	// No parameters
	// Returns integer
	// =======================================================================
	public int lightsOn() {
		// Initializes number of lights on
		int intLightsOn = 0;
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Tests if light is on
				if (boolLightState[column][row] == true)
					// Adds number of light on by 1
					intLightsOn++;
		// Returns number of light on the board
		return intLightsOn;
	}

	// =======================================================================
	// mouseToggleLight method
	// Procedural method to toggle light based on mouse coordinates
	// Mouse x and y coordinates parameters
	// Returns void
	// =======================================================================
	public void mouseToggleLight(int x, int y) {
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Tests if mouse coordinates are inside light area
				if (insideLight(x, y, row, column)) {
					// Clears text
					boolClearText = true;
					// Number of lights toggled increased
					intLightsToggled++;
					// Toggles adjacent lights to one that is clicked
					toggleAdjacentLights(row, column);
				}
	}

	// =======================================================================
	// toggleAdjacentLights method
	// Procedural method to toggle adjacent lights
	// Board row and column parameters
	// Returns void
	// =======================================================================
	public void toggleAdjacentLights(int row, int column) {
		// Toggles light clicked
		toggleLight(row, column);
		// Tests if adjacent light is out of board size
		if (row + 1 >= 0 && row + 1 < intBoardSize)
			// Toggles light adjacent to it
			toggleLight(row + 1, column);
		// Tests if adjacent light is out of board size
		if (row - 1 >= 0 && row - 1 < intBoardSize)
			// Toggles light adjacent to it
			toggleLight(row - 1, column);
		// Tests if adjacent light is out of board size
		if (column + 1 >= 0 && column + 1 < intBoardSize)
			// Toggles light adjacent to it
			toggleLight(row, column + 1);
		// Tests if adjacent light is out of board size
		if (column - 1 >= 0 && column - 1 < intBoardSize)
			// Toggles light adjacent to it
			toggleLight(row, column - 1);
	}

	// =======================================================================
	// toggleLight method
	// Procedural method toggle specific lights
	// Board row and column parameters
	// Returns void
	// =======================================================================
	public void toggleLight(int row, int column) {
		// Toggles light state between on or off
		boolLightState[column][row] = !boolLightState[column][row];
	}

	// =======================================================================
	// mouseHoverLight method
	// Procedural method to test if mouse is hovering over light
	// Mouse x and y coordinate parameters
	// Returns void
	// =======================================================================
	public void mouseHoverLight(int x, int y) {
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Tests if mouse coordinates are hovering over light area
				if (insideLight(x, y, row, column))
					// Sets hover state to true
					boolLightHover[column][row] = true;
				else
					// Sets hover state to false
					boolLightHover[column][row] = false;
		// Repaints screen
		repaint();
	}

	// =======================================================================
	// boardSolved method
	// Functional boolean method to test if board is solved
	// No parameters
	// Returns void
	// =======================================================================
	public boolean boardSolved() {
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Tests if any lights are on
				if (boolLightState[column][row])
					// Returns false
					return false;
		// Returns true
		return true;
	}

	// =======================================================================
	// displaySolution method
	// Procedural method to change display solution array
	// No parameters
	// Returns void
	// =======================================================================
	public void displaySolution() {
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize - 1; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Tests if light is on
				if (boolLightState[column][row]) {
					// Sets solution light below to on
					boolLightSolution[column][row + 1] = true;
				} else
					// Sets solution light below to off
					boolLightSolution[column][row + 1] = false;
	}

	// =======================================================================
	// gameSolution method
	// Procedural method to pre-solve game board for solution cases
	// No parameters
	// Returns void
	// =======================================================================
	public void gameSolution() {
		// Initializes 'invisible' array
		boolean[][] boolLights = new boolean[intBoardSize][intBoardSize];
		// Counted loop for rows of board array
		for (int i = 0; i < intBoardSize; i++)
			// Copies each row of the board to the 'invisible' array
			boolLights[i] = Arrays.copyOf(boolLightState[i], boolLightState[i].length);
		// Creates two integer variables for row and column
		int toggleRow, toggleColumn;
		// Counted loop for row of array
		for (int row = 0; row < intBoardSize - 1; row++)
			// Counted loop for column of array
			for (int column = 0; column < intBoardSize; column++)
				// Tests if light state of array is on
				if (boolLights[column][row]) {
					// Sets toggled light row to one below it
					toggleRow = row + 1;
					// Sets toggled light column to same column
					toggleColumn = column;
					// Toggles light
					boolLights[toggleColumn][toggleRow] = !boolLights[toggleColumn][toggleRow];
					// Tests if adjacent lights are inside array size
					if (toggleRow + 1 >= 0 && toggleRow + 1 < intBoardSize)
						// Toggles adjacent light
						boolLights[toggleColumn][toggleRow + 1] = !boolLights[toggleColumn][toggleRow + 1];
					// Tests if adjacent lights are inside array size
					if (toggleRow - 1 >= 0 && toggleRow - 1 < intBoardSize)
						// Toggles adjacent light
						boolLights[toggleColumn][toggleRow - 1] = !boolLights[toggleColumn][toggleRow - 1];
					// Tests if adjacent lights are inside array size
					if (toggleColumn + 1 >= 0 && toggleColumn + 1 < intBoardSize)
						// Toggles adjacent light
						boolLights[toggleColumn + 1][toggleRow] = !boolLights[toggleColumn + 1][toggleRow];
					// Tests if adjacent lights are inside array size
					if (toggleColumn - 1 >= 0 && toggleColumn - 1 < intBoardSize)
						// Toggles adjacent light
						boolLights[toggleColumn - 1][toggleRow] = !boolLights[toggleColumn - 1][toggleRow];
				}
		// Tests if arrangement of lights on bottom requires more steps and board size
		// is 5
		if (lightCase(boolLights) >= 0 && intBoardSize == 5)
			// Counted loop for number of columns of array
			for (int column = 0; column < intBoardSize; column++)
				// Sets solution display array top depending on case number
				boolLightSolution[column][0] = boolLightCaseSolution[lightCase(boolLights)][column];
	}

	// =======================================================================
	// caseSolutionTop method
	// Procedural method to change array to display solution for top of board
	// Mouse x and y coordinate parameters
	// Returns void
	// =======================================================================
	public void caseSolutionTop(int x, int y) {
		// Counted loop for column of array
		for (int column = 0; column < intBoardSize; column++)
			// Tests if mouse coordinates are inside light and light state is true
			if (insideLight(x, y, 0, column) & boolLightSolution[column][0])
				// Sets light state of solution to false
				boolLightSolution[column][0] = false;
			else if (insideLight(x, y, 0, column))
				// Sets light state of solution to true
				boolLightSolution[column][0] = true;
	}

	// =======================================================================
	// lightCase method
	// Functional integer method to identify light arrangement case
	// Boolean board array parameter
	// Returns integer of case #
	// =======================================================================
	public int lightCase(boolean[][] array) {
		// Tests if board size is 5
		if (intBoardSize == 5) {
			// Creates flag boolean variable
			boolean flag;
			// Counted loop for 7 different cases
			for (int num = 0; num < 7; num++) {
				// Sets flag to true
				flag = true;
				// Counted loop for each row of array
				for (int row = 0; row < intBoardSize; row++)
					// Tests if arrangement of light does not match case arrangement
					if (boolLightCase[num][row] != array[row][intBoardSize - 1])
						// Sets flag to false
						flag = false;
				// Tests if flag is still true
				if (flag == true)
					// Returns case number
					return num;
			}
		}
		// If arrangement of lights does not match any case, return -1
		return -1;
	}

	// =======================================================================
	// insideLight method
	// Procedural method to reset variables when new game is run.
	// No parameters
	// Returns void
	// =======================================================================
	public boolean insideLight(int x, int y, int row, int column) {
		// Tests if mouse coordinates are inside specific light area
		if ((Math.sqrt(Math.pow((y - intLightPosition[1][column][row] - intLightRadius), 2)
				+ Math.pow((x - intLightPosition[0][column][row] - intLightRadius), 2))) < intLightRadius)
			// Returns true
			return true;
		else
			// Returns false
			return false;
	}

	// =======================================================================
	// createHighscore method
	// Procedural method to check to see if there is a High-Score list file
	// No parameters
	// Returns void
	// =======================================================================
	public void createHighScore() throws IOException {
		// Initializes object to create new high score text file
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

	// =======================================================================
	// readHighScore method
	// Procedural method to read a High-Score list file
	// No parameters
	// Returns void
	// =======================================================================
	public void readHighScore() throws IOException {
		// Initializes file input object to read from high score text file
		BufferedReader fileInput = new BufferedReader(new FileReader("HighScore.txt"));
		// Counted loop for each 10 ranks of array
		for (int rank = 0; rank < 10; rank++) {
			// Reads line of text file and splits it into two elements
			String[] strHighScore = fileInput.readLine().split(" ", 2);
			// Sets name in array to split text
			strHighScoreName[rank] = strHighScore[0];
			// Sets score in array to split text
			intHighScoreValue[rank] = Integer.parseInt(strHighScore[1]);
		}
		// Closes text file reading
		fileInput.close();
	}

	// =======================================================================
	// resetHighscore method
	// Procedural method to reset a High-Score list file
	// No parameters
	// Returns void
	// =======================================================================
	public void resetHighScore() throws IOException {
		// Counted loop for each rank of high score list
		for (int rank = 0; rank < 10; rank++) {
			// Sets score value of high scores to -1
			intHighScoreValue[rank] = -1;
			// Removes names from high score list
			strHighScoreName[rank] = "";
		}
		// Saves reset high score list to file
		writeHighScore();
	}

	// =======================================================================
	// writeHighscore method
	// Procedural method to write the High-Score list file
	// No parameters
	// Returns void
	// =======================================================================
	public void writeHighScore() throws IOException {
		// Instantiates object to print to high score text file
		PrintWriter fileOutput = new PrintWriter(new FileWriter("HighScore.txt"));
		// Counted loop for each rank of high score list
		for (int rank = 0; rank < 10; rank++)
			// Prints high score list of name and score to text file
			fileOutput.println(strHighScoreName[rank] + " " + intHighScoreValue[rank]);
		// Closes file writing
		fileOutput.close();
	}

	// =======================================================================
	// addHighscore method
	// Procedural method to add to the High-Score list
	// String name and integer score parameters
	// Returns void
	// =======================================================================
	public void addHighScore(String name, int score) {
		// Counted loop for each rank of high score list
		for (int rank = 0; rank < 10; rank++)
			// Tests if score is less than score on list and is not -1
			if (score < intHighScoreValue[rank] || intHighScoreValue[rank] == -1) {
				// Counted loop for each rank needed to move
				for (int moveRank = 9; moveRank > rank; moveRank--) {
					// Moves name of high score list down by one
					strHighScoreName[moveRank] = strHighScoreName[moveRank - 1];
					// moves score of high score list down by one
					intHighScoreValue[moveRank] = intHighScoreValue[moveRank - 1];
				}
				// Sets new high score name
				strHighScoreName[rank] = name;
				// Sets new high score value
				intHighScoreValue[rank] = score;
				// Breaks from counted loop
				break;
			}
	}

	// =======================================================================
	// testHighscore method
	// Functional boolean method to test if score can replace high score rank
	// Integer score parameter
	// Returns true or false
	// =======================================================================
	public boolean testHighScore(int score) throws IOException {
		// Initializes highest score of high score list
		int highestScore = intHighScoreValue[0];
		// Counted loop for each rank of high score list
		for (int rank = 1; rank < 10; rank++)
			// Tests if next score is greater than current
			if (intHighScoreValue[rank] > highestScore) {
				// Replaces highest score variable with new score
				highestScore = intHighScoreValue[rank];
				// Tests if there is no valid score
			} else if (intHighScoreValue[rank] == -1)
				// Returns true; empty space in high score list
				return true;
		// Tests if user's score is less than highest score
		if (score < highestScore)
			// Returns true
			return true;
		// Returns false
		return true;
	}

	// =======================================================================
	// playButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean playButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 190 && y >= getHeight() / 2 - 140
				&& y <= getHeight() / 2 - 60)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// optionsButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean optionsButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 295 && y >= getHeight() / 2 - 40
				&& y <= getHeight() / 2 + 40)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// highScoresButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean highScoresButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 405 && y >= getHeight() / 2 + 60
				&& y <= getHeight() / 2 + 140)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// startingLightsButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean startingLightsButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 40 && x <= getWidth() / 2 + 500 && y >= getHeight() / 2 - 135
				&& y <= getHeight() / 2 - 60)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// boardSizeButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean boardSizeButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 40 && x <= getWidth() / 2 + 375 && y >= getHeight() / 2 - 35
				&& y <= getHeight() / 2 + 30)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// boardSkinsButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean boardSkinsButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 35 && x <= getWidth() / 2 + 190 && y >= getHeight() / 2 - 140
				&& y <= getHeight() / 2 - 60)
			// Returns true
			return true;
		// Returns  false
		return false;
	}

	// =======================================================================
	// backButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean backButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 - 115 & x <= getWidth() / 2 - 30 && y >= getHeight() / 2 + 90
				&& y <= getHeight() / 2 + 120)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// highScoresButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean highScoresBackButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 - 340 && x <= getWidth() / 2 - 255 && y >= getHeight() / 2 + 135
				&& y <= getHeight() / 2 + 165)
			// Returns truee
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// decreaseButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean decreaseButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 70 && x <= getWidth() / 2 + 100 && y >= getHeight() / 2 - 30
				&& y <= getHeight() / 2 + 15)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// increaseButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean increaseButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 320 && x <= getWidth() / 2 + 350 && y >= getHeight() / 2 - 30
				&& y <= getHeight() / 2 + 15)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// showSolutionButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean showSolutionButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 260 && x <= getWidth() / 2 + 435 && y >= getHeight() / 2 - 45
				&& y <= getHeight() / 2 + 55)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// backToMenuButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean backToMenuButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 - 450 && x <= getWidth() / 2 - 270 && y >= getHeight() / 2 - 45
				&& y <= getHeight() / 2 + 55)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// newGameButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean newGameButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 25 && x <= getWidth() / 2 + 394 && y >= getHeight() / 2 - 150
				&& y <= getHeight() / 2 - 59)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// previousGameButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	// Functional boolean method with x and y mouse coordinate parameters.
	public boolean previousGameButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 25 && x <= getWidth() / 2 + 516 && y >= getHeight() / 2 - 50
				&& y <= getHeight() / 2 + 45)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// menuButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean menuButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 25 && x <= getWidth() / 2 + 225 && y >= getHeight() / 2 + 50
				&& y <= getHeight() / 2 + 139)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// randomizeButton method
	// Boolean functional method to check if mouse
	// coordinates are inside button area
	// Mouse x and y coordinates parameters
	// Returns true or false if mouse is inside area
	// =======================================================================
	public boolean randomizeButton(int x, int y) {
		// Tests if mouse coordinates are inside button area
		if (x >= getWidth() / 2 + 87 && x <= getWidth() / 2 + 338 && y >= getHeight() / 2 + 70
				&& y <= getHeight() / 2 + 135)
			// Returns true
			return true;
		// Returns false
		return false;
	}

	// =======================================================================
	// resetButton method
	// Procedural functional method to reset button states
	// No parameters
	// Returns void
	// =======================================================================
	public void resetButtons() {
		// Resets button state to false/off
		boolPlayButtonState = boolOptionsButtonState = boolHighScoresButtonState = boolBackButtonState = boolHighScoresBackButtonState = boolStartingLightsButtonState = boolBoardSizeButtonState = boolIncreaseButtonState = boolDecreaseButtonState = boolBackToMenuButtonState = boolRandomizeButtonState = boolPreviousGameButtonState = boolNewGameButtonState = false;
	}

}