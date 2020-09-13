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
import random
import checkerboard

############################
# The checkerboard which is able to judge the game situation.
############################
class VAgent:
	################
	# The constructor.
	################
	def __init__(self, para_board, para_symbol = checkerboard.WHITE, para_epsilon = 0.1, para_alpha = 0.1):
		# Step 1. Accept parameters.
		self.environment = para_board
		self.symbol = para_symbol
		self.epsilon = para_epsilon
		self.alpha = para_alpha
		
		# Step 2. Set states initialize values. 
		self.initial_value_array = np.zeros(checkerboard.NUM_STATES)
		for i in range(checkerboard.NUM_STATES):
			temp_situation = self.environment.compute_game_situation_state(i)
			if temp_situation == checkerboard.UNFINISHED:
				self.initial_value_array[i] = 0.5
			elif (temp_situation == checkerboard.TIE):
				self.initial_value_array[i] = 0.5
			elif (temp_situation == self.symbol):
				self.initial_value_array[i] = 1.0 # Winner
			else:
				self.initial_value_array[i] = 0.0 # Loser
		#print("initial_value_array:", self.initial_value_array)

		# Step 3. Initialize other member variables.
		self.value_array = np.zeros(checkerboard.NUM_STATES)
		self.training_stage = True
		self.recent_action = 0
		
	################
	# Reset.
	################
	def reset(self):
		self.value_array = self.initial_value_array.copy()

	################
	# Set stage, True for training and false for testing.
	################
	def set_stage(self, para_training_stage):
		self.training_stage = para_training_stage

	################
	# Compute the next state.
	################
	def compute_next_state(self, para_current_state, para_action):
		# print("compute_next_state", para_action)
		temp_base = 1
		for j in range(para_action):
			temp_base = temp_base * 3

		return int(para_current_state + self.symbol * temp_base)

	################
	# Print the learning results.
	################
	def print_learning_results(self):
		print("Value array: ", self.value_array)

	################
	# Compute the next state.
	################
	def step(self):
		# Step 1. Which actions are valid? What are the values of corresponding states?
		temp_valid_action_list = self.environment.get_valid_actions()
		temp_length = len(temp_valid_action_list)
		temp_value_array = np.zeros(temp_length)
		temp_current_state = self.environment.get_current_state();
		temp_next_state = 0
		for i in range(temp_length):
			temp_next_state = self.compute_next_state(temp_current_state, temp_valid_action_list[i])
			temp_value_array[i] = self.value_array[temp_next_state]


		# Step 2. Choose one action to take.
		self.recent_action = self.select_action(temp_value_array, temp_valid_action_list)
		#if (not self.training_stage):
		#	print("player {}, action: {}".format(self.symbol, self.recent_action))
		temp_next_state = self.compute_next_state(temp_current_state, self.recent_action)

		# Step 3. Tell the environment to change.
		self.environment.step(self.recent_action)

		# Step 4. Return the environment's situation.
		temp_situation = int(self.environment.get_game_situation())
		return int(self.environment.get_game_situation())

	################
	# Backup for this route, update the value array. This is the core code.
	################
	def backup(self):
		temp_current_state = 0
		temp_next_state = 0
		# print("Player {} ".format(self.symbol));

		temp_route_length = self.environment.get_current_route_length();
		temp_route_states = self.environment.get_current_route_states();
		#SimpleTools.variableTrackingOutput(
		#environment.stateToCheckerboardString(temp_route_states[temp_route_length - 1]));
		#SimpleTools.variableTrackingOutput(
		#"The final state is: " + temp_route_states[temp_route_length - 1] + "\r\n");
		for i in range(temp_route_length - 2, -1, -1):
			temp_next_state = int(temp_route_states[i + 1])
			temp_current_state = int(temp_route_states[i])

			#SimpleTools.variableTrackingOutput(
			#print("value_array[{}] from {}".format(temp_current_state, self.value_array[temp_current_state]))
			temp_change = self.value_array[temp_next_state] - self.value_array[temp_current_state]
			self.value_array[temp_current_state] = self.value_array[temp_current_state] + self.alpha * temp_change
			#print(" to {}".format(self.value_array[temp_current_state]))
			#SimpleTools.variableTrackingOutput(
			#		environment.stateToCheckerboardString(temp_current_state));

	################
	# Select an action.
	################
	def select_action(self, para_value_array, para_valid_actions):
		#print("para_value_array: ", para_value_array)
		#print("para_valid_actions: ", para_valid_actions)
		if (len(para_valid_actions) == 0):
			print("No action to select.")
		if (self.training_stage and (random.random() < self.epsilon)):
			return self.select_random_action(para_value_array, para_valid_actions)
		else:
			return self.select_best_action(para_value_array, para_valid_actions)
		
	################
	# Select a random action.
	################
	def select_random_action(self, para_value_array, para_valid_actions):
		temp_index = random.randint(0, len(para_valid_actions) - 1)
		# print("temp_index = ", temp_index)
		return para_valid_actions[temp_index]
	
	################
	# Select a best action.
	################
	def select_best_action(self, para_value_array, para_valid_actions):
		temp_max_value = -1;
		temp_best_action_array = np.zeros(checkerboard.NUM_ACTIONS)
		temp_num_best_actions = 0;

		for i in range(len(para_value_array)):
			if (temp_max_value < para_value_array[i]):
				temp_num_best_actions = 0
				temp_max_value = para_value_array[i]
				temp_best_action_array[0] = para_valid_actions[i]
				temp_num_best_actions = temp_num_best_actions + 1
			elif (temp_max_value == para_value_array[i]):
				temp_best_action_array[temp_num_best_actions] = para_valid_actions[i]
				temp_num_best_actions = temp_num_best_actions + 1

		#print("temp_num_best_actions = ", temp_num_best_actions)
		#print("temp_best_action_array = ", temp_best_action_array)

		temp_index = random.randint(0, temp_num_best_actions - 1)
	
		return int(temp_best_action_array[temp_index])

################
# Unit test.
################
if __name__ == '__main__':
	temp_checkerboard = checkerboard.Checkerboard()
	temp_vagent = VAgent(temp_checkerboard)
	temp_vagent.reset()

	for i in range(10):
		print("r: ", random.randint(0, 0))

	print("Hello, VAgent.")
