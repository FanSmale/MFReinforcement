package experimenter;

import java.util.Arrays;

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
		// Environment tempEnvironment = new Maze(Maze.EXAMPLE_TWO_MAZE);
		Environment tempEnvironment = new Maze(Maze.generateComplexMaze());
		tempEnvironment.setStartState(33);

		// Learner tempLearner = new SimpleQLearner(tempEnvironment);
		Learner tempLearner = new ControlledRandomQLearner(tempEnvironment);
		for (int i = 10000; i < 10001; i *= 10) {
			SimpleTools.variableTracking = false;
			tempLearner.learn(i);

			System.out.println(
					"Episodes = " + i + ", average reward = " + tempLearner.getAverageReward());
			System.out.println("The last episode reward: " + tempLearner.getRewardArray()[i - 1]);
		} // Of for i

		int[] tempRoute = {};
		try {
			tempRoute = tempLearner.greedyRouting(33);
		} catch (Exception ee) {
		} // Of try

		System.out.print("With the greedy strategy the route is: ");
		for (int i = 0; i < tempRoute.length; i++) {
			System.out.print(" -> (" + tempRoute[i] / 32 + ", " + tempRoute[i] % 32 + ")");
		} // Of for i
		System.out.println("\r\nThe route length is " + tempRoute.length);
	}// Of main
} // Of class Experimenter
