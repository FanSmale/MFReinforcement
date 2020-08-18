package learner;

import environment.Maze;

import java.util.Arrays;

import common.Common;
import common.SimpleTools;
import environment.Environment;

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

public class SimpleQLearner extends Learner {

	/**
	 * The quality matrix.
	 */
	double[][] qualityMatrix;

	/**
	 * The minimal probability value for the currently worse move.
	 */
	public static final double PROBABILITY_MIN_VALUE = 0.1;

	/**
	 * The gamma value
	 */
	double gamma;

	/**
	 * The alpha value
	 */
	double alpha;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public SimpleQLearner(Environment paraEnvironment) {
		super(paraEnvironment);
		gamma = 0.99;
		alpha = 0.1;
	}// Of the first constructor

	/**
	 ****************** 
	 * Setter.
	 * 
	 * @param paraGamma
	 *            The given gamma value.
	 ****************** 
	 */
	public void setGamma(double paraGamma) {
		gamma = paraGamma;
	}// Of setGamma

	/**
	 ****************** 
	 * Learn.
	 * 
	 * @param paraEpisodes
	 *            The number of rounds.
	 ****************** 
	 */
	public void learn(int paraEpisodes) {
		qualityMatrix = new double[numStates][numActions];
		System.out.println("numStates = " + numStates + ", numActions = " + numActions);
		SimpleTools.variableTracking = false;

		rewardArray = new double[paraEpisodes];

		double tempTotalReward = 0;
		// double tempCurrentRoundReward;

		// Step 1. Randomly pick a state as the start state.
		// int tempStartState = random.nextInt(numRows * numColumns);
		int tempStartState = environment.getStartState();
		int tempAction, tempNextState;

		// Step 2. Run the given rounds.
		int[] wallTimesArray = new int[paraEpisodes];
		int[] stepsArray = new int[paraEpisodes];
		for (int i = 0; i < paraEpisodes; i++) {
			Common.wallTimes = 0;
			if (i == paraEpisodes - 1) {
				SimpleTools.variableTracking = true;
			} // Of if

			// Step 2.1. Initialize. Each time start from the same state.
			rewardArray[i] = 0;
			int tempCurrentState = tempStartState;
			
			// System.out.print("\r\nStart: " + tempCurrentState);
			environment.setCurrentState(tempCurrentState);
			boolean tempFinished = false;

			// Step 2.2. Each time a final state should be reached.
			while (!tempFinished) {
				stepsArray[i]++;
				// State 2.2.1. Randomly go one valid step.
				// The implementation depends on the quality value of actions.
				int[] tempValidActions = environment.getValidActions();
				tempAction = selectAction(qualityMatrix[tempCurrentState],
						tempValidActions);
				//tempAction = tempValidActions[tempActionIndex];

				tempFinished = environment.step(tempAction);
				tempNextState = environment.getCurrentState();

				rewardArray[i] += environment.getCurrentReward();

				// Step 2.2.2. Calculate the best future reward according to the
				// quality matrix.
				double tempMaxFutureReward = 0;
				double tempFutureReward;
				tempValidActions = environment.getValidActions(tempNextState);

				for (int j = 0; j < tempValidActions.length; j++) {
					tempFutureReward = qualityMatrix[tempNextState][tempValidActions[j]];
					if (tempMaxFutureReward < tempFutureReward) {
						tempMaxFutureReward = tempFutureReward;
					} // Of if
				} // Of for j

				// Step 2.2.3. Update the quality matrix.
				// The use of gamma and alpha might not be correct.
				double tempDelta = environment.getCurrentReward() + gamma * tempMaxFutureReward;
				double tempReward = environment.getCurrentReward();
				double tempOldQuality = qualityMatrix[tempCurrentState][tempAction];
				if (tempReward == Environment.TRAP_VALUE) {
					qualityMatrix[tempCurrentState][tempAction] = tempReward;
				} else {
					qualityMatrix[tempCurrentState][tempAction] = tempOldQuality
							+ alpha * (tempDelta - tempOldQuality);
				} // Of if

				// Step 2.2.4. Prepare for the next step.
				tempCurrentState = tempNextState;
			} // Of while

			wallTimesArray[i] = Common.wallTimes;
		} // Of for i

		System.out.println("Wall times: " + Arrays.toString(wallTimesArray));
		System.out.println("Steps: " + Arrays.toString(stepsArray));

		System.out.println("\r\nQ = " + Arrays.deepToString(qualityMatrix));
	} // Of learn

