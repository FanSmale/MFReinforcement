package algorithm.maze;

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

public class ControlledRandomQLearning extends SimpleQLearning{

	/**
	 * The minimal probability value for the currently worse move.
	 */
	public static final double PROBABILITY_MIN_VALUE = 0.1;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public ControlledRandomQLearning(int[][] paraMaze) {
		super(paraMaze);
	}// Of the first constructor

	/**
	 ****************** 
	 * Train.
	 * 
	 * @param paraRounds
	 *            The number of rounds for learning.
	 * @return The average value of gain.
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
	 * Get an index of the array, higher values have higher probability.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraMinValue
	 *            The minimal possible value.
	 * @return An index of the array.
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
		ControlledRandomQLearning tempQLearning = new ControlledRandomQLearning(ControlledRandomQLearning.EXAMPLE_ONE_MAZE);

		double[] tempDoubleMatrix = { -10, 0, 10 };
		for (int i = 0; i < 10; i++) {
			int tempIndex = tempQLearning.getWeightedRandomIndex(tempDoubleMatrix, 0.1);
			System.out.println("The index is: " + tempIndex);
		} // Of for i
	}// Of getWeightedRandomIndexTest

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		ControlledRandomQLearning tempQLearning = new ControlledRandomQLearning(ControlledRandomQLearning.EXAMPLE_TWO_MAZE);
		tempQLearning.setGamma(0.8);
		tempQLearning.train(100);

		System.out.println("\r\nWith my implementation: ");
		System.out.println(tempQLearning);
		
		String tempString = "";
		try {
			tempString = tempQLearning.findBestRoute(5);
		} catch (Exception ee){
			tempString = ee.toString();
		}//Of try
		System.out.println(tempString);

		// getWeightedRandomIndexTest();
	}// Of main
} // Of class ControlledRandomQLearning
