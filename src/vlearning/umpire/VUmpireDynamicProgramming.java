package vlearning.umpire;

import java.util.Arrays;

import vlearning.agent.*;
import common.*;
import qlearning.environment.TicTacToe;
import vlearning.environment.*;

/**
 * The umpire using the dynamic programming approach.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 20, 2020.
 * @version 1.0
 */

public class VUmpireDynamicProgramming extends VUmpire {

	/**
	 ****************** 
	 * The constructor.
	 * 
	 * @param paraEnvironment
	 *            The environment.
	 * @param paraAgentArray
	 *            The agents.
	 ****************** 
	 */
	public VUmpireDynamicProgramming(int paraStrategy) {
		environment = new VTicTacToeDynamicProgramming();
		agentArray = new VAgentDynamicProgramming[2];
		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i] = new VAgentDynamicProgramming(environment, i + 1);
			((VAgentDynamicProgramming) agentArray[i]).setUpdateStrategy(paraStrategy);
		} // Of for i

		winTimesArray = new int[3];
	}// Of the constructor

	/**
	 ****************** 
	 * Train.
	 * 
	 * @param paraEpisodes
	 *            The number of episodes.
	 ****************** 
	 */
	public void train(int paraEpisodes, double paraEpsilon, double paraAlpha) {
		//System.out.println("Training stage ...");
		// Step 1. Initialize.
		environment.reset();

		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i].reset();
			agentArray[i].setTrainingStage(true);
			agentArray[i].setEpsilon(paraEpsilon);
			agentArray[i].setAlpha(paraAlpha);
		} // Of for i

		Arrays.fill(winTimesArray, 0);

		// Step 2. Run the given episodes.
		for (int i = 0; i < paraEpisodes; i++) {
			for (int j = 0; j < agentArray.length; j++) {
				agentArray[j].update();
			} // Of for j
		} // Of for i
	} // Of train

	/**
	 ****************** 
	 * Test.
	 ****************** 
	 */
	public static void ticTacToeVTest() {
		VUmpireDynamicProgramming tempUmpire = new VUmpireDynamicProgramming(0);

		int tempEpisodes = 10;
		SimpleTools.variableTracking = false;
		tempUmpire.train(tempEpisodes, 0.1, 0.1);

		// System.out.println("\r\nEpisodes = " + tempEpisodes + ",
		// winTimesArray = "
		// + Arrays.toString(tempUmpire.winTimesArray));

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
		ticTacToeVTest();
	}// Of main
}// Of class VUmpireDynamicProgramming