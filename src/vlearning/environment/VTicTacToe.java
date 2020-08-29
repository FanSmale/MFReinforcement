package vlearning.environment;

import java.util.Arrays;
import java.util.Random;

import qlearning.action.IllegalActionException;
import common.SimpleTools;

/**
 * The environment of tic-tac-toe for V-learning.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 20, 2020.<br>
 *         Last modified: August 21, 2020.
 * @version 1.1
 */

public class VTicTacToe {

	/**
	 * It is employed to indicate the state of the location (no piece).
	 */
	public static final int EMPTY = 0;

	/**
	 * It is employed to indicate the state of the location, and the winner of
	 * the game.
	 */
	public static final int WHITE = 1;

	/**
	 * It is employed to indicate the state of the location, and the winner of
	 * the game.
	 */
	public static final int BLACK = 2;

	/**
	 * Game situation: tie.
	 */
	public static final int TIE = 0;

	/**
	 * Game situation: unfinished.
	 */
	public static final int UNFINISHED = 3;
	
	/**
	 * The checkerboard size, often 3. Maybe more in the future.
	 */
	public int checkerboardSize;

	/**
	 * The current checkerboard.
	 * @see #currentState
	 */
	public int[][] checkerboard;

	/**
	 * The current state of the environment. Synchronize with checkerboard.
	 * @see #checkerboard
	 */
	int currentState;

	/**
	 * Game situation.
	 */
	int gameSituation;

	/**
	 * Whose turn.
	 */
	int currentPlayer;

	/**
	 * Number of states.
	 */
	int numStates;

	/**
	 * Number of actions.
	 */
	int numActions;

	/**
	 * The current route states.
	 */
	int[] currentRouteStates;

	/**
	 * Current route length.
	 */
	int currentRouteLength;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public VTicTacToe() {
		checkerboardSize = 3;
		checkerboard = new int[checkerboardSize][checkerboardSize];

		// 9 positions, each with three possibilities: EMPTY, WHITE, BLACK.
		// So the total is 3^9. Many of them are unreachable.
		// But this is the simplest coding, which is appropriate for very small
		// sized checkboard.
		numStates = 27 * 27 * 27;

		numActions = 9;
		currentRouteStates = new int[numActions + 1];
		
		reset();
	}// Of the first constructor

	/**
	 ****************** 
	 * Is the state available?
	 * Just a stub here.
	 ****************** 
	 */
	public boolean isStateAvailable(int paraState) {
		return true;
	}// Of isStateAvailable
	
	/**
	 ****************** 
	 * Getter.
	 * Just a stub here.
	 * 
	 * @return The transition matrix.
	 ****************** 
	 */
	public int[][] getTransitionMatrix() {
		return null;
	}// Of getTransitionMatrix

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The number of states.
	 ****************** 
	 */
	public int getNumStates() {
		return numStates;
	}// Of getNumStates

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The number of actions.
	 ****************** 
	 */
	public int getNumActions() {
		return numActions;
	}// Of getNumActions

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The current state.
	 ****************** 
	 */
	public int getCurrentState() {
		return currentState;
	}// Of getCurrentState

	/**
	 ****************** 
	 * Setter.
	 * 
	 * @param paraState
	 *            The given state.
	 ****************** 
	 */
	public void setCurrentState(int paraState) {
		currentState = paraState;
	}// Of setCurrentStates

		/**
	 ****************** 
	 * Get valid actions of the current state.
	 * 
	 * @return The actions.
	 ****************** 
	 */
	public int[] getValidActions() {
		return getValidActions(currentState);
	}// Of getValidActions


	/**
	 ****************** 
	 * Reset the game.
	 ****************** 
	 */
	public void reset() {
		// SimpleTools.processTrackingOutput("Resetting TicTacToe.");
		for (int i = 0; i < checkerboard.length; i++) {
			for (int j = 0; j < checkerboard[0].length; j++) {
				checkerboard[i][j] = EMPTY;
			} // Of for j
		} // Of for i
		currentState = 0;
		currentRouteLength = 1;

		// White first
		currentPlayer = WHITE;
	}// Of reset

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
	 * Getter.
	 ****************** 
	 */
	public int getCurrentRouteLength() {
		return currentRouteLength;
	}// Of getCurrentRouteLength
	
