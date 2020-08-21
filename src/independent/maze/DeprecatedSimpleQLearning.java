package independent.maze;

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

public class DeprecatedSimpleQLearning extends DeprecatedMaze{

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
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public DeprecatedSimpleQLearning(int[][] paraMaze) {
		super(paraMaze);
		
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
	 * Existing version of train.
	 * 
	 * @param paraRounds
	 *            The number of rounds for learning.
	 ****************** 
	 */
	public double train(int paraRounds) {
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
						+ gamma * tempMaxFutureReward;
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
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		// SimpleQLearning tempQLearning = new SimpleQLearning(SimpleQLearning.EXAMPLE_ONE_MAZE);
		DeprecatedSimpleQLearning tempQLearning = new DeprecatedSimpleQLearning(DeprecatedSimpleQLearning.EXAMPLE_TWO_MAZE);
		tempQLearning.setGamma(0.9);
		tempQLearning.train(50);
		System.out.println("\r\nWith simple implementation: ");
		System.out.println(tempQLearning);
		
		String tempString = "";
		try {
			tempString = tempQLearning.findBestRoute(24);
		} catch (Exception ee){
			tempString = ee.toString();
		}//Of try
		
		System.out.println(tempString);
	}// Of main
} // Of class SimpleQLearning
