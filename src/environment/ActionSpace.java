package environment;

/**
 * The super-class of any action space.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public abstract class ActionSpace {
	/**
	 * Number of actions.
	 */
	int numActions;
	
	/**
	 * Actions.
	 */
	int[] actions;
	
	/**
	 ****************** 
	 * Getter.
	 * @return The number of actions.
	 ****************** 
	 */
	public int getNumActions(){
		return numActions;
	}//Of getNumStates
	
	/**
	 ****************** 
	 * Getter the action.
	 * @param paraActionIndex The given action index.
	 * @return The number of actions.
	 ****************** 
	 */
	public int getAction(int paraActionIndex){
		return actions[paraActionIndex];
	}//Of getAction
	
} //Of class ActionSpace
