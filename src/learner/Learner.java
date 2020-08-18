package learner;

import environment.Environment;

/**
 * The super-class of any learner.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public abstract class Learner {
	/**
	 * The environment for the learner.
	 */
	Environment environment;

	/**
	 * The number of states, the same as that of the environment.
	 */
	int numStates;

	/**
	 * The number of actions, the same as that of the environment's actionSpace.
	 */
	int numActions;

	/**
	 * The quality matrix.
	 */
	double[][] qualityMatrix;

	/**
	 * The reward for each episode.
	 */
	double[] rewardArray;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public Learner(Environment paraEnvironment) {
		environment = paraEnvironment;
		numStates = environment.getNumStates();
		numActions = environment.getActionSpace().getNumActions();
		qualityMatrix = new double[numStates][numActions];
		rewardArray = null;
	}// Of the first constructor

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The rewards for episodes.
	 ****************** 
	 */
	public double[] getRewardArray() {
		return rewardArray;
	}// Of getRewardArray

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The average reward for episodes.
	 ****************** 
	 */
	public double getAverageReward() {
		double tempTotalReward = 0;
		for (int i = 0; i < rewardArray.length; i++) {
			tempTotalReward += rewardArray[i];
		}//Of for i
		
		double resultAverageReward = tempTotalReward / rewardArray.length;
		return resultAverageReward;
	}// Of getAverageReward

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
	 *            The number of episodes.
	 ****************** 
	 */
	public abstract void learn(int paraEpisodes);

	/**
	 ****************** 
	 * In each state use greedy selection for routing.
	 * 
	 * @param paraStartState The start state.
	 * @return The route information.
	 * @throws Exception if any.
	 ****************** 
	 */
	public abstract int[] greedyRouting(int paraStartState) throws Exception;

} // Of class Learner
