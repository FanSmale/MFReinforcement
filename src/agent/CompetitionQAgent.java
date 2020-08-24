package agent;

import environment.Maze;

import java.util.Arrays;

import action.IllegalActionException;
import action.NoValidActionException;
import common.Common;
import common.SimpleTools;
import environment.CompetitionEnvironment;
import environment.Environment;

/**
 * A Competing QAgent which randomly select action.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 24, 2020.<br>
 *         Last modified: August 24, 2020.
 * @version 1.0
 */

public class CompetitionQAgent extends QAgent {
	/**
	 * Player number.
	 */
	int player;

	/**
	 * The number of updates for each state.
	 */
	int[] stateNumUpdatesArray;
	
	/**
	 * Player number.
	 */
	CompetitionQAgent competitor;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public CompetitionQAgent(CompetitionEnvironment paraEnvironment, int paraPlayer) {
		super(paraEnvironment);
		player = paraPlayer;
		stateNumUpdatesArray = new int[numStates];
	}// Of the first constructor
	
	/**
	 ****************** 
	 * Set the competitor.
	 * 
	 * @param paraCompetitor
	 *            The given competitor.
	 ****************** 
	 */
	public void setCompetitor(CompetitionQAgent paraCompetitor) {
		competitor = paraCompetitor;
	}// Of setCompetitor
	

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
		//System.out.println("Selecting from " + Arrays.toString(paraRewardArray) + " and "
		//		+ Arrays.toString(paraValidActions));
		// Step 1. Compress reward array.
		double[] tempCompressedRewardArray = new double[paraValidActions.length];
		for (int i = 0; i < tempCompressedRewardArray.length; i++) {
			tempCompressedRewardArray[i] = paraRewardArray[paraValidActions[i]];
		} // Of for i

		// Step 4. Compute an random index according to the valid reward array.
		//int tempIndex = WeightedRandomQAgent.getWeightedRandomIndex(tempCompressedRewardArray,
		//		WeightedRandomQAgent.PROBABILITY_MIN_VALUE);
		
		int tempIndex = getRandomMaxIndex(tempCompressedRewardArray);

		// Step 5. The action corresponds to the index.
		int resultBestAction = paraValidActions[tempIndex];

		return resultBestAction;
	}// Of selectAction

	/**
	 ****************** 
	 * Randomly get an index of the maximal value.
	 * 
	 * @param paraArray
	 *            The given array, which may contain multiple maximal values.
	 * @return An index of the maximal value.
	 ****************** 
	 */
	public int getRandomMaxIndex(double[] paraArray) {
		double tempMaxValue = -Double.MAX_VALUE;
		int resultIndex = -1;
		int tempNumMax = 0;
		int[] tempIndexArray = new int[paraArray.length];
		for (int i = 0; i < paraArray.length; i++) {
			if (tempMaxValue < paraArray[i]) {
				tempMaxValue = paraArray[i];
				resultIndex = i;
				tempNumMax = 0;
				
				tempIndexArray[tempNumMax] = i;
				tempNumMax ++;
			} else if (tempMaxValue == paraArray[i]){
				tempIndexArray[tempNumMax] = i;
				tempNumMax ++;
			}//Of if
		}//Of for i
		
		//Only one max value.
		if (tempNumMax == 1) {
			return resultIndex;
		}//Of if
		
		//Randomly choose one.
		int tempIndex = Environment.random.nextInt(tempNumMax);
		resultIndex = tempIndexArray[tempIndex];
		
		return resultIndex;
	}//Of getMaxIndex
	
	/**
	 ****************** 
	 * Go one step.
	 * 
	 * @param paraCurrentState
	 *            The current state.
	 ****************** 
	 */
	public void step(int paraCurrentState) {
		stateNumUpdatesArray[paraCurrentState]++;
		//System.out.println("player " + player);
		
		//System.out.println("Before update, qualityMatrix[" + paraCurrentState + "] = "
		//		+ Arrays.toString(qualityMatrix[paraCurrentState]));


		int tempAction = 0;
		int tempNextState = 0;

		// State 2.2.1. Randomly go one valid step.
		// The implementation depends on the quality value of actions.
		int[] tempValidActions = environment.getValidActions();
		try {
			tempAction = selectAction(qualityMatrix[paraCurrentState], tempValidActions);
			System.out.print(", " + tempAction);
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
		double tempMaxFuturePenaly = 0;
		double tempFuturePenaly;
		tempValidActions = environment.getValidActions(tempNextState);

		double[] tempCompetitorQualityArray = competitor.qualityMatrix[tempNextState];
		
		//System.out.println("Future: qualityMatrix[" + tempNextState + "] = "
		//		+ Arrays.toString(tempCompetitorQualityArray));
		for (int j = 0; j < tempValidActions.length; j++) {
			tempFuturePenaly = tempCompetitorQualityArray[tempValidActions[j]];
			if (tempMaxFuturePenaly < tempFuturePenaly) {
				tempMaxFuturePenaly = tempFuturePenaly;
			} // Of if
		} // Of for j

		// Step 2.2.3. Update the quality matrix.
		// The use of gamma and alpha might not be correct.
		double tempReward = environment.getCurrentReward();
		//if (tempReward == Environment.REWARD_VALUE) {
		//	System.out.println("Environment.REWARD_VALUE");
		//} else if (tempReward == Environment.PENALTY_VALUE) {
		//	System.out.println("Environment.PENALTY_VALUE");
		//} // Of if

		if (player == 2) {
			tempReward = -tempReward;
		} // Of if

		// if (tempReward == Environment.PENALTY_VALUE) {
		// Do not go to this trap next time
		// qualityMatrix[paraCurrentState][tempAction] = tempReward;
		// } else {
		double tempDelta = tempReward - gamma * tempMaxFuturePenaly;
		double tempOldQuality = qualityMatrix[paraCurrentState][tempAction];
		// Attention: it should be updated even if tempDelta <
		// tempOldQuality
		//System.out.println("tempOldQuality = " + tempOldQuality + ", tempDelta = " + tempDelta);
		qualityMatrix[paraCurrentState][tempAction] = tempOldQuality
				+ alpha * (tempDelta - tempOldQuality);
		//System.out.println("qualityMatrix[" + paraCurrentState + "] = "
		//		+ Arrays.toString(qualityMatrix[paraCurrentState]));
		//System.out.println("The state has been updated: " + stateNumUpdatesArray[paraCurrentState]
		//		+ " times.");
		// } // Of if
	}// Of step

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

} // Of class CompetitionQAgent
