package py;

import java.util.Arrays;

/**
 * State of the game.<br>
 * Project: Tic-tac-toe translation from python.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, https://github.com/FanSmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 25, 2020.<br>
 *         Last modified: August 25, 2020.
 * @version 1.0
 */

public class State {

	/**
	 * Board rows.
	 */
	public static final int BOARD_ROWS = 3;

	/**
	 * Board columns.
	 */
	public static final int BOARD_COLS = 3;

	/**
	 * Board rows.
	 */
	public static final int BOARD_SIZE = BOARD_ROWS * BOARD_COLS;

	/**
	 * The data. the board is represented by an n * n array, 1 represents a
	 * chessman of the player who moves first, -1 represents a chessman of
	 * another player, 0 represents an empty position.
	 */
	int[][] data;

	/**
	 * The winner.
	 */
	int winner;

	/**
	 * Hash value. The ID of this state.
	 */
	int hashValue;

	/**
	 * The end?
	 */
	boolean end;

	/**
	 ****************** 
	 * The constructor.
	 ****************** 
	 */
	public State() {
		data = new int[BOARD_ROWS][BOARD_COLS];
		winner = 0;
		hashValue = -1;
		end = false;
	}// Of the constructor

	/**
	 ****************** 
	 * The second constructor. Clone a state object.
	 * 
	 * @param paraState
	 *            The given state.
	 ****************** 
	 */
	public State(State paraState) {
		data = new int[BOARD_ROWS][BOARD_COLS];
		winner = 0;
		hashValue = -1;
		end = false;
		
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				data[i][j] = paraState.data[i][j];
			}//Of for i
		}//Of for i
	}// Of the constructor

	/**
	 ****************** 
	 * Hash. The same as checkerboardToState()
	 * 
	 * @return The hash value, i.e., the current state ID.
	 ****************** 
	 */
	public int hash() {
		if (hashValue == -1) {
			hashValue = 0;
		} // Of if

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				int tempValue = data[i][j];
				if (tempValue == -1) {
					tempValue = 2;
				} // Of if

				hashValue = hashValue * 3 + i;
			} // Of for j
		} // Of for i

		return hashValue;
	}// Of hash

	/**
	 ****************** 
	 * Check whether or not the game come to an end. At the same time determine
	 * the winner.
	 * 
	 * @return Is end or not.
	 ****************** 
	 */
	public boolean isEnd() {
		int[] tempResults = new int[BOARD_ROWS + BOARD_COLS + 2];

		// Step 1. Prepare.
		// Check row
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				tempResults[i] += data[i][j];
			} // Of for j
		} // Of for i

		// Check columns
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				tempResults[BOARD_ROWS + j] += data[i][j];
			} // Of for j
		} // Of for i

		// Check diagonals
		for (int i = 0; i < BOARD_ROWS; i++) {
			tempResults[BOARD_ROWS + BOARD_COLS] += data[i][i];
		} // Of for i

		for (int i = 0; i < BOARD_ROWS; i++) {
			tempResults[BOARD_ROWS + BOARD_COLS + 1] += data[i][BOARD_ROWS - i - 1];
		} // Of for i

		// Step 2. Judge the winner
		for (int i = 0; i < tempResults.length; i++) {
			switch (tempResults[i]) {
			case 3:
				winner = 1;
				end = true;
				return end;
			case -3:
				winner = -1;
				end = true;
				return end;
			default:
				// Do nothing.
			}// Of switch
		} // Of for i

		// Whether it's a tie
		boolean tempTie = false;
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				if (data[i][j] == 0) {
					tempTie = true;
				} // Of if
			} // Of for j
		} // Of for i

		if (tempTie) {
			winner = 0;
			end = true;
			return end;
		} // Of if

		// Game is still going on
		end = false;
		return end;
	}// Of isEnd

	/**
	 ****************** 
	 * Construct the next state.
	 * 
	 * @param paraRow The row for the new piece.
	 * @param paraCol The column for the new piece.
	 * @param paraSymbol The symbol for the new piece.
	 * 
	 * @return A new state.
	 ****************** 
	 */
	public State nextState(int paraRow, int paraCol, int paraSymbol) {
		State resultNewState = new State(this);
		resultNewState.data[paraRow][paraCol] = paraSymbol;
		
		return resultNewState;
	}//Of nextState

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "";
		char tempToken = 'a';
		for (int i = 0; i < BOARD_ROWS; i++) {
			resultString += "-------------\r\n";
			for (int j = 0; j < BOARD_COLS; j++) {
				if (data[i][j] == 1) {
					tempToken = '*';
				} else if (data[i][j] == 0) {
					tempToken = '0';
				} else if (data[i][j] == -1) {
					tempToken = 'x';
				}//Of if
				
				resultString += tempToken + " | ";
			}//Of for j
			resultString += "\r\n";
		}//Of for i
		resultString += "-------------\r\n";

		return resultString;
	} // Of toString
	
	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		State tempState = new State();
		
		System.out.println("The original state is:\r\n" + tempState);
		
		State tempSecondState = tempState.nextState(1, 2, -1);
		
		System.out.println("The new state is:\r\n" + tempSecondState);
	}// Of main
}// Of class State


