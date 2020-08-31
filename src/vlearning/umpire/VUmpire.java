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
	 * The winner.
	 */
	int winner;

	/**
	 * The checkerboard status.
	 */
	int[][] playCheckerboard;

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
		
		playCheckerboard = new int[VTicTacToe.SIZE][VTicTacToe.SIZE];
		
		winTimesArray = new int[3];
		resetForGame();
	}// Of the constructor

	/**
	 ****************** 
	 * Reset for the new game.
	 ****************** 
	 */
	public void resetForGame() {
		winner = VTicTacToe.EMPTY;
		for (int i = 0; i < VTicTacToe.SIZE; i++) {
			for (int j = 0; j < VTicTacToe.SIZE; j++) {
				playCheckerboard[i][j] = VTicTacToe.EMPTY;
			}//Of for j
		}//Of for i
		
		environment.reset();
	}//Of reset

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
		}//Of for i

		Arrays.fill(winTimesArray, 0);

		// Step 2. Run the given episodes.
		for (int i = 0; i < paraEpisodes; i++) {
			if ((i == 0) || (i >= paraEpisodes - 5) ) {
				//Important: change to true for tracking.
				SimpleTools.variableTracking = false;
			} else {
				SimpleTools.variableTracking = false;
			}//Of if

			//SimpleTools.variableTrackingOutput("\r\nEpisode " + i + ":\r\n");
			//Step 2.1. Reinitialize the environment while not the players.
			environment.reset();

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
		
		//Step 2.3. Only for testing.
		//for (int i = 0; i< agentArray.length; i++) {
		//	agentArray[i].showTheFirstStep();
		//}//Of for i
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
		//System.out.println("Now play ...");
		// Step 1. Initialize.
		for (int i = 0; i < agentArray.length; i++) {
			//agentArray[i].reset();
			agentArray[i].setTrainingStage(false);
		}//Of for i

		Arrays.fill(winTimesArray, 0);

		// Step 2. Run the given episodes.
		for (int i = 0; i < paraEpisodes; i++) {
			//SimpleTools.variableTrackingOutput("\r\nEpisode " + i + ":\r\n");
			//Step 2.1. Reinitialize the environment while not the players.
			environment.reset();

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
		SimpleTools.variableTrackingOutput("" + environment);
	} // Of play
	
	/**
	 ****************** 
	 * Play with human.
	 * 
	 * @param paraPosition
	 *            The human player's new position.
	 * @return the game situation result.
	 ****************** 
	 */
	public int humanPlay(int paraAction) {
		try {
			environment.step(paraAction);
		} catch (Exception ee) {
			System.out.println("Internal error from VUmpire.step()");
			System.exit(0);
		}//Of try
		
		return environment.getGameSituation();
		//if (environment.getGameSituation() == VTicTacToe.WHITE) {
		//	return VTicTacToe.WHITE;
		//} else if (environment.getGameSituation() == VTicTacToe.BLACK) {
		//	return VTicTacToe.BLACK;
		//}//Of if
		
		//winner = agentArray[1].step();
		
		//result = agentArray[1].getRecentAction();
		//return result;
	}//Of humanPlay

	/**
	 ****************** 
	 * Play with agent.
	 * 
	 * @param paraPosition
	 *            The human player's new position.
	 * @return the result. -1 stands for the human win, 9 stands for the machine win
	 ****************** 
	 */
	public int agentPlay() {
		int result = 0;
		winner = agentArray[1].step();
		return winner;
	}//Of agentPlay	
	
	/**
	 ****************** 
	 * Getter.
	 ****************** 
	 */
	public int getRecentAction() {
		return agentArray[1].getRecentAction();
	}// Of getRecentAction

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
		tempUmpire.train(tempEpisodes, 0.1, 0.1);

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