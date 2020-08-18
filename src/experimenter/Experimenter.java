package experimenter;

import environment.Environment;
import environment.Maze;
import learner.ControlledRandomQLearner;
import learner.Learner;
import learner.SimpleQLearner;

/**
 * Test the project.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class Experimenter {
	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		Environment tempEnvironment = new Maze(Maze.EXAMPLE_TWO_MAZE);
		//Learner tempLearner = new SimpleQLearner(tempEnvironment);
		Learner tempLearner = new ControlledRandomQLearner(tempEnvironment);
		tempLearner.learn(300);

		System.out.println("\r\nWith simple implementation: ");
		System.out.println(tempLearner);

		/**
		 * String tempString = ""; try { tempString =
		 * tempQLearning.findBestRoute(24); } catch (Exception ee){ tempString =
		 * ee.toString(); }//Of try
		 * 
		 * System.out.println(tempString);
		 */
	}// Of main
} // Of class Experimenter
