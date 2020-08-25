package environment;

/**
 * The competition environment.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public abstract class CompetitionEnvironment extends Environment {

	/**
	 * The winner. Only valid for competing games.
	 */
	int winner;

	/**
	 * The empty. It is employed to indicate the state of the location (no
	 * piece), and the winner of the play.
	 */
	public static final int EMPTY = 0;

	/**
	 * The piece of the first player, usually white. It is employed to indicate
	 * the state of the location, and the winner of the play.
	 */
	public static final int FIRST = 1;

	/**
	 * The piece of the first player, usually black. It is employed to indicate
	 * the state of the location, and the winner of the play.
	 */
	public static final int SECOND = 2;

	/**
	 * The checkerboard size, often 3. Maybe more in the future.
	 */
	public int checkerboardSize;

	/**
	 * The checkerboard.
	 */
	public int[][] checkerboard;

	/**
	 * Game situation: tie.
	 */
	public static final int TIE = 0;

	/**
	 * Game situation: win (for the first player).
	 */
	public static final int WIN = 1;

	/**
	 * Game situation: lose (for the first player).
	 */
	public static final int LOSE = 2;

	/**
	 * Game situation: unfinished.
	 */
	public static final int UNFINISHED = 3;

	/**
	 * Game situation.
	 */
	int gameSituation;

	/**
	 * Whose turn.
	 */
	int currentPlayer;

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The winner.
	 * @throws Exception
	 *             if the game is unfinished.
	 ****************** 
	 */
	public int getWinner() throws Exception {
		return winner;
	}// Of getWinner

}// Of class CompetitionEnvironment
