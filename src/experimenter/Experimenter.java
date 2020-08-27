package experimenter;

import common.SimpleTools;
import qlearning.environment.*;
import qlearning.umpire.Umpire;
import vlearning.umpire.VUmpire;

import java.util.Arrays;

import qlearning.agent.*;

/**
 * Test the project.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 20, 2020.
 * @version 1.0
 */

public class Experimenter {

	/**
	 ****************** 
	 * Test maze.
	 ****************** 
	 */
	public static void mazeTest() {
		Environment tempEnvironment = new Maze(Maze.EXAMPLE_TWO_MAZE);
		// Environment tempEnvironment = new Maze(Maze.generateComplexMaze());
		int tempStartState = 0;
		// int tempStartState = 0;
		tempEnvironment.setStartState(tempStartState);

		// Learner tempAgent = new SimpleQLearner(tempEnvironment);
		int tempEpisodes = 10000;
		Agent tempAgent = new WeightedRandomQAgent(tempEnvironment);
		SimpleTools.variableTracking = false;
		tempAgent.learn(tempEpisodes);

		System.out.println("Episodes = " + tempEpisodes + ", average reward = "
				+ tempAgent.getAverageReward());
		System.out.println(
				"The last episode reward: " + tempAgent.getRewardArray()[tempEpisodes - 1]);

		int[] tempRoute = {};
		try {
			tempRoute = tempAgent.greedyRouting(tempStartState);
		} catch (Exception ee) {
			System.out.println(ee);
		} // Of try

		System.out.print("With the greedy strategy the route is: ");
		for (int i = 0; i < tempRoute.length; i++) {
			System.out.print(" -> (" + tempRoute[i] / 31 + ", " + tempRoute[i] % 31 + ")");
		} // Of for i
		System.out.println(
				"\r\nThe route length is (not including the start): " + (tempRoute.length - 1));
	}// Of mazeTest

	/**
	 ****************** 
	 * Test maze.
	 * 
	 * @param paraEpisodes
	 *            The number of episodes.
	 ****************** 
	 */
	public static void ticTacToeTest(int paraEpisodes) {
		CompetitionEnvironment tempEnvironment = new TicTacToe();
		tempEnvironment.reset();

		CompetitionQAgent[] tempAgentArray = new CompetitionQAgent[2];
		for (int i = 0; i < tempAgentArray.length; i++) {
			tempAgentArray[i] = new CompetitionQAgent(tempEnvironment, i + 1);
		} // Of for i

		Umpire tempUmpire = new Umpire(tempEnvironment, tempAgentArray);

		SimpleTools.variableTracking = false;
		tempUmpire.train(paraEpisodes);

		System.out.println("Episodes = " + paraEpisodes + ", winTimesArray = "
				+ Arrays.toString(tempUmpire.getWinTimesArray()));
	}// Of ticTacToeTest

	/**
	 ****************** 
	 * Test.
	 ****************** 
	 */
	public static void ticTacToeVTest(int paraEpisodes) {
		VUmpire tempUmpire = new VUmpire();

		SimpleTools.variableTracking = false;
		tempUmpire.train(paraEpisodes);

		System.out.println("\r\nEpisodes = " + paraEpisodes + ", winTimesArray = "
				+ Arrays.toString(tempUmpire.getWinTimesArray()));

		SimpleTools.variableTracking = true;
		tempUmpire.play(1);
	}// Of ticTacToeVTest

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		// mazeTest();
		// ticTacToeTest(1000);
		ticTacToeVTest(100000);
	}// Of main
} // Of class Experimenter
