package environment;

import java.util.Arrays;

import action.IllegalActionException;
import common.SimpleTools;

/**
 * The environment of tic-tac-toe.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 20, 2020.<br>
 *         Last modified: August 21, 2020.
 * @version 1.1
 */

public class TicTacToe extends CompetitionEnvironment {


	/**
	 * Number of pieces. If it reaches the size of the checkerboard, the game
	 * should be over. int numPieces;
	 */

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public TicTacToe() {
		checkerboardSize = 3;
		checkerboard = new int[checkerboardSize][checkerboardSize];

		// 9 positions, each with three possibilities: EMPTY, WHITE, BLACK.
		// So the total is 3^9. Many of them are unreachable.
		// But this is the simplest coding, which is appropriate for very small
		// sized checkboard.
		numStates = 27 * 27 * 27;
		
		numActions = 9;

		reset();
	}// Of the first constructor

	/**
	 ****************** 
	 * Reset the game.
	 ****************** 
	 */
	public void reset() {
		//SimpleTools.processTrackingOutput("Resetting TicTacToe.");
		for (int i = 0; i < checkerboard.length; i++) {
			for (int j = 0; j < checkerboard[0].length; j++) {
				checkerboard[i][j] = EMPTY;
			} // Of for j
		} // Of for i

		// White first
		currentPlayer = FIRST;
	}// Of the first constructor

	/**
	 ****************** 
	 * Get the game situation.
	 * 
	 * @return The game situation.
	 ****************** 
	 */
	public int getGameSituation() {
		return gameSituation;
	}// Of getGameSituation

	/**
	 ****************** 
	 * Is the current state a final state?
	 * 
	 * @return True if it is.
	 ****************** 
	 */
	public boolean isFinished() {
		if (gameSituation == UNFINISHED) {
			return false;
		} // Of if

		return true;
	}// Of isFinished

	/**
	 ****************** 
	 * Get the winner.
	 * 
	 * @return The game situation.
	 * @throws Exception if the game is unfinished.
	 ****************** 
	 */
	public int getWinner() throws Exception{
		if (gameSituation == UNFINISHED) {
			throw new Exception("Game unfinished yet, cannot get winner.");
		}//Of if
		
		winner = gameSituation;
		return winner;
	}// Of getWinner
	
	/**
	 ****************** 
	 * Get valid actions of the given state.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The actions.
	 ****************** 
	 */
	public int[] getValidActions(int paraState) {
		//Step 1. Construct the checkerboard.
		int[][] tempCheckerboard = stateToCheckerboard(paraState);
		int[] tempValidActions = new int[checkerboardSize * checkerboardSize];
		int tempNumValidActions = 0;

		// Step 1. Check all states.
		for (int i = 0; i < checkerboardSize; i++) {
			for (int j = 0; j < checkerboardSize; j++) {
				if (tempCheckerboard[i][j] == EMPTY) {
					tempValidActions[tempNumValidActions] = i * checkerboardSize + j;
					tempNumValidActions ++;
				}//Of if
			}//Of for j
		}//Of for i
		
		//Step 2. Compress.
		int[] resultActions = new int[tempNumValidActions];
		for (int i = 0; i < tempNumValidActions; i++) {
			resultActions[i] = tempValidActions[i];
		}//Of for i
		
		//SimpleTools.variableTrackingOutput("Valid actions: " + Arrays.toString(resultActions));

		return resultActions;
	}// Of getValidActions
	
	/**
	 ****************** 
	 * Get state reward value.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The reward value for the state.
	 ****************** 
	 */
	public int getStateRewardValue(int paraState) {
		int tempSituation = currentGameSituation(paraState);
		int tempValue = -1;
		switch (tempSituation) {
		case WIN:
			tempValue = 1;
			break;
		case LOSE:
			tempValue = -1;
			break;
		case TIE:
			tempValue = 0;
			break;
		case UNFINISHED:
			tempValue = 0;
			break;
		default:
			System.out.println("Illegal game situation: " + tempValue);
			System.exit(0);
		}// Of switch

		return tempValue;
	}// Of getStateRewardValue

