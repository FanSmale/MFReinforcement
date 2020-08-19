package deprecated.maze;

import java.util.Arrays;
import environment.Maze;

/**
 * The shortest path learner. It is deterministic. Currently, only one final
 * state is supported.<br>
 * Project: Reinforce learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 16, 2020.<br>
 *         Last modified: August 16, 2020.
 * @version 1.0
 */

public class ShortestPathLearning extends DeprecatedMaze {

	/**
	 * The distance from the current to the final state.
	 */
	int[] distanceArray;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraMaze
	 *            The maze matrix.
	 ****************** 
	 */
	public ShortestPathLearning(int[][] paraMaze) {
		super(paraMaze);
	}// Of the first constructor

	/**
	 ****************** 
	 * The training method that should be overwritten.
	 * 
	 * @param paraRounds
	 *            The number of rounds for learning. It is unuseful in this
	 *            method.
	 ****************** 
	 */
	public double train(int paraRounds) {
		// Step 1. Initialize.
		int tempNumStates = numRows * numColumns;
		qualityMatrix = new double[tempNumStates][5];

		// Only one final state supported.
		int tempFinalState = getFinalStates()[0];
		distanceArray = new int[tempNumStates];
		Arrays.fill(distanceArray, Integer.MAX_VALUE);
		distanceArray[tempFinalState] = 0;
		
		// The visited array.
		boolean[] tempVisitedArray = new boolean[tempNumStates];
		Arrays.fill(tempVisitedArray, false);
		tempVisitedArray[tempFinalState] = true;

		// The parent state of each state.
		int[] tempParentArray = new int[tempNumStates];
		Arrays.fill(tempParentArray, -1);

		// Trap states are unavailable.
		boolean[] tempAvailableArray = new boolean[tempNumStates];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				if (maze[i][j] == TRAP_STATE_VALUE) {
					tempAvailableArray[i * numColumns + j] = false;
				} else {
					tempAvailableArray[i * numColumns + j] = true;
				} // Of if
			} // Of for j
		} // Of for i

		// Step 2. Start from the final state.
		int tempExpandedState = tempFinalState;

		expandState(tempExpandedState);

		// Step 3. Standard double loop to find shortest path.
		int tempMinDistance;
		for (int i = 0; i < tempNumStates; i++) {
			// Step 3.1. Select a state to expand.
			tempExpandedState = -1;
			tempMinDistance = Integer.MAX_VALUE;
			for (int j = 0; j < tempNumStates; j++) {
				if (tempVisitedArray[j]) {
					continue;
				} // Of if

				if (!tempAvailableArray[j]) {
					continue;
				} // Of if

				if (tempMinDistance > distanceArray[j]) {
					tempMinDistance = distanceArray[j];
					tempExpandedState = j;
				}//Of if
			} // Of for j

			if (tempExpandedState == -1) {
				// No state to expand.
				break;
			} // Of if
			
			tempVisitedArray[tempExpandedState] = true;

			// Step 3.2. Prepare for the next round.
			expandState(tempExpandedState);
		} // Of for i

		return 0;
	}// Of train

	/**
	 ****************** 
	 * Expand the node to update distance and quality information.
	 * 
	 * @param paraCurrentState
	 *            The current state.
	 ****************** 
	 */
	private void expandState(int paraCurrentState) {
		expandState(paraCurrentState, UP, DOWN);
		expandState(paraCurrentState, DOWN, UP);
		expandState(paraCurrentState, LEFT, RIGHT);
		expandState(paraCurrentState, RIGHT, LEFT);
	}//Of expandState
	
	/**
	 ****************** 
	 * Expand the node to update distance and quality information.
	 * 
	 * @param paraCurrentState
	 *            The current state.
	 * @param paraDirection
	 *            The direction.
	 * @param paraReverseDirection
	 *            The reverse direction.
	 ****************** 
	 */
	private void expandState(int paraCurrentState, int paraDirection,
			int paraReverseDirection) {
		int tempNextState = transitionMatrix[paraCurrentState][paraDirection];

		if (tempNextState == INVALID_STATE) {
			return;
		} // Of if

		// Do not consider trap state.
		if (maze[tempNextState / numColumns][tempNextState % numColumns] == -10) {
			return;
		} // Of if

		//System.out.println("paraCurrentState: " + paraCurrentState + ", tempNextState: " + tempNextState
		//		+ ", paraDirection = " + paraDirection);
		//System.out.println("distanceArray[tempNextState]: " + distanceArray[tempNextState] + ", distanceArray[paraCurrentState]: " + distanceArray[paraCurrentState]
		//		+ ", paraDirection = " + paraDirection);
		if (distanceArray[tempNextState] > distanceArray[paraCurrentState] + 1) {
			distanceArray[tempNextState] = distanceArray[paraCurrentState] + 1;
			for (int i = 0; i < 5; i++) {
				qualityMatrix[tempNextState][i] = -1;
			} // Of for i
			qualityMatrix[tempNextState][paraReverseDirection] = 1;
			//System.out.println("Change qualityMatrix: " + tempNextState + ", " + paraReverseDirection + " to 1.");
		} // Of if
	}// Of expandState

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		//ShortestPathLearning tempLearner = new ShortestPathLearning(DeprecatedMaze.EXAMPLE_TWO_MAZE);
		ShortestPathLearning tempLearner = new ShortestPathLearning(Maze.generateComplexMaze());
		tempLearner.train(1);
		System.out.println("\r\nWith shortest path implementation: ");
		System.out.println(tempLearner);
		
		String tempString = "";
		try {
			tempString = tempLearner.findBestRoute(33);
		} catch (Exception ee){
			tempString = ee.toString();
		}//Of try
		
		System.out.println(tempString);

	}// Of main
}// Of class ShortestPathLearning