	/**
	 ****************** 
	 * Getter.
	 ****************** 
	 */
	public int[] getCurrentRouteStates() {
		return currentRouteStates;
	}// Of getCurrentRouteStates

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
	 * @throws Exception
	 *             if the game is unfinished.
	 ****************** 
	 */
	public int getWinner() throws Exception {
		if (gameSituation == UNFINISHED) {
			throw new Exception("Game unfinished yet, cannot get winner.");
		} // Of if

		return gameSituation;
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
		// Step 1. Construct the checkerboard.
		int[][] tempCheckerboard = stateToCheckerboard(paraState);
		int[] tempValidActions = new int[checkerboardSize * checkerboardSize];
		int tempNumValidActions = 0;

		// Step 1. Check all states.
		for (int i = 0; i < checkerboardSize; i++) {
			for (int j = 0; j < checkerboardSize; j++) {
				if (tempCheckerboard[i][j] == EMPTY) {
					tempValidActions[tempNumValidActions] = i * checkerboardSize + j;
					tempNumValidActions++;
				} // Of if
			} // Of for j
		} // Of for i

		// Step 2. Compress.
		int[] resultActions = new int[tempNumValidActions];
		for (int i = 0; i < tempNumValidActions; i++) {
			resultActions[i] = tempValidActions[i];
		} // Of for i

		// SimpleTools.variableTrackingOutput("Valid actions: " +
		// Arrays.toString(resultActions));

		return resultActions;
	}// Of getValidActions


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
		//SimpleTools.variableTrackingOutput(
		//		"Assign " + currentPlayer + " to (" + tempRow + ", " + tempColumn + ").");
		SimpleTools.variableTrackingOutput("" + paraAction + ", ");
		checkerboard[tempRow][tempColumn] = currentPlayer;
		currentState = checkerboardToState(checkerboard);

		gameSituation = computeGameSituation();
		//SimpleTools.variableTrackingOutput("Environment current stage: " + currentState + ", ");
		//SimpleTools.variableTrackingOutput("gameSituation: " + gameSituation + ", ");

		currentRouteStates[currentRouteLength] = currentState;
		currentRouteLength++;
		
