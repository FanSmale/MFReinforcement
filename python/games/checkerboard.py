########################################################################
# Copyright (C)                                                        #
# 2020 Fan Min (minfanphd@163.com)                                     #
# 2016 - 2018 Shangtong Zhang (zhangshangtong.cpp@gmail.com)           #
# 2016 Jan Hakenberg (jan.hakenberg@gmail.com)                         #
# 2016 Tian Jun (tianjun.cpp@gmail.com)                                #
# 2016 Kenta Shimada (hyperkentakun@gmail.com)                         #
# Permission given to modify the code as long as you keep this         #
# declaration at the top                                               #
# https://github.com/FanSmale/MFReinforcement/tree/master/python/games #
########################################################################

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

# 3^9 states
NUM_STATES = 19683
# 3^2 actions
NUM_ACTIONS = 9

############################
# The checkerboard which is able to judge the game situation.
############################
class Checkerboard:
	################
	# The constructor.
	################
	def __init__(self):
		self.current_board = np.zeros((SIZE, SIZE)) # The 3 * 3 current_board
		self.current_state = 0 # All positions are empty
		self.game_situation = UNFINISHED
		self.current_player = WHITE
		self.current_route_states = np.zeros(NUM_ACTIONS +1)
		self.current_route_length = 0

	################
	# Get the game situation.
	################
	def get_game_situation(self):
		return self.game_situation

	################
	# Get the current state.
	################
	def get_current_state(self):
		return self.current_state

	################
	# Get the current route length.
	################
	def get_current_route_length(self):
		return self.current_route_length

	################
	# Get the current route.
	################
	def get_current_route_states(self):
		return self.current_route_states

	################
	# Reset.
	################
	def reset(self):
		for i in range(SIZE):
			for j in range(SIZE):
				self.current_board[i][j] = EMPTY
		self.current_state = 0
		self.current_player = WHITE
		self.current_route_length = 1

	################
	# Int state to current_board.
	################
	def state_to_board(self, para_state):
		temp_value = para_state
		result_board = np.zeros((SIZE, SIZE)) # The 3 * 3 current_board
		for i in range(SIZE):
			for j in range(SIZE):
				result_board[i][j] = temp_value % SIZE
				temp_value = temp_value // SIZE

		# print("The current_board is: ", result_board)
		return result_board

	################
	# Board to int state.
	################
	def board_to_state(self, para_board):
		result_state = 0
		for i in range(SIZE - 1, -1, -1):
			for j in range(SIZE - 1, -1, -1):
				result_state = result_state * SIZE + para_board[i][j]
		return result_state

	################
	# Get valid actions
	################
	def get_valid_actions(self):
		temp_board = self.state_to_board(self.current_state)

		result_actions = []
		for i in range(SIZE):
			for j in range(SIZE):
				if temp_board[i][j] == EMPTY:
					result_actions.append(i * SIZE + j)

		#print("valid actions: ", result_actions)
		return result_actions

	################
	# Go one step.
	################
	def step(self, para_action):
		#print("board: ", self.current_board)
		#print("action: ", para_action)
		#print("current_route_length: ", self.current_route_length)
		temp_row = para_action // SIZE
		temp_column = para_action % SIZE
		self.current_board[temp_row][temp_column] = self.current_player
		self.current_state = self.board_to_state(self.current_board)
		self.game_situation = self.compute_game_situation(self.current_board)
		self.current_route_states[self.current_route_length] = int(self.current_state)
		self.current_route_length = self.current_route_length + 1

		if self.current_player == WHITE:
			self.current_player = BLACK
		else:
			self.current_player = WHITE

	################
	# Compute the game situation using the state
	################
	def compute_game_situation_state(self, para_state):
		temp_board = self.state_to_board(para_state)
		return self.compute_game_situation(temp_board)

	################
	# Compute the game situation, TIE, WHITE, BLACK, UNFINISHED
	################
	def compute_game_situation(self, para_board):
		# Step 1. Horizontal
		for i in range(SIZE):
			if para_board[i][0] == EMPTY:
				continue
			# A whole row is the same.
			temp_same = True
			for j in range(1, SIZE):
				if para_board[i][j] != para_board[i][0]:
					temp_same = False
					break

			if temp_same:
				return para_board[i][0]

		# Step 2. Vertical
		for j in range(SIZE):
			if para_board[0][j] == EMPTY:
				continue
			temp_same = True # A whole column is the same.
			for i in range(1, SIZE):
				if para_board[i][j] != para_board[0][j]:
					temp_same = False
					break
			if temp_same:
				return para_board[0][j]

		# Step 3. Slope: Left-top to right-bottom.
		if para_board[0][0] != EMPTY:
			temp_same = True
			for i in range(1, SIZE):
				if para_board[i][i] != para_board[0][0]:
					temp_same = False
					break
			if temp_same:
				return para_board[0][0]

		# Step 4. Slope: Right-top to left-bottom.
		if para_board[0][SIZE - 1] != EMPTY:
			for i in range(1, SIZE):
				temp_same = True
				if para_board[i][SIZE - i - 1] != para_board[0][SIZE - 1]:
					temp_same = False
					break
			if temp_same:
				return para_board[0][SIZE - 1]

		# Step 5. Checkboard full
		temp_has_empty = False
		for i in range(SIZE):
			for j in range(SIZE):
				if para_board[i][j] == EMPTY:
					temp_has_empty = True
					break
			if temp_has_empty:
				break
		if not temp_has_empty:
			return TIE

		return UNFINISHED

	################
	# Print the board.
	################
	def print_board(self):
		temp_token = "0"
		#print("Checkerboard status:")
		for i in range (SIZE):
			print("--------")
			temp_line = "|"
			for j in range (SIZE):
				if (self.current_board[i][j] == WHITE):
					temp_token = "*"
				elif (self.current_board[i][j] == BLACK):
					temp_token = "x"
				else:
                			temp_token = "0"
				temp_line += temp_token + "|"
			print(temp_line)
		print("--------")

################
# Unit test.
################
if __name__ == '__main__':
	checkerboard = Checkerboard()

	checkerboard.get_valid_actions()
	temp_situation = checkerboard.compute_game_situation_state(123)
	checkerboard.print_board()
	print("Hello, tic-tac-toe checkerboard test, the situation is: ", temp_situation)
