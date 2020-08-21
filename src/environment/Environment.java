package environment;

import java.util.Random;

//import action.ActionSpace;
import action.IllegalActionException;

/**
 * The super-class of any environment.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public abstract class Environment {
	/**
	 * Random generator. Maybe moved to common.Common.java.
	 */
	public static Random random = new Random();

	/**
	 * Number of states.
	 */
	int numStates;

	/**
	 * Action space. May be useful in the future.
	ActionSpace actionSpace;
	 */

	/**
	 * Number of actions.
	 */
	int numActions;

	/**
	 * The reward matrix. The number of rows is numStates, and the number of
	 * columns is numActions.
	 */
	int[][] rewardMatrix;

	/**
	 * The transition matrix. The number of rows is numStates, and the number of
	 * columns is numActions.
	 */
	public int[][] transitionMatrix;

	/**
	 * The current state of the environment.
	 */
	int currentState;

	/**
	 * The reward for win, reaching the maze exit, etc.
	 */
	public static final int REWARD_VALUE = 100;

	/**
	 * The penalty for lose, reaching the trap state, etc.
	 */
	public static final int PENALTY_VALUE = -100;
	
	/**
	 * The current reward with the action just taken.
	 */
	int currentReward;

	/**
	 * The valid actions for each state. It is determined by the size of the
	 * environment.
	 */
	int[][] validActions;

	/**
	 * Start state.
	 */
	int startState;

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The number of states.
	 ****************** 
	 */
	public int getNumStates() {
		return numStates;
	}// Of getNumStates

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The number of actions.
	 ****************** 
	 */
	public int getNumActions() {
		return numActions;
	}// Of getNumActions

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The current state.
	 ****************** 
	 */
	public int getCurrentState() {
		return currentState;
	}// Of getCurrentState

	/**
	 ****************** 
	 * Setter.
	 * 
	 * @param paraState
	 *            The given state.
	 ****************** 
	 */
	public void setCurrentState(int paraState) {
		currentState = paraState;
	}// Of setCurrentStates

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The current reward.
	 ****************** 
	 */
	public int getCurrentReward() {
		return currentReward;
	}// Of getCurrentReward

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The start state.
	 ****************** 
	 */
	public int getStartState() {
		return startState;
	}// Of getStartState

	/**
	 ****************** 
	 * Setter.
	 * 
	 * @param The
	 *            start state.
	 ****************** 
	 */
	public void setStartState(int paraStartState) {
		startState = paraStartState;
	}// Of setStartState

	/**
	 ****************** 
	 * Get valid actions of the current state.
	 * 
	 * @return The actions.
	 ****************** 
	 */
	public int[] getValidActions() {
		return getValidActions(currentState);
	}// Of getValidActions

	/**
	 ****************** 
	 * Get valid actions of the given state.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The actions.
	 ****************** 
	 */
	public int[] getValidActions(int paraState) {
		return validActions[paraState];
	}// Of getValidActions

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The action space.
	 ****************** 
	public ActionSpace getActionSpace() {
		return actionSpace;
	}// Of getActionSpace
	 */

	/**
	 ****************** 
	 * Reset the environment.
	 ****************** 
	 */
	public abstract void reset();

	/**
	 ****************** 
	 * Is the current game finished?
	 * 
	 * @return True if it is.
	 ****************** 
	 */
	public abstract boolean isFinished();

	/**
	 ****************** 
	 * Get state reward value.
	 * 
	 * @param paraState
	 *            The given state.
	 * @return The reward value for the state.
	 ****************** 
	 */
	public abstract int getStateRewardValue(int paraState);

	/**
	 ****************** 
	 * Go one step with the given action. The new state and the reward should be
	 * retrieved using other methods.
	 * 
	 * @param paraAction
	 *            The given action.
	 * @return Whether or not the process comes an end.
	 * @throws IllegalActionException
	 *             if the action is illegal.
	 * @see #getCurrentState()
	 * @see #getCurrentReward()
	 ****************** 
	 */
	public abstract void step(int paraAction) throws IllegalActionException;

} // Of class Environment
