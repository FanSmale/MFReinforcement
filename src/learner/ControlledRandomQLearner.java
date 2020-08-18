package learner;

import java.util.Arrays;

import common.SimpleTools;
import environment.Environment;

public class ControlledRandomQLearner extends SimpleQLearner {

	/**
	 * The minimal probability value for the currently worse move.
	 */
	public static final double PROBABILITY_MIN_VALUE = 0.2;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public ControlledRandomQLearner(Environment paraEnvironment) {
		super(paraEnvironment);
	}// Of the first constructor

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
		//Step 1. Compress reward array.
		double[] tempCompressedRewardArray = new double[paraValidActions.length];
		for (int i = 0; i < tempCompressedRewardArray.length; i++) {
			tempCompressedRewardArray[i] = paraRewardArray[paraValidActions[i]];
		}//Of for i
		
		//Step 2. Trap states are also invalid.
		int tempNumInvalidActions = 0;
		for (int i = 0; i < tempCompressedRewardArray.length; i++) {
			if (tempCompressedRewardArray[i] < Environment.TRAP_VALUE + 1e-6) {
				tempNumInvalidActions ++;
			}//Of if
		}//Of for i
		
		//Step 3. Construct new arrays if necessary.
		double[] tempValieRewardArray = tempCompressedRewardArray;
		int[] tempValidActions = paraValidActions;
		if (tempNumInvalidActions > 0) {
			int tempNewLength = tempCompressedRewardArray.length - tempNumInvalidActions;
			tempValieRewardArray = new double[tempNewLength];
			tempValidActions = new int[tempNewLength];
			
			int tempCounter = 0;
			for (int i = 0; i < paraRewardArray.length; i++) {
				if (tempCompressedRewardArray[i] >= Environment.TRAP_VALUE + 1e-6) {
					tempValieRewardArray[tempCounter] = tempCompressedRewardArray[i];
					tempValidActions[tempCounter] = paraValidActions[i];
					tempCounter ++;
				}//Of if
			}//Of for i
		}//Of if
		
		//Step 4. Compute an random index according to the valid reward array.
		int tempIndex = getWeightedRandomIndex(tempValieRewardArray, PROBABILITY_MIN_VALUE);
		
		//Step 5. The action corresponds to the index.
		int resultBestAction = tempValidActions[tempIndex];
		
		return resultBestAction;
	}//Of selectAction
	
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
		double tempRandom = Environment.random.nextDouble();
		int resultIndex = -1;
		for (int i = 0; i < tempScaleTotal.length; i++) {
			if (tempRandom <= tempScaleTotal[i]) {
				resultIndex = i;
				break;
			} // Of if
		} // Of for i
		return resultIndex;
	}// Of getWeightedRandomIndex
} //Of class ControlledRandomQLearner