	/**
	 ****************** 
	 * Go one step with the given action.
	 * 
	 * @param paraAction
	 *            The given action. It is the position chosen by the player.
	 * @return Whether or not the process comes an end.
	 ****************** 
	 */
	public void step(int paraAction) throws IllegalActionException {
		// Step 1. Is this action legal?
		int tempRow = paraAction / checkerboardSize;
		int tempColumn = paraAction % checkerboardSize;
		if (checkerboard[tempRow][tempColumn] != EMPTY) {
			System.out.println("The checkerboard state is: " + Arrays.deepToString(checkerboard));
			throw new IllegalActionException("The position (" + tempRow + ", " + tempColumn
					+ " is already occupied " + checkerboard[tempRow][tempColumn]);
		} // Of if

		// Step 2. Change the state of the checkerboard.
		SimpleTools.variableTrackingOutput("Assign " + currentPlayer + " to (" + tempRow
				+ ", " + tempColumn + ").");
		checkerboard[tempRow][tempColumn] = currentPlayer;
		currentState = checkerboardToState(checkerboard);
		
		gameSituation = currentGameSituation();
		//SimpleTools.variableTrackingOutput("Game situation " + gameSituation);
		switch(gameSituation) {
		case WIN:
			currentReward = REWARD_VALUE;
			break;
		case LOSE:
			currentReward = PENALTY_VALUE;
			break;
		default:
			currentReward = 0;
		}//Of switch

		// Step 3. Now it's the turn of the other player.
		if (currentPlayer == FIRST) {
			currentPlayer = SECOND;
		} else {
			currentPlayer = FIRST;
		} // Of if
		
		//SimpleTools.variableTrackingOutput("Now it is the turn of player " + currentPlayer);
	}// Of step

	/**
	 ****************** 
	 * Convert as state to a checkerboard.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The checkerboard.
	 ****************** 
	 */
	private int[][] stateToCheckerboard(int paraState) {
		int[][] resultCheckerboard = new int[checkerboardSize][checkerboardSize];
		int tempValue = paraState;

		for (int i = 0; i < checkerboardSize; i++) {
			for (int j = 0; j < checkerboardSize; j++) {
				resultCheckerboard[i][j] = (tempValue % checkerboardSize);
				tempValue /= checkerboardSize;
			} // Of for j
		} // Of for i

		return resultCheckerboard;
	}// Of stateToCheckerboard

	/**
	 ****************** 
	 * Convert a checkerboard to a state.
	 * 
	 * @param paraCheckerboard
	 *            The given checkerboard.
	 * @return The state.
	 ****************** 
	 */
	private int checkerboardToState(int[][] paraCheckerboard) {
		int resultState = 0;
		int tempExponential = 1;
		for (int i = 0; i < checkerboardSize; i++) {
			for (int j = 0; j < checkerboardSize; j++) {
				resultState += paraCheckerboard[i][j] * tempExponential;
				tempExponential *= checkerboardSize;
			} // Of for j
		} // Of for i

		return resultState;
	}// Of stateToCheckerboard

	/**
	 ****************** 
	 * Determine current game situation for white: WIN, LOSE, TIE, or
	 * UNFINISHED.
	 * 
	 * @return The situation.
	 ****************** 
	 */
	public int currentGameSituation() {
		return currentGameSituation(checkerboard);
	}// Of currentGameSituation

	/**
	 ****************** 
	 * Determine current game situation for white: WIN, LOSE, TIE, or
	 * UNFINISHED.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The situation.
	 ****************** 
	 */
	public int currentGameSituation(int paraState) {
		int[][] tempCheckerboard = stateToCheckerboard(paraState);
		//System.out.println("The checkerboard is: " + Arrays.deepToString(tempCheckerboard));

		return currentGameSituation(tempCheckerboard);
	}// Of currentGameSituation

