import tensorflow as tf
from tensorflow.keras import optimizers, losses
from tensorflow.keras import Model

import pandas as pd
import numpy as np

from typing import Final
from collections import deque


class Network(tf.Model):
    def __init__(self, state_size: int, action_size: int, hidden_size: int
    ):
        super(Network, self).__init__()
        
        self.layer1 = tf.keras.layers.Dense(hidden_size, activation='relu')
        self.layer2 = tf.keras.layers.Dense(hidden_size, activation='relu')
        self.value = tf.keras.layers.Dense(action_size)

    def call(self, state):
        layer1 = self.layer1(state)
        layer2 = self.layer2(layer1)
        value = self.value(layer2)
        return value
    
class DQNAgent:
    def __init__(
        self,
        predict_len: int = 3
    ):

        # battery parameters
        self.BATTERY_CAPACITY: Final = 10000 #kWh
        self.MIN_SOC: Final = 10
        self.MAX_SOC: Final = 90
        self.MAX_BATTERY_POWER: Final = self.BATTERY_CAPACITY / 2
        self.EFFICIENCY: Final = 0.9

        # network parameters
        self.state_size: Final = 2 + predict_len * 2
        self.action_space: Final = [-0.005, -0.01, -0.05, -0.1, 0, 0.005, 0.01, 0.05, 0.1]

        self.predict_len: Final = predict_len

    def initialize_agent(self, timestamp : pd.Timestamp):
        if timestamp.month >= 6 and timestamp.month <= 8:
            self.LOW_LOAD = 79.3
            self.MIDDLE_LOAD = 132.2
            self.HIGH_LOAD = 214.3

            self.dqn = tf.saved_model.load("./summer_ess/")

        elif timestamp.month >= 9 and timestamp.month <= 10:
            self.LOW_LOAD = 79.3
            self.MIDDLE_LOAD = 101.8
            self.HIGH_LOAD = 132.5

            self.dqn = tf.saved_model.load("./fall_ess/")
        else :
            self.LOW_LOAD = 79.3
            self.MIDDLE_LOAD = 101.8
            self.HIGH_LOAD = 132.5

            self.dqn = tf.saved_model.load("./fall_ess/")
        

    def set_data(self, data):
        self.data = data

    def get_tou(self, timestmap : pd.Timestamp): 
        if timestmap.hour >= 22 or timestmap.hour < 8 : 
            return self.LOW_LOAD
        elif (timestmap.hour == 11) or (timestmap.hour >= 13 and timestmap.hour < 18) : 
            return self.HIGH_LOAD
        else :
            return self.MIDDLE_LOAD
        
    def get_action(self, state, epsilon):
        if np.random.rand() <= epsilon:
            action = np.random.choice(self.action_size)
        else:
            q_value = self.dqn(tf.convert_to_tensor([state], dtype=tf.float32))[0]
            action = np.argmax(q_value) 

        return action
    
    def take_step(self, state, action, step_num):
        
        #[SoC, battery power, power consumption_1, tou_1, pc_2, tou_2, pc3, tou_3, pc4,tou_4]
        
        next_state = [state[0]]
        # battery power
        next_state.append(state[1] + self.action_space[action])

        # power consumption
        for i in range(self.predict_len):
            next_state.append(self.data.iloc[step_num + i, 0])
            next_state.append(self.get_tou(self.data.iloc[step_num + i].name))

        # SoC
        next_state[0] += (next_state[1] / self.BATTERY_CAPACITY) * 100 * self.EFFICIENCY

        return next_state