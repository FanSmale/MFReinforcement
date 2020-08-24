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
	 ****************** 
	 * Getter.
	 * 
	 * @return The winner.
	 * @throws Exception if the game is unfinished.
	 ****************** 
	 */
	public int getWinner() throws Exception{
		return winner;
	}// Of getWinner
	
}//Of class CompetitionEnvironment
