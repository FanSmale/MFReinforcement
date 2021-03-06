package qlearning.environment;

import java.util.Arrays;

import qlearning.action.IllegalActionException;
import common.Common;
import common.SimpleTools;

/**
 * The learning algorithm for maze.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class Maze extends Environment {

	/**
	 * The number of columns.
	 */
	int numColumns;

	/**
	 * The number of rows.
	 */
	int numRows;

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
	 * The first exemplary maze.
	 */
	public static final int[][] EXAMPLE_ONE_MAZE = { { 0, 0 }, { TRAP_STATE_VALUE, FINAL_STATE_VALUE } };

	/**
	 * The first exemplary maze.
	 */
	public static final int[][] EXAMPLE_TWO_MAZE = { { 0, 0, 0, 0, 0 }, { TRAP_STATE_VALUE, 0, 0, 0, 0 },
			{ 0, 0, TRAP_STATE_VALUE, TRAP_STATE_VALUE, 0 }, { 0, 0, 0, FINAL_STATE_VALUE, 0 }, { 0, TRAP_STATE_VALUE, 0, TRAP_STATE_VALUE, 0 } };

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

		//actionSpace = new MazeActionSpace();

		numRows = maze.length;
		numColumns = maze[0].length;

		numStates = numRows * numColumns;
		numActions = 4;

		computeFinalStates();
		generateTransitionMatrix();
		generateValidActions();

		generateRewardMatrix();
	}// Of the first constructor

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
		int tempRow = paraState / numColumns;
		int tempColumn = paraState % numColumns;
		int resultValue = 0;
		switch (maze[tempRow][tempColumn]) {
		case FINAL_STATE_VALUE:
			resultValue = Environment.REWARD_VALUE;;
			break;
		case NULL_STATE_VALUE:
			resultValue = -1;
			break;
		case TRAP_STATE_VALUE:
			resultValue = Environment.PENALTY_VALUE;
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
		rewardMatrix = new int[numRows * numColumns][4];
		int tempState, tempNextState;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				tempState = i * numColumns + j;

				// The first row cannot go up.
				if (i == 0) {
					rewardMatrix[tempState][MazeActionSpace.UP] = 0;
				} else {
					tempNextState = tempState - numColumns;
					rewardMatrix[tempState][MazeActionSpace.UP] = getStateRewardValue(
							tempNextState);
				} // Of if

				// The last row cannot go down.
				if (i == numRows - 1) {
					rewardMatrix[tempState][MazeActionSpace.DOWN] = 0;
				} else {
					tempNextState = tempState + numColumns;
					rewardMatrix[tempState][MazeActionSpace.DOWN] = getStateRewardValue(
							tempNextState);
				} // Of if

				// The first column cannot go left.
				if (j == 0) {
					rewardMatrix[tempState][MazeActionSpace.LEFT] = 0;
				} else {
					tempNextState = tempState - 1;
					rewardMatrix[tempState][MazeActionSpace.LEFT] = getStateRewardValue(
							tempNextState);
				} // Of if

				// The first column cannot go right.
				if (j == numColumns - 1) {
					rewardMatrix[tempState][MazeActionSpace.RIGHT] = 0;
				} else {
					tempNextState = tempState + 1;
					rewardMatrix[tempState][MazeActionSpace.RIGHT] = getStateRewardValue(
							tempNextState);
				} // Of if
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
					transitionMatrix[tempState][MazeActionSpace.UP] = -1;
				} else {
					transitionMatrix[tempState][MazeActionSpace.UP] = tempState - numColumns;
				} // Of if

				// The last row cannot go down.
				if (i == numRows - 1) {
					transitionMatrix[tempState][MazeActionSpace.DOWN] = -1;
				} else {
					transitionMatrix[tempState][MazeActionSpace.DOWN] = tempState + numColumns;
				} // Of if

				// The first column cannot go left.
				if (j == 0) {
					transitionMatrix[tempState][MazeActionSpace.LEFT] = -1;
				} else {
					transitionMatrix[tempState][MazeActionSpace.LEFT] = tempState - 1;
				} // Of if

				// The first column cannot go right.
				if (j == numColumns - 1) {
					transitionMatrix[tempState][MazeActionSpace.RIGHT] = -1;
				} else {
					transitionMatrix[tempState][MazeActionSpace.RIGHT] = tempState + 1;
				} // Of if
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
						validActions[tempState] = new int[2];
						validActions[tempState][0] = MazeActionSpace.DOWN;
						validActions[tempState][1] = MazeActionSpace.RIGHT;
					} else if (j == numColumns - 1) {
						validActions[tempState] = new int[2];
						validActions[tempState][0] = MazeActionSpace.DOWN;
						validActions[tempState][1] = MazeActionSpace.LEFT;
					} else {
						validActions[tempState] = new int[3];
						validActions[tempState][0] = MazeActionSpace.DOWN;
						validActions[tempState][1] = MazeActionSpace.LEFT;
						validActions[tempState][2] = MazeActionSpace.RIGHT;
					} // Of if
				} else if (i == numRows - 1) {
					if (j == 0) {
						validActions[tempState] = new int[2];
						validActions[tempState][0] = MazeActionSpace.UP;
						validActions[tempState][1] = MazeActionSpace.RIGHT;
					} else if (j == numColumns - 1) {
						validActions[tempState] = new int[2];
						validActions[tempState][0] = MazeActionSpace.UP;
						validActions[tempState][1] = MazeActionSpace.LEFT;
					} else {
						validActions[tempState] = new int[3];
						validActions[tempState][0] = MazeActionSpace.UP;
						validActions[tempState][1] = MazeActionSpace.LEFT;
						validActions[tempState][2] = MazeActionSpace.RIGHT;
					} // Of if
				} else if (j == 0) {
					validActions[tempState] = new int[3];
					validActions[tempState][0] = MazeActionSpace.UP;
					validActions[tempState][1] = MazeActionSpace.DOWN;
					validActions[tempState][2] = MazeActionSpace.RIGHT;
				} else if (j == numColumns - 1) {
					validActions[tempState] = new int[3];
					validActions[tempState][0] = MazeActionSpace.UP;
					validActions[tempState][1] = MazeActionSpace.DOWN;
					validActions[tempState][2] = MazeActionSpace.LEFT;
				} else {
					validActions[tempState] = new int[4];
					validActions[tempState][0] = MazeActionSpace.UP;
					validActions[tempState][1] = MazeActionSpace.DOWN;
					validActions[tempState][2] = MazeActionSpace.LEFT;
					validActions[tempState][3] = MazeActionSpace.RIGHT;
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
	 * Reset the environment.
	 ****************** 
	 */
	public void reset(){
		setCurrentState(startState);
	}//Of reset

	/**
	 ****************** 
	 * Is the current state a final state?
	 * 
	 * @return True if it is.
	 ****************** 
	 */
	public boolean isFinished() {
		for (int i = 0; i < finalStates.length; i++) {
			if (currentState == finalStates[i]) {
				return true;
			} // Of if
		} // Of for i

		return false;
	}// Of isFinished
	
	/**
	 ****************** 
	 * Is the given state a trap state?
	 * 
	 * @param paraState
	 *            The given state.
	 * @return True if it is.
	 ****************** 
	 */
	public boolean isTrapState(int paraState) {
		if (maze[paraState / numColumns][paraState % numRows] == TRAP_STATE_VALUE) {
			return true;
		} // Of if

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
	}// Of getFinalStates

	/**
	 ****************** 
	 * Select an action to take.
	 * 
	 * @return The action.
	 ****************** 
	public int selectAction() {
		// Attention!! Not implemented yet.
		return 0;
	}// Of selectAction
	 */

	/**
	 ****************** 
	 * Go one step with the given action.
	 * 
	 * @param paraAction
	 *            The given action.
	 * @return Whether or not the process comes an end.
	 ****************** 
	 */
	public void step(int paraAction) throws IllegalActionException{
		// Store the reward.
		currentReward = rewardMatrix[currentState][paraAction];
		// System.out.println("State: " + currentState + ", action: " +
		// paraAction);

		// Change the state if the new one is not a trap.
		int tempState = transitionMatrix[currentState][paraAction];
		
		if (!isTrapState(tempState)) {
			currentState = tempState;
		} else {
			SimpleTools.variableTrackingOutput("Trying to get into (" + (tempState / numColumns)+
					", " + (tempState % numColumns) + ") from (" + (currentState / numColumns)+
					", " + (currentState % numColumns) + ") but fail.");
			Common.wallTimes ++;
		}//Of if
				
		//return isFinished();
	}// Of step

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
	 * Generate a complex (32 * 32) maze.
	 * 
	 * @return The maze in int matrix.
	 ****************** 
	 */
	public static int[][] generateComplexMaze() {
		int tempSize = 31;
		int[][] resultMaze = new int[tempSize][tempSize];

		// Wall in four directions.
		for (int i = 0; i < resultMaze.length; i++) {
			resultMaze[0][i] = TRAP_STATE_VALUE; // North
			resultMaze[tempSize - 1][i] = TRAP_STATE_VALUE; // South
			resultMaze[i][0] = TRAP_STATE_VALUE; // West
			resultMaze[i][tempSize - 1] = TRAP_STATE_VALUE; // East
		} // Of for i

		// wall += [(6, i) for i in range(24, 30)]
		for (int i = 24; i < 30; i++) {
			resultMaze[6][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(6, i) for i in range(4, 17)]
		for (int i = 4; i < 17; i++) {
			resultMaze[6][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(12, i) for i in range(1, 7)]
		for (int i = 1; i < 7; i++) {
			resultMaze[12][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(12, i) for i in range(14, 27)]
		for (int i = 14; i < 27; i++) {
			resultMaze[12][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(18, i) for i in range(4, 17)]
		for (int i = 4; i < 17; i++) {
			resultMaze[18][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(18, i) for i in range(24, 30)]
		for (int i = 24; i < 30; i++) {
			resultMaze[18][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(24, i) for i in range(1, 7)]
		for (int i = 1; i < 7; i++) {
			resultMaze[24][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(24, i) for i in range(14, 27)]
		for (int i = 14; i < 27; i++) {
			resultMaze[24][i] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(j, 10) for j in range(7, 30)]
		for (int i = 7; i < 30; i++) {
			resultMaze[i][10] = TRAP_STATE_VALUE;
		} // Of for i

		// wall += [(j, 20) for j in range(6, 30)]
		for (int i = 6; i < 30; i++) {
			resultMaze[i][20] = TRAP_STATE_VALUE;
		} // Of for i
		
		//One final state.
		resultMaze[tempSize - 2][tempSize - 2] = FINAL_STATE_VALUE;
		
		return resultMaze;
	}// Of generateComplexMaze

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "\r\n*****I am a maze*****";
		resultString += "\r\nMy data matrix is:\r\n" + Arrays.deepToString(maze);
		resultString += "\r\nThe final states include:\r\n" + Arrays.toString(finalStates);
		resultString += "\r\nThe reward matrix is:\r\n" + Arrays.deepToString(rewardMatrix);
		resultString += "\r\nThe valid actions matrix is:\r\n" + Arrays.deepToString(validActions);
		return resultString;
	} // Of toString

} // Of class Maze
