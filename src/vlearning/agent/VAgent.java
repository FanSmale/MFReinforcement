package vlearning.agent;

import java.util.Arrays;
import common.*;
//import qlearning.environment.Environment;
import vlearning.environment.VTicTacToe;

/**
 * The super-class of any value agent.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 26, 2020.<br>
 *         Last modified: August 26, 2020.
 * @version 1.0
 */

public class VAgent {

	/**
	 * The minimal probability value for the currently worse move.
	 */
	public static final double PROBABILITY_MIN_VALUE = 0.02;

	/**
	 * The initial value array, only compute once.
	 */
	double[] initialValueArray;

	/**
	 * The value array.
	 */
	double[] valueArray;

	/**
	 * The environment.
	 */
	VTicTacToe environment;

	/**
	 * The player's symbol/color/side.
	 */
	int symbol;

	/**
	 * The gamma value
	 */
	double gamma;

	/**
	 * The alpha value
	 */
	double alpha;

	/**
	 * Is the training stage.
	 */
	boolean trainingStage;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public VAgent(VTicTacToe paraEnvironment, int paraSymbol) {
		environment = paraEnvironment;
		symbol = paraSymbol;

		computeInitialValueArray();
		valueArray = new double[environment.getNumStates()];
		reset();

		trainingStage = true;

		gamma = 0.99;
		alpha = 0.1;
	}// Of the first constructor

	/**
	 ****************** 
	 * Compute initial value array. It is time consuming.
	 ****************** 
	 */
	private void computeInitialValueArray() {
		int tempSituation;
		initialValueArray = new double[environment.getNumStates()];
		for (int i = 0; i < initialValueArray.length; i++) {
			tempSituation = environment.computeGameSituation(i);
			if (tempSituation == VTicTacToe.UNFINISHED) {
				initialValueArray[i] = 0.5;
			} else if (tempSituation == VTicTacToe.TIE) {
				initialValueArray[i] = 0.5;
			} else if (tempSituation == symbol) {
				initialValueArray[i] = 1.0; // Winner
			} else {
				initialValueArray[i] = 0.0; // Loser
			} // Of if
		} // Of for i
	}// Of computeInitialValueArray

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
	 * Setter.
	 * 
	 * @param paraTrainingStage
	 *            Training or testing stage.
	 ****************** 
	 */
	public void setTrainingStage(boolean paraTrainingStage) {
		trainingStage = paraTrainingStage;
	}// Of setTrainingStage

	/**
	 ****************** 
	 * Reset for the next run.
	 ****************** 
	 */
	public void reset() {
		for (int i = 0; i < valueArray.length; i++) {
			valueArray[i] = initialValueArray[i];
		} // Of for i
	}// Of reset

	/**
	 ****************** 
	 * Compute the next state.
	 * 
	 * @param paraCurrentState
	 *            The current state.
	 * @param paraAction
	 *            The current action. It's validity is not checked here.
	 * @return The next state.
	 ****************** 
	 */
	int computeNextState(int paraCurrentState, int paraAction) {
		int tempBase = 1;
		for (int j = 0; j < paraAction; j++) {
			tempBase *= 3;
		} // Of for

		int resultNextState = paraCurrentState + symbol * tempBase;

		return resultNextState;
	}// Of computeNextState

	/**
	 ****************** 
	 * Go one step.
	 * 
	 * @return The game situation.
	 ****************** 
	 */
	public int step() {
		// Step 1. Which actions are valid? What are the values of corresponding
		// states?
		int[] tempValidActionArray = environment.getValidActions();
		double[] tempValueArray = new double[tempValidActionArray.length];
		int tempCurrentState = environment.getCurrentState();
		int tempNextState = 0;

		for (int i = 0; i < tempValueArray.length; i++) {
			tempNextState = computeNextState(tempCurrentState, tempValidActionArray[i]);
			tempValueArray[i] = valueArray[tempNextState];
		} // Of for i

		// Step 2. Choose one action to take.
		int tempAction = selectAction(tempValueArray, tempValidActionArray);
		tempNextState = computeNextState(tempCurrentState, tempAction);

		// Step 3. Tell the environment to change.
		try {
			environment.step(tempAction);
		} catch (Exception ee) {
			System.out.println("Error occurred in VAgent.step(): \r\n" + " The action " + tempAction
					+ " is invalid. " + ee);
		} // Of try

		// Step 5. Return the environment's situation.
		return environment.getGameSituation();
	}// Of step

