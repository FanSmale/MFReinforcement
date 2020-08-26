package py;

/**
 * AI player of the game.<br>
 * Project: Tic-tac-toe translation from python.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 25, 2020.<br>
 *         Last modified: August 25, 2020.
 * @version 1.0
 */

public class Player {
	
	/**
	 * The symbol.
	 */
	int symbol;

	/**
	 * The estimations, a dictionary.
	 */
	int[] estimations;

	/**
	 * Step size.
	 */
	double stepSize;

	/**
	 * Epsilon.
	 */
	double epsilon;

	/**
	 * The number of states.
	 */
	int numStates;

	/**
	 * The states.
	 */
	State[] states;

	/**
	 * The greedy strategies.
	 */
	boolean[] greedy;

	/**
	 ****************** 
	 * The constructor.
	 ****************** 
	 */
	public Player(double paraStepSize, double paraEpsilon) {
		stepSize = paraStepSize;
		epsilon = paraEpsilon;

		//3^9 = 19683 possible states
		estimations = new int[19683];
		states = new State[19683];
		greedy = new boolean[19683];
		numStates = 0;
	}// Of the constructor

	/**
	 ****************** 
	 * Reset.
	 ****************** 
	 */
	public void reset() {
		numStates = 0;
	}//Of reset

	/**
	 ****************** 
	 * Add a new state.
	 ****************** 
	 */
	public void setState(State paraState) {
		states[numStates] = paraState;
		greedy[numStates] = true;
		numStates ++;
	}//Of setState
	
	

} // Of class Player
