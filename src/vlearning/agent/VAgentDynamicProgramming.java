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
	 * Update the value array.
	 ****************** 
	 */
	public void update() {
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
		}//Of for i
		System.out.println("...");
	}// Of update

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