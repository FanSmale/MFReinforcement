package experimenter;

import common.SimpleTools;
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
		//Environment tempEnvironment = new Maze(Maze.EXAMPLE_TWO_MAZE);
		Environment tempEnvironment = new Maze(Maze.generateComplexMaze());
		tempEnvironment.setStartState(33);
		
		//Learner tempLearner = new SimpleQLearner(tempEnvironment);
		Learner tempLearner = new ControlledRandomQLearner(tempEnvironment);
		for (int i = 10000; i < 10001; i*= 10) {
			SimpleTools.variableTracking = false;
			tempLearner.learn(i);

			System.out.println("Episodes = " + i + ", average reward = " + tempLearner.getAverageReward());
			System.out.println("The last episode reward: " + tempLearner.getRewardArray()[i - 1]);
		}//Of for i

		/**
		 * String tempString = ""; try { tempString =
		 * tempQLearning.findBestRoute(24); } catch (Exception ee){ tempString =
		 * ee.toString(); }//Of try
		 * 
		 * System.out.println(tempString);
		 */
	}// Of main
} // Of class Experimenter
