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

public class VTicTacToeDynamicProgramming extends VTicTacToe {

	/**
	 * The transition matrix. The number of rows is numStates, and the number of
	 * columns is numActions.
	 */
	public int[][] transitionMatrix;

	/**
	 * Which states are available?
	 */
	boolean[] stateAvailableArray;

	/**
	 * Store only available?
	 */
	int[] availableStates;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public VTicTacToeDynamicProgramming() {
		super();

		computeTransitionMatrix();
		availableStates = booleanArrayToIndexArray(stateAvailableArray);
	}// Of the first constructor

	/**
	 ****************** 
	 * Backup for this route, update the value array. This is the core code.
	 ****************** 
	 */
	private void computeTransitionMatrix() {
		stateAvailableArray = new boolean[numStates];
		transitionMatrix = new int[numStates][numActions];

		stepInAvailableStates(0, 1);
	}// Of compute the transition matrix

	/**
	 ****************** 
	 * Step in more available states. It is recursive.
	 * 
	 * @param paraState
	 *            The given state.
	 * @param paraPlayer
	 *            The current player, 1 or 2.
	 ****************** 
	 */
	public void stepInAvailableStates(int paraState, int paraPlayer) {
		stateAvailableArray[paraState] = true;
		// System.out.println("Available: " + paraState);
		int tempSituation = computeGameSituation(paraState);
		if (tempSituation != VTicTacToe.UNFINISHED) {
			return;
		} // Of if

		int tempNextPlayer = -1;
		if (paraPlayer == 1) {
			tempNextPlayer = 2;
		} else {
			tempNextPlayer = 1;
		} // Of if

		// 3^0, 3^1, ..., 3^8
		int tempPositionBase = 1;
		int tempNextState;
		int tempDigit;
		for (int i = 0; i < numActions; i++) {
			tempDigit = paraState % (tempPositionBase * 3) / tempPositionBase;
			// System.out.println("The digit for " + i + "is: " + tempDigit);
			if (tempDigit == 0) {
				// System.out.println(
				// "From " +
				// Arrays.deepToString(environment.stateToCheckerboard(paraState))
				// + " adding " + paraPlayer + " to position " + i);
				// System.out.println("The next round will test " +
				// Arrays.deepToString(environment
				// .stateToCheckerboard(paraState + paraPlayer *
				// tempPositionBase)));
				tempNextState = paraState + paraPlayer * tempPositionBase;
				transitionMatrix[paraState][i] = tempNextState;
				stepInAvailableStates(tempNextState, tempNextPlayer);
			} // Of if
			tempPositionBase *= 3;
		} // Of for i
	}// Of stepInAvailableStates

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The transition matrix.
	 ****************** 
	 */
	public int[][] getTransitionMatrix() {
		return transitionMatrix;
	}// Of getTransitionMatrix

	/**
	 ****************** 
	 * Train.
	 * 
	 * @param paraEpisodes
	 *            The number of episodes.
	 ****************** 
	 */
	public static int[] booleanArrayToIndexArray(boolean[] paraArray) {
		int[] tempIntArray = new int[paraArray.length];
		int tempLength = 0;

		for (int i = 0; i < paraArray.length; i++) {
			if (paraArray[i]) {
				tempIntArray[tempLength] = i;
				tempLength++;
			} // Of if
		} // Of for i

		// Compress.
		int[] resultArray = new int[tempLength];
		for (int i = 0; i < resultArray.length; i++) {
			resultArray[i] = tempIntArray[i];
		} // Of for i

		return resultArray;
	}// Of booleanArrayToIndexArray

	/**
	 ****************** 
	 * Getter.
	 ****************** 
	 */
	public int[] getAvailableStates() {
		return availableStates;
	}// Of getAvailableStates

	/**
	 ****************** 
	 * Is the state available?
	 ****************** 
	 */
	public boolean isStateAvailable(int paraState) {
		return stateAvailableArray[paraState];
	}// Of getAvailableStates

	/**
	 ****************** 
	 * Test a method.
	 ****************** 
	 */
	public static void getValidActionsTest() {
		VTicTacToeDynamicProgramming tempEnvironment = new VTicTacToeDynamicProgramming();
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
		return super.toString();
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
		VTicTacToeDynamicProgramming tempEnvironment = new VTicTacToeDynamicProgramming();
		System.out.println("The first line of the transition matrix: "
				+ Arrays.toString(tempEnvironment.getTransitionMatrix()[0]));
		System.out.println("The second line of the transition matrix: "
				+ Arrays.toString(tempEnvironment.getTransitionMatrix()[1]));

	}// Of main
} // Of class VTicTacToeDynamicProgramming
