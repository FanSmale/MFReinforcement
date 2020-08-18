package deprecated.maze;

import java.util.Arrays;
import java.util.Random;

/**
 * The learning algorithm for maze.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public abstract class Maze {

	/**
	 * Move direction: up.
	 */
	public static final int UP = 0;

	/**
	 * Move direction: down.
	 */
	public static final int DOWN = 1;

	/**
	 * Move direction: left.
	 */
	public static final int LEFT = 2;

	/**
	 * Move direction: right.
	 */
	public static final int RIGHT = 3;

	/**
	 * Move direction: no move.
	 */
	public static final int NO_MOVE = 4;

	/**
	 * Random generator.
	 */
	public static Random random = new Random();

	/**
	 * The number of columns.
	 */
	int numColumns;

	/**
	 * The number of rows.
	 */
	int numRows;

	/**
	 * The first exemplary maze.
	 */
	public static final int[][] EXAMPLE_ONE_MAZE = { { 0, 0 }, { -10, 10 } };

	/**
	 * The first exemplary maze.
	 */
	public static final int[][] EXAMPLE_TWO_MAZE = { { 0, 0, 0, 0, 0 }, { -10, 0, 0, 0, 0 },
			{ 0, 0, 10, -10, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, -10, 0 } };

	/**
	 * The final state.
	 */
	public static final int FINAL_STATE_VALUE = 10;

	/**
	 * The null state.
	 */
	public static final int NULL_STATE_VALUE = 0;

	/**
	 * The trap state.
	 */
	public static final int TRAP_STATE_VALUE = -10;

	/**
	 * Indicate the index of the invalid state.
	 */
	public static final int INVALID_STATE = -1;

	/**
	 * The maze.
	 */
	int[][] maze;

	/**
	 * The set of final states.
	 */
	int[] finalStates;

	/**
	 * The reward matrix.
	 */
	int[][] rewardMatrix;

	/**
	 * The quality matrix.
	 */
	double[][] qualityMatrix;

	/**
	 * The transition matrix. The directions are: up, down, left, right, no
	 * move, respectively. It is determined by the size of the maze.
	 */
	int[][] transitionMatrix;

	/**
	 * The valid actions for each state. It is determined by the size of the
	 * maze.
	 */
	int[][] validActions;

	/**
	 * Average reward of the learner.
	 */
	double averageReward;

	/**
	 * The current route.
	 */
	int[] currentRoute;

	/**
	 * The reward of the current route.
	 */
	int currentRouteReward;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public Maze(int[][] paraMaze) {
		maze = paraMaze;

		numRows = maze.length;
		numColumns = maze[0].length;

		computeFinalStates();
		generateTransitionMatrix();
		generateValidActions();

		generateRewardMatrix();
	}// Of the first constructor

	/**
	 ****************** 
	 * Get state reward value.
	 * @param paraState The given state.
	 * 
	 * @return The reward value for the state.
	 ****************** 
	 */
	public int getStateRewardValue(int paraState) {
		int tempRow = paraState / numColumns;
		int tempColumn = paraState % numColumns;
		int resultValue = 0;
		switch (maze[tempRow][tempColumn]) {
		case FINAL_STATE_VALUE:
			resultValue = 10;
			break;
		case NULL_STATE_VALUE:
			resultValue = -1;
			break;
		case TRAP_STATE_VALUE:
			resultValue = -10;
			break;
		default:
			System.out.println("Internal error in getStateRewardValue():\r\n"
					+ "Unsupported state: " + maze[tempRow][tempColumn]);
			System.exit(0);
		}// Of switch

		return resultValue;
	}// Of getStateRewardValue

	/**
	 ****************** 
	 * Generate the reward matrix from the maze.
	 * 
	 * @return The transition matrix.
	 ****************** 
	 */
	public int[][] generateRewardMatrix() {
		rewardMatrix = new int[numRows * numColumns][5];
		int tempState, tempNextState;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				tempState = i * numColumns + j;

				// The first row cannot go up.
				if (i == 0) {
					rewardMatrix[tempState][UP] = 0;
				} else {
					tempNextState = tempState - numColumns;
					rewardMatrix[tempState][UP] = getStateRewardValue(tempNextState);
				} // Of if

				// The last row cannot go down.
				if (i == numRows - 1) {
					rewardMatrix[tempState][DOWN] = 0;
				} else {
					tempNextState = tempState + numColumns;
					rewardMatrix[tempState][DOWN] = getStateRewardValue(tempNextState);
				} // Of if

				// The first column cannot go left.
				if (j == 0) {
					rewardMatrix[tempState][LEFT] = 0;
				} else {
					tempNextState = tempState - 1;
					rewardMatrix[tempState][LEFT] = getStateRewardValue(tempNextState);
				} // Of if

				// The first column cannot go right.
				if (j == numColumns - 1) {
					rewardMatrix[tempState][RIGHT] = 0;
				} else {
					tempNextState = tempState + 1;
					rewardMatrix[tempState][RIGHT] = getStateRewardValue(tempNextState);
				} // Of if

				rewardMatrix[tempState][NO_MOVE] = getStateRewardValue(tempState);
			} // Of for j
		} // Of for i

		return rewardMatrix;
	}// Of generateRewardMatrix

	/**
	 ****************** 
	 * Generate the transition matrix.
	 * 
	 * @return The transition matrix.
	 ****************** 
	 */
	public int[][] generateTransitionMatrix() {
		transitionMatrix = new int[numRows * numColumns][5];

		int tempState;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				tempState = i * numColumns + j;

				// The first row cannot go up.
				if (i == 0) {
					transitionMatrix[tempState][UP] = -1;
				} else {
					transitionMatrix[tempState][UP] = tempState - numColumns;
				} // Of if

				// The last row cannot go down.
				if (i == numRows - 1) {
					transitionMatrix[tempState][DOWN] = -1;
				} else {
					transitionMatrix[tempState][DOWN] = tempState + numColumns;
				} // Of if

				// The first column cannot go left.
				if (j == 0) {
					transitionMatrix[tempState][LEFT] = -1;
				} else {
					transitionMatrix[tempState][LEFT] = tempState - 1;
				} // Of if

				// The first column cannot go right.
				if (j == numColumns - 1) {
					transitionMatrix[tempState][RIGHT] = -1;
				} else {
					transitionMatrix[tempState][RIGHT] = tempState + 1;
				} // Of if

				transitionMatrix[tempState][NO_MOVE] = tempState;
			} // Of for j
		} // Of for i

		return transitionMatrix;
	}// Of generateTransationMatrix

	/**
	 ****************** 
	 * Generate the transition matrix.
	 * 
	 * @return The transition matrix.
	 ****************** 
	 */
	public int[][] generateValidActions() {
		validActions = new int[numRows * numColumns][5];

		int tempState;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				tempState = i * numColumns + j;

				// 8 special cases.
				if (i == 0) {
					if (j == 0) {
						validActions[tempState] = new int[3];
						validActions[tempState][0] = DOWN;
						validActions[tempState][1] = RIGHT;
						validActions[tempState][2] = NO_MOVE;
					} else if (j == numColumns - 1) {
						validActions[tempState] = new int[3];
						validActions[tempState][0] = DOWN;
						validActions[tempState][1] = LEFT;
						validActions[tempState][2] = NO_MOVE;
					} else {
						validActions[tempState] = new int[4];
						validActions[tempState][0] = DOWN;
						validActions[tempState][1] = LEFT;
						validActions[tempState][2] = RIGHT;
						validActions[tempState][3] = NO_MOVE;
					} // Of if
				} else if (i == numRows - 1) {
					if (j == 0) {
						validActions[tempState] = new int[3];
						validActions[tempState][0] = UP;
						validActions[tempState][1] = RIGHT;
						validActions[tempState][2] = NO_MOVE;
					} else if (j == numColumns - 1) {
						validActions[tempState] = new int[3];
						validActions[tempState][0] = UP;
						validActions[tempState][1] = LEFT;
						validActions[tempState][2] = NO_MOVE;
					} else {
						validActions[tempState] = new int[4];
						validActions[tempState][0] = UP;
						validActions[tempState][1] = LEFT;
						validActions[tempState][2] = RIGHT;
						validActions[tempState][3] = NO_MOVE;
					} // Of if
				} else if (j == 0) {
					validActions[tempState] = new int[4];
					validActions[tempState][0] = UP;
					validActions[tempState][1] = DOWN;
					validActions[tempState][2] = RIGHT;
					validActions[tempState][3] = NO_MOVE;
				} else if (j == numColumns - 1) {
					validActions[tempState] = new int[4];
					validActions[tempState][0] = UP;
					validActions[tempState][1] = DOWN;
					validActions[tempState][2] = LEFT;
					validActions[tempState][3] = NO_MOVE;
				} else {
					validActions[tempState] = new int[5];
					validActions[tempState][0] = UP;
					validActions[tempState][1] = DOWN;
					validActions[tempState][2] = LEFT;
					validActions[tempState][3] = RIGHT;
					validActions[tempState][4] = NO_MOVE;
				} // Of if
			} // Of for j
		} // Of for i

		return validActions;
	}// Of generateValidActions

	/**
	 ****************** 
	 * Compute the set of final states.
	 * 
	 * @return The set of final states.
	 ****************** 
	 */
	public int[] computeFinalStates() {
		int tempNumFinalStates = 0;
		int[] tempFinalStates = new int[numRows * numColumns];

		int tempState;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				tempState = i * numColumns + j;

				if (maze[i][j] == FINAL_STATE_VALUE) {
					tempFinalStates[tempNumFinalStates] = tempState;
					tempNumFinalStates++;
				} // Of if
			} // Of for j
		} // Of j

		// Now copy.
		finalStates = new int[tempNumFinalStates];
		for (int i = 0; i < tempNumFinalStates; i++) {
			finalStates[i] = tempFinalStates[i];
		} // Of for i

		return finalStates;
	}// Of computeFinalStates

	/**
	 ****************** 
	 * Is the given state a final state?
	 * @param paraState The given state.
	 * 
	 * @return True if it is.
	 ****************** 
	 */
	public boolean isFinalState(int paraState) {
		for (int i = 0; i < finalStates.length; i++) {
			if (paraState == finalStates[i]) {
				return true;
			} // Of if
		} // Of for i

		return false;
	}// Of isFinalState

	/**
	 ****************** 
	 * Is the given state a trap state?
	 * @param paraState The given state.
	 * 
	 * @return True if it is.
	 ****************** 
	 */
	public boolean isTrapState(int paraState) {
		if (maze[paraState / numColumns][paraState % numRows] == TRAP_STATE_VALUE) {
			return true;
		}//Of if
		
		return false;
	}// Of isTrapState

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The set of final states.
	 ****************** 
	 */
	public int[] getFinalStates() {
		return finalStates;
	}//Of getFinalStates

	/**
	 ****************** 
	 * Find the best route according to the quality matrix.
	 * 
	 * @param paraStartState
	 *            The starting state.
	 *            @return The route information.
	 * @throws Exception if the given state is a trap state.
	 ****************** 
	 */
	public String findBestRoute(int paraStartState) throws Exception {
		if (isTrapState(paraStartState)) {
			throw new Exception("State " + paraStartState + " is a trap state.");
		}//Of if
		
		int[] tempCurrentRoute = new int[numColumns * numRows];
		int tempCurrentRouteLength = 0;

		String resultRoute = "Start: " + paraStartState;
		currentRouteReward = 0;
		int tempCurrentState = paraStartState;

		tempCurrentRoute[tempCurrentRouteLength] = tempCurrentState;
		tempCurrentRouteLength++;

		int tempNextState;
		while (!isFinalState(tempCurrentState)) {
			double tempMax = -Double.MAX_VALUE;
			tempNextState = -1;
			int tempAction = -1;
			// Choose the current best move.
			for (int i = 0; i < validActions[tempCurrentState].length; i++) {
				tempAction = validActions[tempCurrentState][i];
				if (tempMax < qualityMatrix[tempCurrentState][tempAction]) {
					tempMax = qualityMatrix[tempCurrentState][tempAction];
					tempNextState = transitionMatrix[tempCurrentState][tempAction];
				} // Of if
			} // Of for i

			resultRoute += " -> " + tempNextState;
			currentRouteReward += rewardMatrix[tempCurrentState][tempAction];

			// Prepare for the next state.
			tempCurrentState = tempNextState;
			tempCurrentRoute[tempCurrentRouteLength] = tempCurrentState;
			tempCurrentRouteLength++;
		} // Of while

		resultRoute += ", reward = " + currentRouteReward + ".\r\n";

		currentRoute = new int[tempCurrentRouteLength];
		for (int i = 0; i < tempCurrentRouteLength; i++) {
			currentRoute[i] = tempCurrentRoute[i];
		} // Of for i

		return resultRoute;
	}// Of findBestRoute

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The current route.
	 ****************** 
	 */
	public int[] getCurrentRoute() {
		return currentRoute;
	}// Of getCurrentRoute

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The reward of the current route.
	 ****************** 
	 */
	public int getCurrentRouteReward() {
		return currentRouteReward;
	}// Of getCurrentRoute

	/**
	 ****************** 
	 * The training method that should be overwritten.
	 * 
	 * @param paraRounds
	 *            The number of rounds for learning.
	 * @return The average reward.
	 ****************** 
	 */
	abstract public double train(int paraRounds);

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "\r\n*****Input of the maze*****";
		resultString += "\r\nThe maze is:\r\n" + Arrays.deepToString(maze);
		resultString += "\r\nThe final states include:\r\n" + Arrays.toString(finalStates);
		resultString += "\r\nThe reward matrix is:\r\n" + Arrays.deepToString(rewardMatrix);
		resultString += "\r\nThe valid actions matrix is:\r\n" + Arrays.deepToString(validActions);
		resultString += "\r\n*****Output of the maze*****";
		resultString += "\r\nThe quality matrix is:\r\n" + Arrays.deepToString(qualityMatrix);
		return resultString;
	} // Of toString

} // Of class QLearning
