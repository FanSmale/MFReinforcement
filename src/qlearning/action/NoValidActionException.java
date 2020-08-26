package qlearning.action;

/**
 * The exception for illegal action.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class NoValidActionException extends Exception {
	
	/**
	 * A number required by Java.
	 */
	private static final long serialVersionUID = 2827434606066042472L;

	/**
	 ****************** 
	 * The constructor.
	 * @param paraString The string to display.
	 ****************** 
	 */
	public NoValidActionException(String paraString) {
		super(paraString);
	}//Of the constructor
}//Of class NoValidActionException
