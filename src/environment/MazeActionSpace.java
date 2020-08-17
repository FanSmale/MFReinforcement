package environment;

/**
 * The maze action space.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class MazeActionSpace extends ActionSpace {

	/**
	 * Move direction: up.
	 */
	public static final int UP = 0;

	/**
	 * Move direction: down.
	 */
	public static final int DOWN = 1;

	/**
	 * Move direction: left.
	 */
	public static final int LEFT = 2;

	/**
	 * Move direction: right.
	 */
	public static final int RIGHT = 3;

	/**
	 * Move direction: no move.
	public static final int NO_MOVE = 4;
	 */

	/**
	 ****************** 
	 * The first constructor.
	 ****************** 
	 */
	public MazeActionSpace() {
		numActions = 4;
		actions = new int[4];
		for (int i = 0; i < actions.length; i++) {
			actions[i] = i;
		}//Of for i
	}// Of the first constructor
} //Of class MazeActionSpace
