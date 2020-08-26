package qlearning.agent;

import java.util.Arrays;

import qlearning.action.NoValidActionException;
import qlearning.environment.*;

/**
 * A Q-agent with the weighted random approach for action selection.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 20, 2020.
 * @version 1.0
 */

public class WeightedRandomQAgent extends QAgent {

	/**
	 * The minimal probability value for the currently worse move.
	 */
	public static final double PROBABILITY_MIN_VALUE = 0.02;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public WeightedRandomQAgent(Environment paraEnvironment) {
		super(paraEnvironment);
	}// Of the first constructor

	/**
	 ****************** 
	 * Select an action according to the given reward array. Actions
	 * corresponding to trap states (which can be observed by the reward value)
	 * will not be selected.
	 * 
	 * @param paraRewardArray
	 *            The given reward array.
	 * @param paraValidActions
	 *            The valid actions.
	 * @return The selected action.
	 * @throws NoValidActionException
	 *             Since some times the other blank will win regardless the
	 *             choice.
	 ****************** 
	 */
	public int selectAction(double[] paraRewardArray, int[] paraValidActions)
			throws NoValidActionException {
		return selectActionWeightedRandom(paraRewardArray, paraValidActions);
	}// Of selectAction

	/**
	 ****************** 
	 * Select an action according to the given reward array. Actions
	 * corresponding to trap states (which can be observed by the reward value)
	 * will not be selected.
	 * 
	 * @param paraRewardArray
	 *            The given reward array.
	 * @param paraValidActions
	 *            The valid actions.
	 * @return The selected action.
	 * @throws NoValidActionException
	 *             Since some times the other blank will win regardless the
	 *             choice.
	 ****************** 
	 */
	public static int selectActionWeightedRandom(double[] paraRewardArray, int[] paraValidActions)
			throws NoValidActionException {
		// Step 1. Compress reward array.
		double[] tempCompressedRewardArray = new double[paraValidActions.length];
		for (int i = 0; i < tempCompressedRewardArray.length; i++) {
			tempCompressedRewardArray[i] = paraRewardArray[paraValidActions[i]];
		} // Of for i

		// Step 2. Trap states are also invalid.
		int tempNumInvalidActions = 0;
		for (int i = 0; i < tempCompressedRewardArray.length; i++) {
			if (tempCompressedRewardArray[i] < Environment.PENALTY_VALUE + 1e-6) {
				tempNumInvalidActions++;
			} // Of if
		} // Of for i

		// Step 3. Construct new arrays if necessary.
		double[] tempValidRewardArray = tempCompressedRewardArray;
		int[] tempValidActions = paraValidActions;
		if (tempNumInvalidActions > 0) {
			int tempNewLength = tempCompressedRewardArray.length - tempNumInvalidActions;
			if (tempNewLength == 0) {
				throw new NoValidActionException("No action to choose at all.");
			} // Of if
			tempValidRewardArray = new double[tempNewLength];
			tempValidActions = new int[tempNewLength];

			int tempCounter = 0;
			for (int i = 0; i < tempCompressedRewardArray.length; i++) {
				if (tempCompressedRewardArray[i] >= Environment.PENALTY_VALUE + 1e-6) {
					tempValidRewardArray[tempCounter] = tempCompressedRewardArray[i];
					tempValidActions[tempCounter] = paraValidActions[i];
					tempCounter++;
				} // Of if
			} // Of for i
		} // Of if

		// Step 4. Compute an random index according to the valid reward array.
		int tempIndex = getWeightedRandomIndex(tempValidRewardArray, PROBABILITY_MIN_VALUE);

		// Step 5. The action corresponds to the index.
		int resultBestAction = tempValidActions[tempIndex];

		return resultBestAction;
	}// Of selectActionWeightedRandom

	/**
	 ****************** 
	 * Test the method.
	 ****************** 
	 */
	public static void selectActionTest() {
		// double[] tempRewardArray = {5, -100, 9, 3};
		double[] tempRewardArray = { 0.0, -100.0, 0.0, 0.0 };
		int[] tempActionArray = { 0, 1, 3 };

		int tempAction;
		int[] tempActionSelectionArray = new int[4];
		for (int i = 0; i < 100; i++) {
			try {
				tempAction = new WeightedRandomQAgent(new Maze(Maze.EXAMPLE_TWO_MAZE))
						.selectAction(tempRewardArray, tempActionArray);
				tempActionSelectionArray[tempAction]++;
			} catch (NoValidActionException ee) {
				System.out.println("NoValidActionException");
				break;
			} // Of try
		} // Of for i
		System.out.println("The action array is: " + Arrays.toString(tempActionSelectionArray));
	}// Of selectActionTest

	/**
	 ****************** 
	 * Get an index of the array, higher values have higher probability.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraMinValue
	 *            The minimal possible value to avoid probability 0 of the
	 *            samllest one.
	 * @return An index of the array.
	 ****************** 
	 */
	public static int getWeightedRandomIndex(double[] paraArray, double paraMinValue) {
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
		double tempRandom = Environment.random.nextDouble();
		// Important: Initialize as 0 instead of -1.
		int resultIndex = 0;
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
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		selectActionTest();
	}// Of main

} // Of class WeightedRandomQAgent
