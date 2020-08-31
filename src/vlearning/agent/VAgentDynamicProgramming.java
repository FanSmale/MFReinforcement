package vlearning.agent;

import java.util.Arrays;

import common.*;
import vlearning.environment.VTicTacToe;

/**
 * The super-class of any value agent.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 26, 2020.<br>
 *         Last modified: August 27, 2020.
 * @version 1.0
 */

public class VAgentDynamicProgramming extends VAgent {

	/**
	 * Strategy of update. Average for all neighbors.
	 */
	public static final int AVERAGE_UPDATE = 0;

	/**
	 * Strategy of update. Greedy on all neighbors.
	 */
	public static final int GREEDY_UPDATE = 1;

	/**
	 * Strategy of update.
	 */
	int updateStrategy;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraEnvironment
	 *            The given environment.
	 ****************** 
	 */
	public VAgentDynamicProgramming(VTicTacToe paraEnvironment, int paraSymbol) {
		super(paraEnvironment, paraSymbol);
	}// Of the first constructor

	/**
	 ****************** 
	 * Setter
	 ****************** 
	 */
	public void setUpdateStrategy(int paraStratedy) {
		updateStrategy = paraStratedy;
	}// Of setUpdateStrategy

	/**
	 ****************** 
	 * Update the value array.
	 ****************** 
	 */
	public void update() {
		if (updateStrategy == AVERAGE_UPDATE) {
			updateAverage();
		} else {
			updateGreedy();
		}//Of if
	}//Of update

	/**
	 ****************** 
	 * Update the value array.
	 ****************** 
	 */
	public void updateAverage() {
		for (int i = 0; i < environment.getNumStates(); i++) {
			// Ignore unavailable states.
			if (!environment.isStateAvailable(i)) {
				continue;
			} // Of if

			// Update the current state value according to its neighbors.
			int[] tempNeighbors = environment.getTransitionMatrix()[i];
			// System.out.println("The neighbors are " +
			// Arrays.toString(tempNeighbors));
			double tempValue = 0;
			for (int j = 0; j < tempNeighbors.length; j++) {
				if (tempNeighbors[j] == 0) {
					// Back to itself
					tempValue += valueArray[i];
				} else {
					tempValue += valueArray[tempNeighbors[j]];
				} // Of if
			} // Of for j

			valueArray[i] = tempValue / tempNeighbors.length;
		} // Of for i

		System.out.print("Player " + symbol + ", value array = ");
		for (int i = 0; i < 20; i++) {
			System.out.print("" + valueArray[i] + ",");
		} // Of for i
		System.out.println("...");
	}// Of updateAverage

	/**
	 ****************** 
	 * Update the value array.
	 ****************** 
	 */
	public void updateGreedy() {
		double tempGamma = 0.9;
		double tempStepCost = 0.01;
		for (int i = 0; i < environment.getNumStates(); i++) {
			// Ignore unavailable states.
			if (!environment.isStateAvailable(i)) {
				continue;
			} // Of if

			// Update the current state value according to its neighbors.
			int[] tempNeighbors = environment.getTransitionMatrix()[i];
			// System.out.println("The neighbors are " +
			// Arrays.toString(tempNeighbors));
			double tempMax = -100;
			for (int j = 0; j < tempNeighbors.length; j++) {
				if (tempNeighbors[j] != 0) {
					if (tempMax < tempGamma * valueArray[tempNeighbors[j]] - tempStepCost) {
						tempMax = tempGamma * valueArray[tempNeighbors[j]] - tempStepCost;
					}//Of if
				} // Of if
			} // Of for j
			
			if (tempMax > -99) {
				valueArray[i] = tempMax;
			}//Of if
		} // Of for i

		System.out.print("Player " + symbol + ", value array = ");
		for (int i = 0; i < 20; i++) {
			System.out.print("" + valueArray[i] + ",");
		} // Of for i
		System.out.println("...");
	}// Of updateGreedy

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a VAgentDynamicProgramming.\r\n";

		return resultString;
	}// Of toString

} // Of class VAgentDynamicProgramming