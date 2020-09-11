__author__ = 'minfanphd'

import numpy as np

# Board size constants
SIZE = 3
BOARD_SIZE = SIZE * SIZE

# Situation constants
TIE = 0
WHITE = 1
BLACK = 2
UNFINISHED = 3

# The state constant additional to WHITE and BLACK
EMPTY = 0

NUM_STATES = 19683 # 3^9
NUM_ACTIONS = 9 # 3^2

############################
# The checkerboard which is able to judge the game situation.
############################
class Checkerboard:
	################
	# The constructor.
	################
	def __init__(self):
		self.board = np.zeros((SIZE, SIZE)) # The 3 * 3 board
		self.currentState = 0 # All positions are empty
		self.gameSituation = UNFINISHED
		self.currentPlayer = WHITE
		self.currentRoute = np.zeros(NUM_ACTIONS +1)
		self.currentRouteLength = 0

	################
	# Get the current state.
	################
	def getCurrentState(self):
		return self.currentState

	################
	# Get valid actions
	################
	def stateToBoard(self, paraState):
		tempValue = paraState
		resultBoard = np.zeros((SIZE, SIZE)) # The 3 * 3 board
		for i in range(SIZE):
			for j in range(SIZE):
				resultBoard[i][j] = tempValue % SIZE
				tempValue = tempValue / SIZE

		print("The board is: ", resultBoard)
		return resultBoard

	################
	# Get valid actions
	################
	def getValidActions(self, paraState):
		tempBoard = self.stateToBoard(paraState)

		resultActions = []
		for i in range(SIZE):
			for j in range(SIZE):
				if tempBoard[i][j] == EMPTY:
					resultActions.append(i * SIZE + j)

		print("valid actions: ", resultActions)
		return resultActions

	################
	# Go one step.
	################
	def step(self, paraAction):
		tempRow = paraAction / SIZE
		tempColumn = paraAction % SIZE
		board[tempRow][tempColumn] = currentPlayer
		currentState = checkerboardToState(board)
		gameSituation = computeGameSituation()
		self.currentRoute[currentRouteLength] = currentState
		self.currentRouteLength = self.currentRouteLength + 1

		if self.currentPlayer == WHITE:
			self.currentPlayer == BLACK
		else:
			self.currentPlayer == WHITE

	################
	# Compute the game situation, TIE, WHITE, BLACK, UNFINISHED
	################
	def computeGameSituation(self, paraBoard):
		paraBoard = self.stateToBoard(paraBoard)
		# Step 1. Horizontal
		for i in range(SIZE):
			if paraBoard[i][0] == EMPTY:
				continue
			# A whole row is the same.
			tempSame = True
			for j in range(1, SIZE):
				if paraBoard[i][j] != paraBoard[i][0]:
					tempSame = False
					break

			if tempSame:
				return paraBoard[i][0]

		# Step 2. Vertical
		for j in range(SIZE):
			if paraBoard[0][j] == EMPTY:
				continue
			tempSame = True # A whole column is the same.
			for i in range(1, SIZE):
				if paraBoard[i][j] != paraBoard[0][j]:
					tempSame = False
					break
			if tempSame:
				return paraBoard[0][j]

		# Step 3. Slope: Left-top to right-bottom.
		if paraBoard[0][0] != EMPTY:
			tempSame = True
			for i in range(1, SIZE):
				if paraBoard[i][i] != paraBoard[0][0]:
					tempSame = False
					break
			if tempSame:
				return paraBoard[0][0]

		# Step 4. Slope: Right-top to left-bottom.
		if paraBoard[0][SIZE - 1] != EMPTY:
			for i in range(1, SIZE):
				tempSame = True
				if paraBoard[i][SIZE - i - 1] != paraBoard[0][SIZE - 1]:
					tempSame = False
					break
			if tempSame:
				return paraBoard[0][SIZE - 1]

		# Step 5. Checkboard full
		tempHasEmpty = False
		for i in range(SIZE):
			for j in range(SIZE):
				if paraBoard[i][j] == EMPTY:
					tempHasEmpty = True
					break
			if tempHasEmpty:
				break
		if not tempHasEmpty:
			return TIE

		return UNFINISHED
####################Of class Checkerboard

#The main entrance of the program.
if __name__ == '__main__':
	checkerboard = Checkerboard()

	checkerboard.getValidActions(123)
	tempSituation = checkerboard.computeGameSituation(123)
	print("Hello, tic-tac-toe, the situation is: ", tempSituation)