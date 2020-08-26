package qlearning.agent;

import java.util.Arrays;

import qlearning.action.*;
import common.*;
import qlearning.environment.Environment;

/**
 * The super-class of any quality agent.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 20, 2020.
 * @version 1.0
 */

public abstract class QAgent extends Agent {

	/**
	 * The quality matrix.
	 */
	double[][] qualityMatrix;

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
	public QAgent(Environment paraEnvironment) {
		super(paraEnvironment);
		gamma = 0.99;
		alpha = 0.1;
		qualityMatrix = new double[numStates][numActions];
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
	 * Reset for the next run.
	 ****************** 
	 */
	public void reset() {
		for (int i = 0; i < qualityMatrix.length; i++) {
			for (int j = 0; j < qualityMatrix[i].length; j++) {
				qualityMatrix[i][j] = 0;
			} // Of for j
		} // Of for i
	}// Of reset

	/**
	 ****************** 
	 * Learn.
	 * 
	 * @param paraEpisodes
	 *            The number of rounds.
	 ****************** 
	 */
	public void learn(int paraEpisodes) {
		reset();
		// SimpleTools.variableTracking = true;

		rewardArray = new double[paraEpisodes];
		stepsArray = new int[paraEpisodes];

		// Step 1. Randomly pick a state as the start state.
		int tempStartState = environment.getStartState();
		int tempAction = 0, tempNextState;

		// Step 2. Run the given rounds.
		int[] wallTimesArray = new int[paraEpisodes];
		for (int i = 0; i < paraEpisodes; i++) {
			SimpleTools.variableTrackingOutput("Episode " + i);
			environment.reset();
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
				try {
					tempAction = selectAction(qualityMatrix[tempCurrentState], tempValidActions);
				} catch (NoValidActionException ee) {
					rewardArray[i] = Environment.PENALTY_VALUE;
					break;
				} // Of try

				try {
					environment.step(tempAction);
				} catch (IllegalActionException ee) {
					System.out.println("QAgent: " + ee);
					System.exit(0);
				} // Of try
				tempNextState = environment.getCurrentState();
				rewardArray[i] += environment.getCurrentReward();
				tempFinished = environment.isFinished();

				// SimpleTools.variableTrackingOutput("Finished? " +
				// tempFinished);

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
				double tempReward = environment.getCurrentReward();
				if (tempReward == Environment.PENALTY_VALUE) {
					// Do not go to this trap next time
					qualityMatrix[tempCurrentState][tempAction] = tempReward;
				} else {
					double tempDelta = tempReward + gamma * tempMaxFutureReward;
					double tempOldQuality = qualityMatrix[tempCurrentState][tempAction];
					// Attention: it should be updated even if tempDelta <
					// tempOldQuality
					qualityMatrix[tempCurrentState][tempAction] = tempOldQuality
							+ alpha * (tempDelta - tempOldQuality);
				} // Of if

				// Step 2.2.4. Prepare for the next step.
				tempCurrentState = tempNextState;
			} // Of while

			wallTimesArray[i] = Common.wallTimes;
			SimpleTools.variableTrackingOutput("The environment is: " + environment.toString());
		} // Of for i

		//System.out.println("Wall times: " + Arrays.toString(wallTimesArray));
		//System.out.println("\r\nFinally, Q = " + Arrays.deepToString(qualityMatrix));
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
	 * @throws Exception
	 *             if no valid action exists.
	 ****************** 
	 */
	public abstract int selectAction(double[] paraRewardArray, int[] paraValidActions)
			throws NoValidActionException;

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
		// if (environment.isTrapState(paraStartState)) {
		// throw new Exception("State " + paraStartState + " is a trap state.");
		// } // Of if

		int tempCurrentRouteLength = 0;

		// currentRouteReward = 0;
		int tempCurrentState = paraStartState;

		tempCurrentRoute[tempCurrentRouteLength] = tempCurrentState;
		tempCurrentRouteLength++;

		int tempNextState;
		environment.setCurrentState(tempCurrentState);
		boolean tempFinished = false;
		while (!tempFinished) {
			double tempMax = -Double.MAX_VALUE;
			tempNextState = -1;
			int tempAction = -1;
			int tempBestAction = -1;
			// Choose the current best move.
			for (int i = 0; i < environment.getValidActions(tempCurrentState).length; i++) {
				tempAction = environment.getValidActions(tempCurrentState)[i];
				if (tempMax < qualityMatrix[tempCurrentState][tempAction]) {
					tempMax = qualityMatrix[tempCurrentState][tempAction];
					tempBestAction = tempAction;
					tempNextState = environment.transitionMatrix[tempCurrentState][tempAction];
				} // Of if
			} // Of for i

			environment.step(tempBestAction);
			tempFinished = environment.isFinished();

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
	 * Go one step.
	 * 
	 * @param paraCurrentState
	 *            The current state.
	 ****************** 
	public void step(int paraCurrentState) {
		int tempAction = 0;
		int tempNextState = 0;

		// State 2.2.1. Randomly go one valid step.
		// The implementation depends on the quality value of actions.
		int[] tempValidActions = environment.getValidActions();
		try {
			tempAction = selectAction(qualityMatrix[paraCurrentState], tempValidActions);
		} catch (NoValidActionException ee) {
			System.out.println("QAgent: " + ee);
			System.exit(0);
		} // Of try

		try {
			environment.step(tempAction);
		} catch (IllegalActionException ee) {
			System.out.println("QAgent: " + ee);
			System.exit(0);
		} // Of try
		tempNextState = environment.getCurrentState();

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
		double tempReward = environment.getCurrentReward();
		if (tempReward == Environment.PENALTY_VALUE) {
			// Do not go to this trap next time
			qualityMatrix[paraCurrentState][tempAction] = tempReward;
		} else {
			double tempDelta = tempReward + gamma * tempMaxFutureReward;
			double tempOldQuality = qualityMatrix[paraCurrentState][tempAction];
			// Attention: it should be updated even if tempDelta <
			// tempOldQuality
			qualityMatrix[paraCurrentState][tempAction] = tempOldQuality
					+ alpha * (tempDelta - tempOldQuality);
		} // Of if
	}// Of step
	 */

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a QAgent.\r\n";

		return resultString;
	}// Of toString

} // Of class QAgent