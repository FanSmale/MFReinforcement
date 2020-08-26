package vlearning.umpire;

import java.util.Arrays;

import vlearning.agent.*;
import common.*;
import qlearning.environment.TicTacToe;
import vlearning.environment.*;

/**
 * The umpire.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 20, 2020.
 * @version 1.0
 */

public class VUmpire {
	/**
	 * The environment for the agents.
	 */
	VTicTacToe environment;

	/**
	 * Two players.
	 */
	VAgent[] agentArray;

	/**
	 * The win times of agents. 0 stands for TIE.
	 */
	int[] winTimesArray;

	/**
	 ****************** 
	 * The constructor.
	 * 
	 * @param paraEnvironment
	 *            The environment.
	 *            @param paraAgentArray The agents.
	 ****************** 
	 */
	public VUmpire() {
		environment = new VTicTacToe();
		agentArray = new VAgent[2];
		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i] = new VAgent(environment, i + 1);
		}//Of  for i
		
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
	public void train(int paraEpisodes) {
		System.out.println("Training stage ...");
		// Step 1. Initialize.
		environment.reset();

		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i].reset();
			agentArray[i].setTrainingStage(true);
		}//Of for i

		Arrays.fill(winTimesArray, 0);

		// Step 2. Run the given episodes.
		for (int i = 0; i < paraEpisodes; i++) {
			if (i > paraEpisodes - 5) {
				SimpleTools.variableTracking = true;
			}//Of if

			//SimpleTools.variableTrackingOutput("\r\nEpisode " + i + ":\r\n");
			//Step 2.1. Reinitialize the environment while not the players.
			environment.reset();
			//Step 2.3. Now update the 
			for (int j = 0; j < agentArray.length; j++) {
				//agentArray[j].resetRoute();
			}//Of for i

			int tempCurrentPlayer = 0;
			int tempGameSituation = TicTacToe.UNFINISHED;

			// Step 2.2. Each time a final state should be reached.
			while (tempGameSituation == TicTacToe.UNFINISHED) {
				try {
					tempGameSituation = agentArray[tempCurrentPlayer].step();
				} catch (Exception ee) {
					System.out.println("QAgent for agent.step: " + ee);
					System.exit(0);
				} // Of try

				tempCurrentPlayer = (tempCurrentPlayer + 1) % 2;
			} // Of while

			//SimpleTools.variableTrackingOutput("The environment is: " + environment.toString());
			winTimesArray[tempGameSituation]++;
			SimpleTools.variableTrackingOutput(" & " + tempGameSituation + "\\\\\r\n");
			
			//Step 2.3. Now update the 
			for (int j = 0; j < agentArray.length; j++) {
				agentArray[j].backup();
			}//Of for i
		} // Of for i
	} // Of train
	
	/**
	 ****************** 
	 * Play.
	 * 
	 * @param paraEpisodes
	 *            The number of episodes.
	 ****************** 
	 */
	public void play(int paraEpisodes) {
		System.out.println("Now play ...");
		// Step 1. Initialize.
		for (int i = 0; i < agentArray.length; i++) {
			//agentArray[i].reset();
			agentArray[i].setTrainingStage(false);
		}//Of for i

		Arrays.fill(winTimesArray, 0);

		// Step 2. Run the given episodes.
		for (int i = 0; i < paraEpisodes; i++) {
			if (i > paraEpisodes - 5) {
				SimpleTools.variableTracking = true;
			}//Of if

			//SimpleTools.variableTrackingOutput("\r\nEpisode " + i + ":\r\n");
			//Step 2.1. Reinitialize the environment while not the players.
			environment.reset();
			//Step 2.3. Now update the 
			for (int j = 0; j < agentArray.length; j++) {
				//agentArray[j].resetRoute();
			}//Of for i

			int tempCurrentPlayer = 0;
			int tempGameSituation = TicTacToe.UNFINISHED;

			// Step 2.2. Each time a final state should be reached.
			while (tempGameSituation == TicTacToe.UNFINISHED) {
				try {
					tempGameSituation = agentArray[tempCurrentPlayer].step();
				} catch (Exception ee) {
					System.out.println("QAgent for agent.step: " + ee);
					System.exit(0);
				} // Of try

				tempCurrentPlayer = (tempCurrentPlayer + 1) % 2;
			} // Of while

			//SimpleTools.variableTrackingOutput("The environment is: " + environment.toString());
			winTimesArray[tempGameSituation]++;
			SimpleTools.variableTrackingOutput(" & " + tempGameSituation + "\\\\");
		} // Of for i
	} // Of play
	
	/**
	 ****************** 
	 * Getter.
	 * @return The win times array.
	 ****************** 
	 */
	public int[] getWinTimesArray() {
		return winTimesArray;
	}//Of getWinTimesArray
	
	/**
	 ****************** 
	 * Test.
	 ****************** 
	 */
	public static void ticTacToeVTest() {
		VUmpire tempUmpire = new VUmpire();

		int tempEpisodes = 10000;
		SimpleTools.variableTracking = false;
		tempUmpire.train(tempEpisodes);

		System.out.println("\r\nEpisodes = " + tempEpisodes + ", winTimesArray = "
				+ Arrays.toString(tempUmpire.winTimesArray));

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
}// Of class Umpire