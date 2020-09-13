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
import checkerboard
import vagent
import player

############################
# The umpire who is in charge of the whole process.
############################
class VUmpire:
	################
	# The constructor.
	################
	def __init__(self, para_epsilon = 0.1, para_alpha = 0.1):
		# Step 1. Accept parameters.
		self.environment = checkerboard.Checkerboard()
		self.agent_array = []
		self.agent_array.append(vagent.VAgent(self.environment, checkerboard.WHITE, para_epsilon, para_alpha))
		self.agent_array.append(vagent.VAgent(self.environment, checkerboard.BLACK, para_epsilon, para_alpha))

		# Step 2. Initialize member variables.
		self.winner = 0
		self.win_times_array = np.zeros(3)
		self.player_checker_board = np.zeros((checkerboard.SIZE, checkerboard.SIZE))
		
	################
	# Reset for a new game.
	################
	def reset_for_game(self):
		self.winner = checkerboard.EMPTY
		for i in range(checkerboard.SIZE):
			for j in range(checkerboard.SIZE):
				self.player_checker_board[i][j] = checkerboard.EMPTY
		self.environment.reset()

	################
	# Print the learning results.
	################
	def print_learning_results(self):
		print("Agent 1: ")
		self.agent_array[0].print_learning_results()
		print("Agent 2: ")
		self.agent_array[1].print_learning_results()

        ################
	# Train.
	################
	def train(self, para_episodes = 1000):
		self.win_times_array = np.zeros(3)
		for i in range(2):
			self.agent_array[i].reset()
		
		for i in range(para_episodes):
                        # Step 1.1 Reset.
			self.environment.reset()
			temp_current_player = 0;
			temp_game_situation = checkerboard.UNFINISHED

			# Step 1.2. Reach a final state each time.
			while (temp_game_situation == checkerboard.UNFINISHED):
				temp_game_situation = self.agent_array[temp_current_player].step()
				temp_current_player = (temp_current_player + 1) % 2
			# print("Game situation: ", temp_game_situation)
			self.win_times_array[temp_game_situation] = self.win_times_array[temp_game_situation] + 1

			# Step 1.3. Now update the agents
			for j in range(2):
				self.agent_array[j].backup()

	################
	# Play. For agents there is only one game if the greedy approach is employed.
	################
	def play(self):
                #Step 1. Reset for the play stage.
		self.environment.reset()
		self.environment.print_board()
		temp_current_player = 0;
		temp_game_situation = checkerboard.UNFINISHED;
		for i in range(2):
			self.agent_array[i].set_stage(False)
			#self.agent_array[i].reset()

		# Now play.
		while (temp_game_situation == checkerboard.UNFINISHED):
			temp_game_situation = self.agent_array[temp_current_player].step()
			temp_current_player = (temp_current_player + 1) % 2
			self.environment.print_board()
		return temp_game_situation


	################
	# Play. For agents there is only one game if the greedy approach is employed.
	################
	def human_machine_play(self):
                #Step 1. Reset for the play stage.
		self.environment.reset()
		temp_current_player = 0;
		temp_game_situation = checkerboard.UNFINISHED;

		self.agent_array[0] = player.HumanPlayer(self.environment)
		#self.agent_array[1].set_stage(False)

		temp_winner = self.play()
		if (temp_winner == checkerboard.WHITE):
			print("You win!")
		elif (temp_winner == checkerboard.BLACK):
			print("You lose :<")
		else:
			print("It is a tie")
            
################
#The main entrance of the program.
################
if __name__ == '__main__':
	temp_umpire = VUmpire(para_epsilon = 0.01, para_alpha = 0.1)
	temp_rounds = int(1e4)
	print("***Training rounds (change this setting to 1e5 in vumpire.py to obtain a smart agent): ", temp_rounds)
	temp_umpire.train(temp_rounds)
	print("In the training stage, win times (tie, white, black): ", temp_umpire.win_times_array)
	temp_umpire.print_learning_results()

	print("***Now two agents plays one game: ")
	temp_winner = temp_umpire.play()
	print("The winner is (0 for tie, 1 for white and 2 for black): ", temp_winner)
	
	print("***Now you can play with me :)")
	temp_umpire.human_machine_play()
	print("***The end.")

