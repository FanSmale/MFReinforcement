package environment;

import java.util.Random;

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
	 * Random generator.
	 */
	public static Random random = new Random();

	/**
	 * Number of states.
	 */
	int numStates;

	/**
	 * Action space.
	 */
	ActionSpace actionSpace;
	
	/**
	 * The reward matrix.
	 * The number of rows is numStates, and the number of columns is numActions.
	 */
	int[][] rewardMatrix;

	/**
	 * The transition matrix.
	 * The number of rows is numStates, and the number of columns is numActions.
	 */
	int[][] transitionMatrix;
	
	/**
	 * The current of the environment.
	 */
	int currentState;

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
	 * @return The number of states.
	 ****************** 
	 */
	public int getNumStates(){
		return numStates;
	}//Of getNumStates
	
	/**
	 ****************** 
	 * Getter.
	 * @return The current state.
	 ****************** 
	 */
	public int getCurrentState(){
		return currentState;
	}//Of getCurrentState

	/**
	 ****************** 
	 * Setter.
	 * @param paraState The given state.
	 ****************** 
	 */
	public void setCurrentState(int paraState){
		currentState = paraState;
	}//Of setCurrentStates

	/**
	 ****************** 
	 * Getter.
	 * @return The current reward.
	 ****************** 
	 */
	public int getCurrentReward(){
		return currentReward;
	}//Of getCurrentReward

	/**
	 ****************** 
	 * Getter.
	 * @return The start state.
	 ****************** 
	 */
	public int getStartState(){
		return startState;
	}//Of getStartState

	/**
	 ****************** 
	 * Setter.
	 * @param The start state.
	 ****************** 
	 */
	public void setStartState(int paraStartState){
	startState = paraStartState;
	}//Of setStartState

	/**
	 ****************** 
	 * Get valid actions of the current state.
	 * 
	 * @return The actions.
	 ****************** 
	 */
	public int[] getValidActions(){
		return getValidActions(currentState);
	}//Of getValidActions

	/**
	 ****************** 
	 * Get valid actions of the given state.
	 * 
	 * @param paraState The given state.
	 * @return The actions.
	 ****************** 
	 */
	public int[] getValidActions(int paraState){
		return validActions[paraState];
	}//Of getValidActions

	/**
	 ****************** 
	 * Getter.
	 * @return The action space.
	 ****************** 
	 */
	public ActionSpace getActionSpace(){
		return actionSpace;
	}//Of getActionSpace
	
	/**
	 ****************** 
	 * Select an action to take.
	 * @return The action.
	 ****************** 
	 */
	public abstract int selectAction();

	/**
	 ****************** 
	 * Go one step with the given action.
	 * The new state and the reward should be retrieved using other methods.
	 * 
	 * @param paraAction The given action.
	 * @return Whether or not the new state is a final state.
	 * @see #getCurrentState()
	 * @see #getCurrentReward()
	 ****************** 
	 */
	public abstract boolean step(int paraAction);
	
} // Of class Environment