	/**
	 ****************** 
	 * Determine the given game situation for white: WIN, LOSE, TIE, or
	 * UNFINISHED.
	 * 
	 * @param paraCheckerboard
	 *            The given checkerboard.
	 * @return The situation
	 ****************** 
	 */
	public int currentGameSituation(int[][] paraCheckerboard) {
		// Indicate whether or not the row/column/slope is the same.
		boolean tempSame = false;

		// Step 1. Horizontal
		for (int i = 0; i < checkerboardSize; i++) {
			if (paraCheckerboard[i][0] == EMPTY) {
				continue;
			} // Of if

			// A whole row is the same.
			tempSame = true;
			for (int j = 1; j < checkerboardSize; j++) {
				if (paraCheckerboard[i][j] != paraCheckerboard[i][0]) {
					tempSame = false;
					break;
				} // Of if
			} // Of for j

			if (tempSame) {
				if (paraCheckerboard[i][0] == FIRST) {
					return WIN;
				} else {
					return LOSE;
				} // Of if
			} // Of if
		} // Of for i

		// Step 2. Vertical
		for (int j = 0; j < checkerboardSize; j++) {
			if (paraCheckerboard[0][j] == EMPTY) {
				continue;
			} // Of if

			// A whole column is the same.
			tempSame = true;
			for (int i = 1; i < checkerboardSize; i++) {
				if (paraCheckerboard[i][j] != paraCheckerboard[0][j]) {
					tempSame = false;
					break;
				} // Of if
			} // Of for i

			if (tempSame) {
				if (paraCheckerboard[0][j] == FIRST) {
					return WIN;
				} else {
					return LOSE;
				} // Of if
			} // Of if
		} // Of for i

		// Step 3. Slope: Left-top to right-bottom.
		if (paraCheckerboard[0][0] != EMPTY) {
			tempSame = true;
			for (int i = 1; i < checkerboardSize; i++) {
				if (paraCheckerboard[i][i] != paraCheckerboard[0][0]) {
					tempSame = false;
					break;
				} // Of if
			} // Of for i

			if (tempSame) {
				if (paraCheckerboard[0][0] == FIRST) {
					return WIN;
				} else {
					return LOSE;
				} // Of if
			} // Of if
		} // Of if

		// Step 4. Slope: Right-top to left-bottom.
		if (paraCheckerboard[0][checkerboardSize - 1] != EMPTY) {
			for (int i = 1; i < checkerboardSize; i++) {
				tempSame = true;
				if (paraCheckerboard[i][checkerboardSize - i
						- 1] != paraCheckerboard[0][checkerboardSize - 1]) {
					tempSame = false;
					break;
				} // Of if
			} // Of for i

			if (tempSame) {
				if (paraCheckerboard[0][checkerboardSize - 1] == FIRST) {
					return WIN;
				} else {
					return LOSE;
				} // Of if
			} // Of if
		} // Of if

		// Step 5. Checkboard full
		boolean tempHasEmpty = false;
		for (int i = 0; i < paraCheckerboard.length; i++) {
			for (int j = 0; j < paraCheckerboard.length; j++) {
				if (paraCheckerboard[i][j] == EMPTY) {
					tempHasEmpty = true;
					break;
				} // Of if
			} // Of for j

			if (tempHasEmpty) {
				break;
			} // Of if
		} // Of for i
		if (!tempHasEmpty) {
			return TIE;
		} // Of if

		return UNFINISHED;
	}// Of currentGameSituation

	/**
	 ****************** 
	 * Test two methods.
	 ****************** 
	 */
	public static void stateCheckerboardConvertionTest() {
		TicTacToe tempEnvironment = new TicTacToe();
		int[] tempTestStates = { 0, 13, 39, 26, 6643 };

		int tempState;
		int[][] tempCheckerboard;
		for (int i = 0; i < tempTestStates.length; i++) {
			tempCheckerboard = tempEnvironment.stateToCheckerboard(tempTestStates[i]);
			System.out.println("The checkerboard of " + tempTestStates[i] + " is: "
					+ Arrays.deepToString(tempCheckerboard));
			tempState = tempEnvironment.checkerboardToState(tempCheckerboard);
			System.out.println("The state back is " + tempState);
		} // Of for i

	}// Of stateCheckerboardConvertionTest

	/**
	 ****************** 
	 * Test a method.
	 ****************** 
	 */
	public static void currentGameSituationTest() {
		TicTacToe tempEnvironment = new TicTacToe();
		int[] tempTestStates = { 0, 13, 39, 26, 6643 };

		int tempResult;
		for (int i = 0; i < tempTestStates.length; i++) {
			tempResult = tempEnvironment.currentGameSituation(tempTestStates[i]);
			System.out.println("The result of " + tempTestStates[i] + " is: " + tempResult);
		} // Of for i
	}// Of currentGameSituationTest

	/**
	 ****************** 
	 * Test a method.
	 ****************** 
	 */
	public static void getValidActionsTest() {
		TicTacToe tempEnvironment = new TicTacToe();
		int[] tempTestStates = { 0, 13, 39, 26, 6643 };

		int[] tempResultArray;
		for (int i = 0; i < tempTestStates.length; i++) {
			tempResultArray = tempEnvironment.getValidActions(tempTestStates[i]);
			System.out.println("The result of " + tempTestStates[i] + " is: " + Arrays.toString(tempResultArray));
		} // Of for i
	}// Of getValidActionsTest	
	
	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "\r\nCheckerboard state: " + Arrays.deepToString(checkerboard);
		resultString += "\r\nThe current situation is: " + gameSituation;
		return resultString;
	} // Of toString

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		System.out.println("\r\ncurrentGameSituationTest()");
		currentGameSituationTest();

		System.out.println("\r\nstateCheckerboardConvertionTest()");
		stateCheckerboardConvertionTest();
		
		System.out.println("\r\ngetValidActionsTest()");
		getValidActionsTest();
	}// Of main
} // Of class TicTacToe
