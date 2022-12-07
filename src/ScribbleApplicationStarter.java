
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScribbleApplicationStarter {

	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static FileOperations fileOperations = new FileOperations();
	// To store List of all Players in a game
	private List<Player> playersInGame = new ArrayList<>();
	// To keep record which words are already used
	private Set<String> alreadyUsedWords = new HashSet<>();
	// To keep record of used grids
	private Set<Grid> gridsUsed = new HashSet<>();
	// Flag to identify game is active or not
	private Boolean isGameActive = Boolean.TRUE;

	// This method is entry point to start game
	public void start() {

		try {
			startMenu();
		} catch (IOException e) {
			System.err.println("ERROR: While starting application--" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("ERROR: While starting application--" + e.getMessage());
			e.printStackTrace();
		}

	}

	// This method is controller method for main menu
	private void startMenu() throws IOException {
		System.out.println(ScribbleConstants.APPLICATION_NAME);
		while (true) {
			isGameActive = Boolean.TRUE;
			System.out.println(ScribbleConstants.APPLICATION_MENU_OPTION);
			String choice = reader.readLine();

			if ("Q".equalsIgnoreCase(choice)) {
				System.out.println("!!!!!!!THANK YOU!!!!!!");
				break;
			}

			if ("1".equalsIgnoreCase(choice)) {
				System.out.println("!!!!!!!!!!GAME IS IN PROGRESS!!!!!!!!!!!!");
				playersInGame = new ArrayList<>();
				gridsUsed = new HashSet<>();
				alreadyUsedWords = new HashSet<>();
				startGame();
			} else if ("2".equalsIgnoreCase(choice)) {
				System.out.println("!!!!!!!!!!Saved Games Menu!!!!!!!!!!!!");
				loadSavedGames();
			} else if ("3".equalsIgnoreCase(choice)) {
				System.out.println("!!!!!!!!!!Help\\Instruction!!!!!!!!!!!!");
				helpInstructionsMenu();
			} else {
				System.err.println("ERROR: INVALID choice");
			}

		}

	}

	// Method to be executed when choice is 1 entered by user
	private void startGame() throws IOException {

		while (isGameActive && true) {
			if (!isGameActive) {
				break;
			}
			// Asking for no of players
			System.out.println("Enter number of player(2 or 3 or 4):");
			String noOfPlayer = reader.readLine();
			if ("2".equalsIgnoreCase(noOfPlayer) || "3".equalsIgnoreCase(noOfPlayer)
					|| "4".equalsIgnoreCase(noOfPlayer)) {

				// Executed when number of player mentioned is 2 or 3 or 4.
				// Setting initial state of game board
				char[][] gameBoardInitialState = setGameBoardInitialState();
				// Setting player Details
				setPlayerDetails(Integer.parseInt(noOfPlayer));
				// Display Game state to console
				displayGameBoardValues(gameBoardInitialState);
				// Starting player's turn
				playerStartPlay(gameBoardInitialState);

			} else {
				// In case number of players mentioned is not 2 or 3 or 4.
				System.err.println("ERROR: Invalid number of player");
				// startGame();
			}
		}

	}

	private void playerStartPlay(char[][] gameBoardInitialState) throws IOException {
		String wordByPlayer = "";
		String loc = "";
		String directionToMove = "";
		while (isGameActive && true) {
			enterWordLoop:
			for (Player player : playersInGame) {
				
				while (isGameActive && true) {
					// Checking for termination condition
					if (isTerminated(playersInGame, gridsUsed)) {
						terminateGame(playersInGame);
						isGameActive = Boolean.FALSE;
						break;
					}
					// Asking for player choice in game.
					System.out.println("\n\n" + player.getPlayerName() + "[" + player.getPlayerScore() + "]"
							+ "TURN\nEnter Word(Two or More alphabets) or q to QUIT or s to SAVE:");
					wordByPlayer = reader.readLine().toLowerCase();

					// Block to execute if player opted to SAVE the game
					if ("s".equalsIgnoreCase(wordByPlayer)) {
						// Code to QUIT game
						// display winner name
						System.err.println("!!!SAVING GAME: Stared........");
						String header = createHeaderWithPlayerNameWithScore(playersInGame);
						String bodyToWrite = createGameStateInBodyForFile(gameBoardInitialState);
						String contentToWriteInFileString = header.concat("\n").concat(bodyToWrite);
						fileOperations.writeIntoFile(contentToWriteInFileString);
						System.err.println("!!!SAVING GAME: Done........");
						isGameActive = Boolean.FALSE;
						break;

					}

					// Block to execute if player opted to QUIT the game
					if ("q".equalsIgnoreCase(wordByPlayer)) {
						// Code to save game current state
						System.out.println("Quitting.....");
						isGameActive = Boolean.FALSE;
						terminateGame(playersInGame);
						break;

					}

					// Executed when player entered word with at least 2 alphabets and is not in
					// used words, Moving to next step
					if (wordByPlayer.trim().length() >= 2 && !alreadyUsedWords.contains(wordByPlayer)) {
						break;
					} else {
						// User entered number is either of shorter length or is in used words
						System.err.println(
								"WARNING: Word should have atleast two characters or you entered already used word.");
					}
				}

				while (isGameActive && true) {
					// Asking direction to align words.
					Boolean isWordPlaced = Boolean.FALSE;
					// Asking user where to start placing words.
					System.out.println("Enter Location(e.g. 3,4) :");
					loc = reader.readLine();
					if(loc.isEmpty()) {
						System.out.println("Location can't be blank");
						continue;
					}
					String[] locChar = loc.trim().split(",");
					int row = Integer.parseInt(locChar[0].trim());
					int col = Integer.parseInt(locChar[1].trim());
					// Verifying that specified loc is EMPTY and within the game board dimensions
					if (loc.isEmpty() || row < 0 || row > 14 || col < 0 || col > 14) {
						continue;
					}
					System.out.println(
							"Align Word\nN or n towards NORTH\nS or s towards SOUTH\nE or e towards EAST\nW or w towards WEST :");
					directionToMove = reader.readLine();
					if (directionToMove.isEmpty()) {
						continue;
					}
					boolean isWordLengthValid = verifyWordSize(wordByPlayer, row, col, directionToMove);
					if (isGameActive && isWordLengthValid) {
						if ("N".equalsIgnoreCase(directionToMove)) {
							isWordPlaced = isWordPlaced(gameBoardInitialState, wordByPlayer, loc, -1, Boolean.TRUE);
						} else if ("S".equalsIgnoreCase(directionToMove)) {
							isWordPlaced = isWordPlaced(gameBoardInitialState, wordByPlayer, loc, 1, Boolean.TRUE);
						} else if ("E".equalsIgnoreCase(directionToMove)) {
							isWordPlaced = isWordPlaced(gameBoardInitialState, wordByPlayer, loc, 1, Boolean.FALSE);
						} else if ("W".equalsIgnoreCase(directionToMove)) {
							isWordPlaced = isWordPlaced(gameBoardInitialState, wordByPlayer, loc, -1, Boolean.FALSE);
						} else {
							System.out.println("ERROR: INVALID DIRECTION");

						}
					}
					else {
						System.out.println("INFO: Either game is not active or Word length can't be accommodated!!!");
						break enterWordLoop;
					}
					if (isWordPlaced) {
						// Executed if word is placed successfully
						// Showing the updated game board
						displayGameBoardValues(gameBoardInitialState);
						// Counting player score
						countScoreForPlayer(wordByPlayer, player);
						// Display player score
						System.out.println("\n!!!CURRENT SCORE BOARD!!!");
						displayScoreForAllPlayers(playersInGame);
						// Adding word into already used category
						alreadyUsedWords.add(wordByPlayer);
						break;
					}
				}

			}

		}

	}

	private boolean verifyWordSize(String wordByPlayer, int row, int col, String directionToMove) {
		boolean isWordLengthValid = false;
		int availableCellsSize = 0;
		if ("N".equalsIgnoreCase(directionToMove)) {
			availableCellsSize = row+1;
			if(wordByPlayer.length() <= availableCellsSize) {
				isWordLengthValid = true;
			}
		} else if ("S".equalsIgnoreCase(directionToMove)) {
			availableCellsSize = 15 - col;
			if(wordByPlayer.length() <= availableCellsSize) {
				isWordLengthValid = true;
			}
		} else if ("E".equalsIgnoreCase(directionToMove)) {
			availableCellsSize = 15 - col;
			if(wordByPlayer.length() <= availableCellsSize) {
				isWordLengthValid = true;
			}
		} else if ("W".equalsIgnoreCase(directionToMove)) {
			availableCellsSize = row+1;
			if(wordByPlayer.length() <= availableCellsSize) {
				isWordLengthValid = true;
			}
		} 
		
		
		
		
		return isWordLengthValid;
	}

	// This method checks if termination condition is fulfilled or not
	private boolean isTerminated(List<Player> playersInGame, Set<Grid> gridsUsed) {
		boolean isGameOver = false;

		for (Player player : playersInGame) {

			if (player.getPlayerScore() >= 100 || gridsUsed.size() == 225) {
				isGameOver = true;
				break;
			}
		}

		return isGameOver;
	}

	// This method performs action to do if game is to be terminated
	private void terminateGame(List<Player> playersInGame) {
		System.out.println("!!FINAL SCORE BOARD!!");
		System.out.println("Player Name : Score");
		// Display score for all players
		displayScoreForAllPlayers(playersInGame);
		// Display WINNER
		displayWinnerPlayerNameAndScore(playersInGame);

	}

	// This method is responsible to display WINNER
	private void displayWinnerPlayerNameAndScore(List<Player> playersInGame) {
		for (int i = 0; i < playersInGame.size(); i++) {
			for (int j = i + 1; j < playersInGame.size(); j++) {
				if (playersInGame.get(i).getPlayerScore() < playersInGame.get(j).getPlayerScore()) {
					Player temp = playersInGame.get(i);
					playersInGame.set(i, playersInGame.get(j));
					playersInGame.set(j, temp);
				}
			}
		}
		if (playersInGame.get(0).getPlayerScore() == 0) {
			System.out.println("!!Can't be DECIDED!!");
		} else {

			System.out.println("WINNER: " + playersInGame.get(0).getPlayerName());
		}

	}

	// This method displays all player Scores
	private void displayScoreForAllPlayers(List<Player> playersInGame) {
		for (Player player : playersInGame) {

			System.out.println(player.getPlayerName() + " : " + player.getPlayerScore());
		}

	}

	// Created board state in String form to save into file
	private String createGameStateInBodyForFile(char[][] gameBoardInitialState) {
		// Code to create game board state to write into file.
		String formattedBodyToReturn = "";
		for (char[] row : gameBoardInitialState) {
			for (int i = 0; i < row.length; i++) {
				String formattedValue = "";
				if (i == (row.length - 1)) {

					formattedBodyToReturn += formattedValue.concat(String.valueOf(row[i])).concat("\n");

				} else {

					formattedBodyToReturn += formattedValue.concat(String.valueOf(row[i])).concat(",");
				}

			}
		}

		return formattedBodyToReturn;
	}

	// It created player with score details in order to save player state
	private String createHeaderWithPlayerNameWithScore(List<Player> playersInGame) {
		String headerStringToReturn = "";
		for (int i = 0; i < playersInGame.size(); i++) {
			Player player = playersInGame.get(i);
			String currentString = "";
			if (i == (playersInGame.size() - 1)) {
				headerStringToReturn += currentString.concat(player.getPlayerName()).concat("_")
						.concat(String.valueOf(player.getPlayerScore()));
			} else {
				headerStringToReturn += currentString.concat(player.getPlayerName()).concat("_")
						.concat(String.valueOf(player.getPlayerScore())).concat(",");
			}
		}
		return headerStringToReturn;
	}

	// This method is used to place word on Game Board
	private boolean isWordPlaced(char[][] gameBoardInitialState, String wordByPlayer, String loc, int stepSize,
			Boolean changeRowValue) {
		Boolean wordPlaced = Boolean.FALSE;
		String[] locChar = loc.trim().split(",");
		int row = Integer.parseInt(locChar[0].trim());
		int col = Integer.parseInt(locChar[1].trim());
		int initialRowValue = row;
		int initialColValue = col;
		for (char wordChar : wordByPlayer.toLowerCase().toCharArray()) {

			if (isValidMove(gameBoardInitialState, wordChar, row, col)) {
				gameBoardInitialState[row][col] = wordChar;
				if (changeRowValue) {
					row += stepSize;
				} else {
					col += stepSize;
				}
				gridsUsed.add(new Grid(row, col));
				wordPlaced = Boolean.TRUE;
			} else {
				System.err.println("ERROR: Word Cann't be placed in specified location");
				int finalrowValue = row;
				int finalColValue = col;
				resetUpdatedValues(gameBoardInitialState,initialRowValue, finalrowValue, initialColValue, finalColValue);
				wordPlaced = Boolean.FALSE;
				break;
			}
		}
		return wordPlaced;

	}

	private void resetUpdatedValues(char[][] gameBoardInitialState, int initialRowValue, int finalRowValue,
			int initialColValue, int finalColValue) {
		if (initialColValue == finalColValue) {
			for (int i = initialColValue; i<finalColValue; i++) {
				gameBoardInitialState[initialRowValue][i] = ' '; 
			}
		}else {
			for (int i = initialRowValue; i<finalRowValue; i++) {
				gameBoardInitialState[i][initialColValue] = ' '; 
			}
		}
		
	}

	// This method verified that the grid position is valid or not
	private boolean isValidMove(char[][] gameBoardInitialState, char wordChar, int row, int col) {
		Boolean isLocValid = Boolean.FALSE;
		if (row >= 0 && row <= 14 && col >= 0 && col <= 14) {
			char currentBoardElement = gameBoardInitialState[row][col];

			if (currentBoardElement == ' ' || currentBoardElement == wordChar) {
				isLocValid = Boolean.TRUE;
			}

		}
		return isLocValid;
	}

	// This counts the score for a word placed by player
	private void countScoreForPlayer(String wordByPlayer, Player player) {
		for (char wordChar : wordByPlayer.toCharArray()) {
			int pointValue = ScribbleConstants.CHARACTER_SCORE_VALUE.get(wordChar);
			int currentScore = player.getPlayerScore();
			currentScore += pointValue;
			player.setPlayerScore(currentScore);
		}

	}

	// This displays the Game board on console
	public void displayGameBoardValues(char[][] gameBoard) {
		for (int i = 0; i < 15; i++) {
			if (i <= 9) {
				System.out.print(" " + i + " ");
			} else {
				System.out.print(i + " ");
			}
			for (int j = 0; j < 15; j++) {
				String val = String.valueOf(gameBoard[i][j]).trim();
				if (i == 0) {
					System.out.print("|" + j + val);
				} else {
					if (j <= 9) {

						System.out.print("|" + " " + val);
					} else {
						System.out.print("|" + "  " + val);
					}
				}
				System.out.print("__");
			}
			System.out.print("|\n");

		}
	}

	// It created the initial game board of 15X15 with ' ' as values
	public char[][] setGameBoardInitialState() {
		// Creating char array of 15X15 with ' ' as initial value to all elements
		char[][] gameBoard = new char[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				gameBoard[i][j] = ' ';
			}
		}
		return gameBoard;
	}

	// It created details of players, when new game is created
	private void setPlayerDetails(Integer noOfPlayer) throws IOException {
		for (int i = 1; i <= noOfPlayer; i++) {
			Integer playerId = i;
			System.out.println("Enter Player " + i + " name:");
			String name = reader.readLine();
			if(name.isEmpty()) {
				
				name = "Player "+playerId;
				System.out.println("For Player "+i+" name is set as "+name );
			}
			Integer score = 0;
			Player player = new Player(playerId, name, score);
			playersInGame.add(player);
		}

	}

	// This method is executed when user enters to load saved game
	public void loadSavedGames() throws IOException {

		// Reading last saved game from file
		String savedGames = fileOperations.readFromFile();
		String[] savedGame = savedGames.split("\n");
		// Getting player details
		playersInGame = getSavedPlayerDetails(savedGame);
		// Creating board with saved details
		char[][] gameBoardSavedState = reloadSavedGameBoard(savedGame);
		// Displaying the game board
		displayGameBoardValues(gameBoardSavedState);
		System.out.println("\n!! Score Board !! \n");
		displayScoreForAllPlayers(playersInGame);
		// Starting the saved game
		playerStartPlay(gameBoardSavedState);

	}

	// Creating game board with saved state
	private char[][] reloadSavedGameBoard(String[] savedGame) {
		char[][] gameBoard = new char[15][15];
		int rowNum = 1;
		for (int i = 0; i < 15; i++) {
			while (rowNum < savedGame.length) {
				String[] row = savedGame[rowNum].split(",");

				for (int k = 0; k < 15; k++) {
					gameBoard[i][k] = row[k].charAt(0);
				}

				rowNum++;
				break;
			}
		}
		return gameBoard;
	}

	// Creating saved player details when user ask to load saved games
	private List<Player> getSavedPlayerDetails(String[] savedGame) {
		String[] playerDetails = savedGame[0].split(",");
		playersInGame = new ArrayList<>();
		int playerCount = 1;
		for (String playerDetail : playerDetails) {
			String[] playerNameAndScore = playerDetail.split("_");
			Player player = new Player(playerCount, playerNameAndScore[0], Integer.parseInt(playerNameAndScore[1]));
			playersInGame.add(player);
			playerCount++;

		}
		return playersInGame;
	}

	// It executes when user want to know about game
	private void helpInstructionsMenu() throws IOException {
		System.out.println("!!! SCRIBBLE !!!");

		System.out.println("ABOUT");

		System.out.println(
				"Scrabble is a word game in which two to four players score points by placing tiles, each bearing a single letter,\nonto a game board divided into a 15×15 grid of squares. "
						+ "\nThe tiles must form words that, in crossword fashion, read left to right in rows or downward in columns \nand are included in a standard dictionary or lexicon.");

		System.err.println("RULES:");

		System.out.println(
				"1. It is played between 2 or 3 or 4 players.\n2. Player will get turn in order they have joined the game."
						+ "\n3. Player need to specify word they want to use.\n4. Also player need to mention location in format row,column(e.g., 4,5)."
						+ "\n5. Player who score 100 first will be winner.\n6. If any player QUITS, player with highest score will be winner.");

		System.err.println("\nEnter b to got back to Main Menu");

		if ("b".equalsIgnoreCase(reader.readLine())) {

		} else {
			helpInstructionsMenu();
		}

	}

}
