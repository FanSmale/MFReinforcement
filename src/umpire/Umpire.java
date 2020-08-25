package umpire;

import java.util.Arrays;

import agent.Agent;
import agent.CompetitionQAgent;
import common.*;
import environment.CompetitionEnvironment;
import environment.Environment;
import environment.TicTacToe;

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

public class Umpire {
	/**
	 * The environment for the agents.
	 */
	CompetitionEnvironment environment;

	/**
	 * The number of agents.
	 */
	int numAgents;

	/**
	 * The environment for the agents.
	 */
	CompetitionQAgent[] agentArray;

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
	public Umpire(CompetitionEnvironment paraEnvironment, CompetitionQAgent[] paraAgentArray) {
		environment = paraEnvironment;
		numAgents = paraAgentArray.length;

		agentArray = paraAgentArray;
		for (int i = 0; i < paraAgentArray.length; i++) {
			agentArray[i].setCompetitor(agentArray[(i + 1) % 2]);
		}//Of for i

		winTimesArray = new int[numAgents + 1];
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
		environment.reset();
		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i].reset();
		} // Of for i
		
		Arrays.fill(winTimesArray, 0);

		// Step 1. Get the start state.
		int tempStartState = environment.getStartState();

		// Step 2. Run the given episodes.
		for (int i = 0; i < paraEpisodes; i++) {
			SimpleTools.variableTrackingOutput("\r\nEpisode " + i);
			environment.reset();
			environment.setCurrentState(tempStartState);

			// Step 2.1. Initialize. Each time start from the same state.
			// rewardArray[i] = 0;
			int tempCurrentState = tempStartState;
			boolean tempFinished = false;

			int tempCurrentPlayer = 0;
			int tempWinner = 0;

			// Step 2.2. Each time a final state should be reached.
			while (!tempFinished) {
				try {
					agentArray[tempCurrentPlayer].step(tempCurrentState);
				} catch (Exception ee) {
					System.out.println("QAgent for agent.step: " + ee);
					System.exit(0);
				} // Of try

				tempCurrentState = environment.getCurrentState();
				tempFinished = environment.isFinished();

				tempCurrentPlayer = (tempCurrentPlayer + 1) % numAgents;
			} // Of while

			SimpleTools.variableTrackingOutput("The environment is: " + environment.toString());

			try {
				tempWinner = environment.getWinner();
			} catch (Exception ee) {
				System.out.println("Exception occurred in Umpire.train():\r\n" + ee);
				System.exit(0);
			} // Of try
			winTimesArray[tempWinner]++;
			System.out.println(" & " + tempWinner + "\\\\");
		} // Of for i
	} // Of train

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
	public static void ticTacToeTest() {
		CompetitionEnvironment tempEnvironment = new TicTacToe();
		tempEnvironment.reset();

		CompetitionQAgent[] tempAgentArray = new CompetitionQAgent[2];
		for (int i = 0; i < tempAgentArray.length; i++) {
			tempAgentArray[i] = new CompetitionQAgent(tempEnvironment, i + 1);
		} // Of for i

		Umpire tempUmpire = new Umpire(tempEnvironment, tempAgentArray);

		int tempEpisodes = 10000;
		SimpleTools.variableTracking = false;
		tempUmpire.train(tempEpisodes);

		System.out.println("Episodes = " + tempEpisodes + ", winTimesArray = "
				+ Arrays.toString(tempUmpire.winTimesArray));
	}// Of ticTacToeTest

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		ticTacToeTest();
	}// Of main
}// Of class Umpire