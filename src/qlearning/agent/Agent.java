package qlearning.agent;

import qlearning.environment.Environment;

/**
 * The super-class of any angent.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 20, 2020.
 * @version 1.0
 */

public abstract class Agent {
	/**
	 * The environment for the agent.
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
	 * The reward for each episode. For statistics and output.
	 */
	double[] rewardArray;

	/**
	 * The steps for each episode. For statistics and output.
	 */
	int[] stepsArray;
	
	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public Agent(Environment paraEnvironment) {
		environment = paraEnvironment;
		numStates = environment.getNumStates();
		numActions = environment.getNumActions();
		// numActions = environment.getActionSpace().getNumActions();
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
	 * @return The steps for episodes.
	 ****************** 
	 */
	public int[] getStepsArray() {
		return stepsArray;
	}// Of getStepsArray

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
		} // Of for i

		double resultAverageReward = tempTotalReward / rewardArray.length;
		return resultAverageReward;
	}// Of getAverageReward

	/**
	 ****************** 
	 * Reset for the next run.
	 ****************** 
	 */
	public abstract void reset();

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
	 * Go one step.
	 * @param paraState The current state.
	 ****************** 
	public abstract void step(int paraCurrentState);
	 */

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
	public abstract int[] greedyRouting(int paraStartState) throws Exception;

} // Of class Agent
