package learner;

import environment.Maze;

import java.util.Arrays;

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

public class SimpleQLearner extends Learner{

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
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public SimpleQLearner(Environment paraEnvironment) {
		super(paraEnvironment);
		gamma = 0.8;
	}// Of the first constructor

	/**
	 ****************** 
	 * Setter.
	 * @param paraGamma The given gamma value.
	 ****************** 
	 */
	public void setGamma(double paraGamma) {
		gamma = paraGamma;
	}//Of setGamma

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
		
		rewardArray = new double[paraEpisodes];
		
		double tempTotalReward = 0;
		//double tempCurrentRoundReward;

		// Step 1. Randomly pick a state as the start state.
		//int tempStartState = random.nextInt(numRows * numColumns);
		int tempStartState = 0;
		int tempAction, tempNextState;

		// Step 2. Run the given rounds.
		for (int i = 0; i < paraEpisodes; i++) {
			// Step 2.1. Initialize. Each time start from the same state.
			rewardArray[i] = 0;
			int tempCurrentState = tempStartState;
			System.out.print("\r\nStart: " + tempCurrentState);
			environment.setCurrentState(tempCurrentState);
			boolean tempFinished = false;

			// Step 2.2. Each time a final state should be reached.
			while (!tempFinished) {
				// State 2.2.1. Randomly go one valid step.
				int[] tempValidActions = environment.getValidActions();
				//int tempActionIndex = Environment.random.nextInt(validActions[tempCurrentState].length);
				
				//tempAction = environment.getActionSpace().getAction(tempActionIndex);
				//tempAction = validActions[tempCurrentState][tempActionIndex];
				
				int tempActionIndex = selectActionIndex(qualityMatrix[tempCurrentState], tempValidActions);
				tempAction = tempValidActions[tempActionIndex];
				
				tempFinished = environment.step(tempAction);
				tempNextState = environment.getCurrentState();
				//tempNextState = transitionMatrix[tempCurrentState][tempAction];
				
				rewardArray[i] += environment.getCurrentReward();
				//rewardArray[i] += rewardMatrix[tempCurrentState][tempAction];

				// Step 2.2.2. Calculate the best future reward according to the quality matrix.
				double tempMaxFutureReward = -10000;
				double tempFutureReward;
				tempValidActions = environment.getValidActions(tempNextState);
				//System.out.println("tempValidActions = " + Arrays.toString(tempValidActions));
				//double[] tempFutureRewards = new double[validActions[tempNextState].length];
				for (int j = 0; j < tempValidActions.length; j++) {
					// The reward of the next move.
					//System.out.println("tempNextState = " + tempNextState + ", j = " + j);
					tempFutureReward = qualityMatrix[tempNextState][tempValidActions[j]];
					if (tempMaxFutureReward < tempFutureReward) {
						tempMaxFutureReward = tempFutureReward;
					} // Of if
				} // Of for j

				// Step 2.2.3. Update the quality matrix.
				// Question: What is the use of gamma?
				qualityMatrix[tempCurrentState][tempAction] = 
						environment.getCurrentReward()
						+ gamma * tempMaxFutureReward;
				// qualityMatrix[tempCurrentState][tempAction] =
				// rewardMatrix[tempCurrentState][tempAction];

				// Step 2.2.4. Prepare for the next step.
				tempCurrentState = tempNextState;
				System.out.print(" -> " + tempCurrentState);
			} // Of while
		} // Of for i
		
		System.out.println("\r\nQ = " + Arrays.deepToString(qualityMatrix));
	} // Of learn
	
	/**
	 ****************** 
	 * Select an action index according to the given rewards.
	 * Random selection.
	 * 
	 * @param paraRewardArray The given reward array.
	 ****************** 
	 */
	public int selectActionIndex(double[] paraRewardArray, int[] paraValidActions) {
		int tempActionIndex = Environment.random.nextInt(paraValidActions.length);

		return tempActionIndex;
	}//Of selectActionIndex

	/**
	 ****************** 
	 * Find the best route according to the quality matrix.
	 * 
	 * @param paraStartState
	 *            The starting state.
	 * @throws Exception if the given state is a trap state.
	 ****************** 
	public String findBestRoute(int paraStartState) throws Exception {
		if (environment.isTrapState(paraStartState)) {
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
	 */

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = environment.toString();
		resultString += "\r\nThe average reward is: " + getAverageReward();
		
		return resultString;
	}//Of toString

		/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		// SimpleQLearner tempQLearning = new SimpleQLearner(SimpleQLearner.EXAMPLE_ONE_MAZE);
		Environment tempMaze = new Maze(Maze.EXAMPLE_TWO_MAZE);
		Learner tempQLearning = new SimpleQLearner(tempMaze);
		//tempQLearning.setGamma(0.9);
		tempQLearning.learn(100);
		System.out.println("\r\nWith simple implementation: ");
		System.out.println(tempQLearning);
		
		/**
		String tempString = "";
		try {
			tempString = tempQLearning.findBestRoute(24);
		} catch (Exception ee){
			tempString = ee.toString();
		}//Of try
		
		System.out.println(tempString);
		*/
	}// Of main
} // Of class SimpleQLearner