	/**
	 ****************** 
	 * Backup for this route, update the value array.
	 * This is the core code.
	 ****************** 
	 */
	public void backup() {
		int tempCurrentState;
		int tempNextState;

		int tempRouteLength = environment.getCurrentRouteLength();
		int[] tempRouteStates = environment.getCurrentRouteStates();
		for (int i = tempRouteLength - 2; i >= 0; i--) {
			tempNextState = tempRouteStates[i + 1];
			tempCurrentState = tempRouteStates[i];

			double tempError = valueArray[tempNextState] - valueArray[tempCurrentState];
			valueArray[tempCurrentState] += alpha * tempError;
		} // Of for i
	}// Of backup

	/**
	 ****************** 
	 * Select an action according to the given reward array. Actions
	 * corresponding to trap states (which can be observed by the reward value)
	 * will not be selected.
	 * 
	 * @param paraValueArray
	 *            The given reward array.
	 * @param paraValidActions
	 *            The valid actions.
	 * @return The selected action.
	 ****************** 
	 */
	public int selectAction(double[] paraValueArray, int[] paraValidActions) {
		if (trainingStage) {
			return selectActionWeightedRandom(paraValueArray, paraValidActions);
		} else {
			return selectBestAction(paraValueArray, paraValidActions);
		} // Of if
	}// Of selectAction

	/**
	 ****************** 
	 * Select an action according to the given reward array. Actions
	 * corresponding to trap states (which can be observed by the reward value)
	 * will not be selected.
	 * 
	 * @param paraValueArray
	 *            The given reward array.
	 * @param paraActionArray
	 *            The valid actions.
	 * @return The selected action.
	 ****************** 
	 */
	public static int selectActionWeightedRandom(double[] paraValueArray, int[] paraActionArray) {
		// Step 1. Scan the first time to obtain max/min/total values.
		double tempMax = -Double.MAX_VALUE;
		double tempMin = Double.MAX_VALUE;
		double tempTotal = 0;
		for (int i = 0; i < paraValueArray.length; i++) {
			if (tempMax < paraValueArray[i]) {
				tempMax = paraValueArray[i];
			} // Of if

			if (tempMin > paraValueArray[i]) {
				tempMin = paraValueArray[i];
			} // Of if
		} // Of for i

		// Important: Handle the situation that all elements are the same.
		if (tempMax == tempMin) {
			tempMax = tempMin + 0.01;
		} // Of if

		// Step 2. Calculate their probabilities.
		double[] tempArray = new double[paraValueArray.length];
		for (int i = 0; i < paraValueArray.length; i++) {
			tempArray[i] = PROBABILITY_MIN_VALUE
					+ (paraValueArray[i] - tempMin) / (tempMax - tempMin);

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
		double tempRandom = Common.random.nextDouble();
		// Important: Initialize as 0 instead of -1.
		int resultIndex = 0;
		for (int i = 0; i < tempScaleTotal.length; i++) {
			if (tempRandom <= tempScaleTotal[i]) {
				resultIndex = i;
				break;
			} // Of if
		} // Of for i

		// Step 5. The action corresponds to the index.
		int resultBestAction = paraActionArray[resultIndex];

		return resultBestAction;
	}// Of selectActionWeightedRandom

	/**
	 ****************** 
	 * Select the best action action according to the given value array.
	 * 
	 * @param paraValueArray
	 *            The given reward array.
	 * @param paraActionArray
	 *            The valid actions.
	 * @return The selected action.
	 ****************** 
	 */
	public static int selectBestAction(double[] paraValueArray, int[] paraActionArray) {
		int resultBestAction = -1;
		double tempMaxValue = -1;

		for (int i = 0; i < paraValueArray.length; i++) {
			if (tempMaxValue < paraValueArray[i]) {
				tempMaxValue = paraValueArray[i];
				resultBestAction = paraActionArray[i];
			} // Of if
		} // Of for i

		//SimpleTools.variableTrackingOutput("\r\nFrom " + Arrays.toString(paraValueArray) + " and "
		//		+ Arrays.toString(paraActionArray) + ", best action: " + resultBestAction + "\r\n");

		return resultBestAction;
	}// Of selectBestAction

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a VAgent.\r\n";

		return resultString;
	}// Of toString

} // Of class VAgent