	/**
	 ****************** 
	 * Select an action according to the given rewards. Random selection.
	 * 
	 * @param paraRewardArray
	 *            The given reward array.
	 * @param paraValidActions
	 *            The valid actions.
	 * @return The selected action.
	 ****************** 
	 */
	public int selectAction(double[] paraRewardArray, int[] paraValidActions) {
		int tempActionIndex = Environment.random.nextInt(paraValidActions.length);

		return paraValidActions[tempActionIndex];
	}// Of selectActionIndex

	/**
	 ****************** 
	 * In each state use greedy selection for routing.
	 * 
	 * @param paraStartState
	 *            The start state.
	 * @return The route information.
	 * @throws Exception
	 *             if any.
	 ****************** 
	 */
	public int[] greedyRouting(int paraStartState) throws Exception {
		int[] tempCurrentRoute = new int[qualityMatrix.length];
		if (environment.isTrapState(paraStartState)) {
			throw new Exception("State " + paraStartState + " is a trap state.");
		} // Of if

		int tempCurrentRouteLength = 0;

		// currentRouteReward = 0;
		int tempCurrentState = paraStartState;

		tempCurrentRoute[tempCurrentRouteLength] = tempCurrentState;
		tempCurrentRouteLength++;

		int tempNextState;
		while (!environment.isFinalState(tempCurrentState)) {
			double tempMax = -Double.MAX_VALUE;
			tempNextState = -1;
			int tempAction = -1;
			// Choose the current best move.
			for (int i = 0; i < environment.getValidActions(tempCurrentState).length; i++) {
				tempAction = environment.getValidActions(tempCurrentState)[i];
				if (tempMax < qualityMatrix[tempCurrentState][tempAction]) {
					tempMax = qualityMatrix[tempCurrentState][tempAction];
					tempNextState = environment.transitionMatrix[tempCurrentState][tempAction];
				} // Of if
			} // Of for i

			// Prepare for the next state.
			tempCurrentState = tempNextState;
			tempCurrentRoute[tempCurrentRouteLength] = tempCurrentState;
			tempCurrentRouteLength++;
		} // Of while

		int[] resultRoute = new int[tempCurrentRouteLength];
		for (int i = 0; i < tempCurrentRouteLength; i++) {
			resultRoute[i] = tempCurrentRoute[i];
		} // Of for i

		return resultRoute;
	}// Of greedyRouting

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = environment.toString();
		resultString += "\r\nThe average reward is: " + getAverageReward() + "\r\n";

		return resultString;
	}// Of toString

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		// SimpleQLearner tempQLearning = new
		// SimpleQLearner(SimpleQLearner.EXAMPLE_ONE_MAZE);
		Environment tempMaze = new Maze(Maze.EXAMPLE_TWO_MAZE);
		Learner tempQLearning = new SimpleQLearner(tempMaze);
		// tempQLearning.setGamma(0.9);
		tempQLearning.learn(100);
		System.out.println("\r\nWith simple implementation: ");
		System.out.println(tempQLearning);

		/**
		 * String tempString = ""; try { tempString =
		 * tempQLearning.findBestRoute(24); } catch (Exception ee){ tempString =
		 * ee.toString(); }//Of try
		 * 
		 * System.out.println(tempString);
		 */
	}// Of main
} // Of class SimpleQLearner
