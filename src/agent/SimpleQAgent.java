package agent;

import environment.Maze;

import action.NoValidActionException;
import environment.Environment;

/**
 * A QAgent which randomly select action.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class SimpleQAgent extends QAgent {

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public SimpleQAgent(Environment paraEnvironment) {
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
	public int selectAction(double[] paraRewardArray, int[] paraValidActions) throws NoValidActionException{
		if (paraValidActions.length == 0) {
			throw new NoValidActionException("No action to choose at all.");
		}//Of if
		
		int tempActionIndex = Environment.random.nextInt(paraValidActions.length);

		return paraValidActions[tempActionIndex];
	}// Of selectActionIndex

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

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		// SimpleQAgent tempQLearning = new
		// SimpleQAgent(SimpleQAgent.EXAMPLE_ONE_MAZE);
		Environment tempMaze = new Maze(Maze.EXAMPLE_TWO_MAZE);
		Agent tempQLearning = new SimpleQAgent(tempMaze);
		// tempQLearning.setGamma(0.9);
		tempQLearning.learn(100);
		System.out.println("\r\nWith simple implementation: ");
		System.out.println(tempQLearning);

		/**
		 * String tempString = ""; try { tempString =
		 * tempQLearning.findBestRoute(24); } catch (Exception ee){ tempString =
		 * ee.toString(); }//Of try
		 * 
		 * System.out.println(tempString);
		 */
	}// Of main
} // Of class SimpleQAgent
