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
# Human interface
# input a number to put a chessman
# | q | w | e |
# | a | s | d |
# | z | x | c |
############################
class HumanPlayer:
	################
	# The constructor.
	################
	def __init__(self, para_board, para_symbol = checkerboard.WHITE):
		self.environment = para_board
		self.symbol = para_symbol
		self.keys = ['q', 'w', 'e', 'a', 's', 'd', 'z', 'x', 'c']
	
	################
	# Cope with the code of class VAgent.
	################
	def set_stage(self, para_training_stage):
		return

	################
	# Step.
	################
	def step(self):
		#print(self.environment.current_board)
		temp_key = input("Input your position using q, w, e, a, s, d, z, x, c and a return): ")
		self.recent_action = self.keys.index(temp_key)
		self.environment.step(self.recent_action)

		return int(self.environment.get_game_situation())

################
# Unit test.
################
if __name__ == '__main__':
	temp_checkerboard = checkerboard.Checkerboard()
	temp_player = HumanPlayer(temp_checkerboard)

	print("Hello, human player.")
