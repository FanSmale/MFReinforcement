package algorithm;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;

/**
 * The basic Q-learning algorithm.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class QLearning {

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
	Random random = new Random();

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
	public static final int FINAL_STATE = 10;

	/**
	 * The null state.
	 */
	public static final int NULL_STATE = 0;

	/**
	 * The trap state.
	 */
	public static final int TRAP_STATE = -10;
	
	/**
	 * The minimal probability value for the currently worse move.
	 */
	public static final double PROBABILITY_MIN_VALUE = 0.1;
	
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
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraRewardMatrix
	 *            The reward matrix.
	 ****************** 
	 */
	public QLearning(int[][] paraMaze) {
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
	 * 
	 * @return The reward value for the state.
	 ****************** 
	 */
	public int getStateRewardValue(int paraState) {
		int tempRow = paraState / numColumns;
		int tempColumn = paraState % numColumns;
		int resultValue = 0;
		switch (maze[tempRow][tempColumn]) {
		case FINAL_STATE:
			resultValue = 10;
			break;
		case NULL_STATE:
			resultValue = -1;
			break;
		case TRAP_STATE:
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
	 * Generate the transition matrix.
	 * 
	 * @return The transition matrix.
	 ****************** 
	 */
	public int[] computeFinalStates() {
		int tempNumFinalStates = 0;
		int[] tempFinalStates = new int[numRows * numColumns];

		int tempState;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				tempState = i * numColumns + j;

				if (maze[i][j] == FINAL_STATE) {
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
	 * Train.
	 * 
	 * @param paraRounds
	 *            The number of rounds for learning.
	 * @param paraGamma
	 *            The gamma value.
	 * @return The average value of gain.
	 ****************** 
	 */
	public double trainMF(int paraRounds, double paraGamma) {
		qualityMatrix = new double[numRows * numColumns][5];
		double tempTotalReward = 0;
		double tempCurrentRoundReward;

		// Step 1. Randomly pick a state as the start state.
		//int tempStartState = random.nextInt(numRows * numColumns);
		int tempStartState = 0;
		int tempAction, tempNextState;

		// Step 2. Run the given rounds.
		for (int i = 0; i < paraRounds; i++) {
			// Step 2.1. Initialize. Each time start from the same state.
			int tempCurrentState = tempStartState;
			System.out.print("\r\nStart: " + tempCurrentState);
			tempCurrentRoundReward = 0;

			// Step 2.2. Each time a final state should be reached.
			while (!isFinalState(tempCurrentState)) {
				// State 2.2.1. Randomly go one valid step.
				// Different from the existing approach, the choice is according
				// to the Q-matrix.
				// int tempActionIndex =
				// random.nextInt(validActions[tempCurrentState].length);
				int tempNumActions = validActions[tempCurrentState].length;
				double[] tempQualityRewardArray = new double[tempNumActions];
				for (int j = 0; j < tempNumActions; j++) {
					tempAction = validActions[tempCurrentState][j];
					tempQualityRewardArray[j] = qualityMatrix[tempCurrentState][tempAction];
				} // OF for j
				int tempActionIndex = getWeightedRandomIndex(tempQualityRewardArray, PROBABILITY_MIN_VALUE);
				// System.out.println("tempActionIndex = " + tempActionIndex);

				tempAction = validActions[tempCurrentState][tempActionIndex];
				tempNextState = transitionMatrix[tempCurrentState][tempAction];
				tempCurrentRoundReward += rewardMatrix[tempCurrentState][tempAction];

				// Step 2.2.2. Calculate the future reward
				double tempMaxFutureReward = -10000;
				double[] tempFutureRewards = new double[validActions[tempNextState].length];
				for (int j = 0; j < validActions[tempNextState].length; j++) {
					// Still errors.
					tempFutureRewards[j] = qualityMatrix[tempNextState][validActions[tempNextState][j]];
					if (tempMaxFutureReward < tempFutureRewards[j]) {
						tempMaxFutureReward = tempFutureRewards[j];
					} // Of if
				} // Of for j

				// Step 2.2.3. Update the quality matrix.
				// Question: What is the use of gamma?
				qualityMatrix[tempCurrentState][tempAction] = rewardMatrix[tempCurrentState][tempAction]
						+ paraGamma * tempMaxFutureReward;
				// qualityMatrix[tempCurrentState][tempAction] =
				// rewardMatrix[tempCurrentState][tempAction];

				// Step 2.2.4. Prepare for the next step.
				tempCurrentState = tempNextState;
				System.out.print(" -> " + tempCurrentState);
			} // Of while

			tempTotalReward += tempCurrentRoundReward;
		} // Of for i

		averageReward = tempTotalReward / paraRounds;
		return averageReward;
	} // Of trainMF

	/**
	 ****************** 
	 * Get an index of the array, higher values have higher probability.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @return An indexx of the array.
	 ****************** 
	 */
	public int getWeightedRandomIndex(double[] paraArray, double paraMinValue) {
		// Step 1. Scan the first time to obtain max/min/total values.
		double tempMax = -Double.MAX_VALUE;
		double tempMin = Double.MAX_VALUE;
		double tempTotal = 0;
		for (int i = 0; i < paraArray.length; i++) {
			if (tempMax < paraArray[i]) {
				tempMax = paraArray[i];
			} // Of if

			if (tempMin > paraArray[i]) {
				tempMin = paraArray[i];
			} // Of if
		} // Of for i

		// Important: Handle the situation that all elements are the same.
		if (tempMax == tempMin) {
			tempMax = tempMin + 0.01;
		} // Of if

		// Step 2. Calculate their probabilities.
		double[] tempArray = new double[paraArray.length];
		for (int i = 0; i < paraArray.length; i++) {
			tempArray[i] = paraMinValue + (paraArray[i] - tempMin) / (tempMax - tempMin);

			tempTotal += tempArray[i];
		} // Of for i

		// Step 3. Divide [0, 1] into a number of sections.
		double[] tempScaleTotal = new double[tempArray.length];
		double tempSum = 0;
		for (int i = 0; i < tempArray.length; i++) {
			tempSum += tempArray[i] / tempTotal;
			tempScaleTotal[i] = tempSum;
		} // Of for i

		// Step 3. Choose one.
		double tempRandom = random.nextDouble();
		int resultIndex = -1;
		for (int i = 0; i < tempScaleTotal.length; i++) {
			if (tempRandom <= tempScaleTotal[i]) {
				resultIndex = i;
				break;
			} // Of if
		} // Of for i
		return resultIndex;
	}// Of getWeightedRandomIndex

	/**
	 ****************** 
	 * Test respective method.
	 ****************** 
	 */
	public static void getWeightedRandomIndexTest() {
		QLearning tempQLearning = new QLearning(QLearning.EXAMPLE_ONE_MAZE);

		double[] tempDoubleMatrix = { -10, 0, 10 };
		for (int i = 0; i < 10; i++) {
			int tempIndex = tempQLearning.getWeightedRandomIndex(tempDoubleMatrix, 0.1);
			System.out.println("The index is: " + tempIndex);
		} // Of for i
	}// Of getWeightedRandomIndexTest

	/**
	 ****************** 
	 * Existing version of train.
	 * 
	 * @param paraRounds
	 *            The number of rounds for learning.
	 * @param paraGamma
	 *            The gamma value.
	 ****************** 
	 */
	public double train(int paraRounds, double paraGamma) {
		qualityMatrix = new double[numRows * numColumns][5];
		double tempTotalReward = 0;
		double tempCurrentRoundReward;

		// Step 1. Randomly pick a state as the start state.
		//int tempStartState = random.nextInt(numRows * numColumns);
		int tempStartState = 0;
		int tempAction, tempNextState;

		// Step 2. Run the given rounds.
		for (int i = 0; i < paraRounds; i++) {
			// Step 2.1. Initialize. Each time start from the same state.
			tempCurrentRoundReward = 0;
			int tempCurrentState = tempStartState;
			System.out.print("\r\nStart: " + tempCurrentState);

			// Step 2.2. Each time a final state should be reached.
			while (!isFinalState(tempCurrentState)) {
				// State 2.2.1. Randomly go one valid step.
				int tempActionIndex = random.nextInt(validActions[tempCurrentState].length);
				tempAction = validActions[tempCurrentState][tempActionIndex];
				tempNextState = transitionMatrix[tempCurrentState][tempAction];
				tempCurrentRoundReward += rewardMatrix[tempCurrentState][tempAction];

				// Step 2.2.2. Calculate the best future reward according to the quality matrix.
				double tempMaxFutureReward = -10000;
				double[] tempFutureRewards = new double[validActions[tempNextState].length];
				for (int j = 0; j < validActions[tempNextState].length; j++) {
					// The reward of the next move.
					tempFutureRewards[j] = qualityMatrix[tempNextState][validActions[tempNextState][j]];
					if (tempMaxFutureReward < tempFutureRewards[j]) {
						tempMaxFutureReward = tempFutureRewards[j];
					} // Of if
				} // Of for j

				// Step 2.2.3. Update the quality matrix.
				// Question: What is the use of gamma?
				qualityMatrix[tempCurrentState][tempAction] = rewardMatrix[tempCurrentState][tempAction]
						+ paraGamma * tempMaxFutureReward;
				// qualityMatrix[tempCurrentState][tempAction] =
				// rewardMatrix[tempCurrentState][tempAction];

				// Step 2.2.4. Prepare for the next step.
				tempCurrentState = tempNextState;
				System.out.print(" -> " + tempCurrentState);
			} // Of while

			tempTotalReward += tempCurrentRoundReward;
		} // Of for i

		averageReward = tempTotalReward / paraRounds;
		return averageReward;
	} // Of train

	/**
	 ****************** 
	 * Find the best route according to the quality matrix.
	 * 
	 * @param paraStartState
	 *            The starting state.
	 ****************** 
	 */
	public String findBestRoute(int paraStartState) {
		String resultRoute = "Start: " + paraStartState;
		int tempCurrentState = paraStartState;
		int tempNextState;
		while (!isFinalState(tempCurrentState)) {
			double tempMax = -Double.MAX_VALUE;
			tempNextState = -1;
			//Choose the current best move.
			for (int i = 0; i < validActions[tempCurrentState].length; i++) {
				int tempAction = validActions[tempCurrentState][i];
				if (tempMax < qualityMatrix[tempCurrentState][tempAction]) {
					tempMax = qualityMatrix[tempCurrentState][tempAction];
					tempNextState = transitionMatrix[tempCurrentState][tempAction];
				}//Of if
			}//Of for i
			
			resultRoute += " -> " + tempNextState;
			tempCurrentState = tempNextState;
		}//Of while
		
		resultRoute += "\r\n";
		
		return resultRoute;
	}//Of findBestRoute
	
	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "\r\n*****Input*****";
		resultString += "\r\nThe maze is:\r\n" + Arrays.deepToString(maze);
		resultString += "\r\nThe final states include:\r\n" + Arrays.toString(finalStates);
		resultString += "\r\nThe reward matrix is:\r\n" + Arrays.deepToString(rewardMatrix);
		resultString += "\r\nThe valid actions matrix is:\r\n" + Arrays.deepToString(validActions);

		resultString += "\r\n*****Output*****";
		resultString += "\r\nThe quality matrix is:\r\n" + Arrays.deepToString(qualityMatrix);
		resultString += "\r\nThe average reward is:\r\n" + averageReward;
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
		// QLearning tempQLearning = new QLearning(QLearning.EXAMPLE_ONE_MAZE);
		QLearning tempQLearning = new QLearning(QLearning.EXAMPLE_TWO_MAZE);
		//double tempAverageReward = tempQLearning.train(1000, 0.9);
		//System.out.println("\r\nWith the given algorithm: ");
		//System.out.println(tempQLearning);

		double tempAverageReward = tempQLearning.trainMF(1000, 0.8);
		System.out.println("\r\nWith my implementation: ");
		System.out.println(tempQLearning);
		
		System.out.println(tempQLearning.findBestRoute(5));

		// getWeightedRandomIndexTest();
	}// Of main
} // Of class QLearning