		// Step 3. Now it's the turn of the other player.
		if (currentPlayer == WHITE) {
			currentPlayer = BLACK;
		} else {
			currentPlayer = WHITE;
		} // Of if
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
	public int[][] stateToCheckerboard(int paraState) {
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
	 * Convert as state to a checkerboard.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The checkerboard.
	 ****************** 
	 */
	public String stateToCheckerboardString(int paraState) {
		int[][] tempCheckerboard = stateToCheckerboard(paraState);
		String resultString = "\r\n";
		//resultString += "Checkerboard state: \r\n";
		for (int i = 0; i < tempCheckerboard.length; i++) {
			for (int j = 0; j < tempCheckerboard[0].length; j++) {
				if (tempCheckerboard[i][j] == WHITE) {
					resultString += "o ";
				} else if (tempCheckerboard[i][j] == BLACK) {
					resultString += "x ";
				} else {
					resultString += "- ";
				}//Of if
			}//Of for j
			resultString += "\r\n";
		}//Of for i
		
		//resultString += currentState;
		return resultString;
	}//Of stateToCheckerboardString
	
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
	 * Determine current game situation for white: WHITE, BLACK, TIE, or
	 * UNFINISHED.
	 * 
	 * @return The situation.
	 ****************** 
	 */
	public int computeGameSituation() {
		return computeGameSituation(checkerboard);
	}// Of computeGameSituation

	/**
	 ****************** 
	 * Determine current game situation for white: WHITE, BLACK, TIE, or
	 * UNFINISHED.
	 * Maybe should check the validity of the state.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The situation.
	 ****************** 
	 */
	public int computeGameSituation(int paraState) {
		int[][] tempCheckerboard = stateToCheckerboard(paraState);
		// System.out.println("The checkerboard is: " +
		// Arrays.deepToString(tempCheckerboard));

		return computeGameSituation(tempCheckerboard);
	}// Of computeGameSituation

	/**
	 ****************** 
	 * Determine the given game situation for white: WHITE, BLACK, TIE, or
	 * UNFINISHED.
	 * 
	 * @param paraCheckerboard
	 *            The given checkerboard.
	 * @return The situation
	 ****************** 
	 */
	public int computeGameSituation(int[][] paraCheckerboard) {
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
				return paraCheckerboard[i][0];
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
				return paraCheckerboard[0][j];
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
				return paraCheckerboard[0][0];
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
				return paraCheckerboard[0][checkerboardSize - 1];
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
	}// Of computeGameSituation

	/**
	 ****************** 
	 * Test two methods.
	 ****************** 
	 */
	public static void stateCheckerboardConvertionTest() {
		VTicTacToe tempEnvironment = new VTicTacToe();
		int[] tempTestStates = { 0, 13, 39, 26, 17087 };

		int tempState;
		int[][] tempCheckerboard;
		for (int i = 0; i < tempTestStates.length; i++) {
			tempCheckerboard = tempEnvironment.stateToCheckerboard(tempTestStates[i]);
			System.out.println("The checkerboard of " + tempTestStates[i] + " is: "
					+ Arrays.deepToString(tempCheckerboard));
			tempState = tempEnvironment.checkerboardToState(tempCheckerboard);
			System.out.println("The state ID is " + tempState);
		} // Of for i

	}// Of stateCheckerboardConvertionTest

	/**
	 ****************** 
	 * Test a method.
	 ****************** 
	 */
	public static void getGameSituationTest() {
		VTicTacToe tempEnvironment = new VTicTacToe();
		int[] tempTestStates = { 0, 13, 39, 26, 17060 };

		int tempResult;
		for (int i = 0; i < tempTestStates.length; i++) {
			tempResult = tempEnvironment.computeGameSituation(tempTestStates[i]);
			System.out.println("The game situation of " + tempTestStates[i] + " is: " + tempResult);
		} // Of for i
	}// Of getGameSituationTest

	/**
	 ****************** 
	 * Test a method.
	 ****************** 
	 */
	public static void getValidActionsTest() {
		VTicTacToe tempEnvironment = new VTicTacToe();
		int[] tempTestStates = { 0, 13, 39, 26, 6643 };

		int[] tempResultArray;
		for (int i = 0; i < tempTestStates.length; i++) {
			tempResultArray = tempEnvironment.getValidActions(tempTestStates[i]);
			System.out.println("The valid actions of " + tempTestStates[i] + " is: "
					+ Arrays.toString(tempResultArray));
		} // Of for i
	}// Of getValidActionsTest

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "\r\n";
		//resultString += "Checkerboard state: \r\n";
		for (int i = 0; i < checkerboard.length; i++) {
			for (int j = 0; j < checkerboard.length; j++) {
				if (checkerboard[i][j] == WHITE) {
					resultString += "o ";
				} else if (checkerboard[i][j] == BLACK) {
					resultString += "x ";
				} else {
					resultString += "- ";
				}//Of if
			}//Of for j
			resultString += "\r\n";
		}//Of for i
		
		resultString += currentState;

		//resultString += "\r\nThe current situation is: " + gameSituation;
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
		System.out.println("\r\ngetGameSituationTest()");
		getGameSituationTest();

		System.out.println("\r\nstateCheckerboardConvertionTest()");
		stateCheckerboardConvertionTest();

		System.out.println("\r\ngetValidActionsTest()");
		getValidActionsTest();
	}// Of main
} // Of class TicTacToe
