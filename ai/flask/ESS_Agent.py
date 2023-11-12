import tensorflow as tf

import pandas as pd
import numpy as np
import datetime

from typing import Final
from collections import deque

import pmdarima as pm
    
class DQNAgent:
    def __init__(
        self,
        call_time:pd.Timestamp,
        predict_len: int = 3
    ):

        # battery parameters
        self.BATTERY_CAPACITY: Final = 10000 #kWh
        self.MIN_SOC: Final = 10
        self.MAX_SOC: Final = 90
        self.MAX_BATTERY_POWER: Final = self.BATTERY_CAPACITY / 2
        self.EFFICIENCY: Final = 0.9

        self.threshold: Final = 2.7

        # network parameters
        self.state_size: Final = 2 + predict_len * 2
        self.action_space: Final = [-0.005, -0.01, -0.05, -0.1, 0, 0.005, 0.01, 0.05, 0.1]

        self.predict_len: Final = predict_len

        self.initialize_agent(call_time)

    def initialize_agent(self, timestamp : pd.Timestamp):
        if timestamp.month >= 6 and timestamp.month <= 8:
            self.LOW_LOAD = 79.3
            self.MIDDLE_LOAD = 132.2
            self.HIGH_LOAD = 214.3

            self.dqn = tf.saved_model.load("ai/ESS/summer_ess2/")

        elif timestamp.month >= 9 and timestamp.month <= 10:
            self.LOW_LOAD = 79.3
            self.MIDDLE_LOAD = 101.8
            self.HIGH_LOAD = 132.5

            self.dqn = tf.saved_model.load("ai/ESS/fall_ess/")
        else :
            self.LOW_LOAD = 79.3
            self.MIDDLE_LOAD = 101.8
            self.HIGH_LOAD = 132.5

            self.dqn = tf.saved_model.load("ai/ESS/fall_ess/")
        

    def set_data(self, data):
        self.data = data

    def get_tou(self, timestmap : pd.Timestamp): 
        if timestmap.hour >= 22 or timestmap.hour < 8 : 
            return self.LOW_LOAD
        elif (timestmap.hour == 11) or (timestmap.hour >= 13 and timestmap.hour < 18) : 
            return self.HIGH_LOAD
        else :
            return self.MIDDLE_LOAD
        
    def get_action(self, state):
        
        q_value = self.dqn(tf.convert_to_tensor([state], dtype=tf.float32))[0]
        action = np.argmax(q_value) 

        return action
    
    def take_step(self, state, action):

        #state [SoC, battery power, pc_t1,tou_t1 ... t3 ]
        
        next_state = [state[0]]
        # battery power
        next_state.append(state[1] + self.action_space[action])

        # SoC
        next_state[0] += (next_state[1] / self.BATTERY_CAPACITY) * 100 * self.EFFICIENCY

        #[soc, b_p]
        return next_state



# data begin at 2022-08-27 12:00:00

arima_model = None
call_cnt = 0

# total_data = pd.read_csv("BEMS/preprocessed_data/[AI]총_소비전력 2022-07-18 00.00.00 ~ 2022-10-14 17.59.00.csv").set_index("TIMESTAMP")
total_data = pd.read_csv("preprocessed_data/[AI]총_소비전력 2022-07-18 00.00.00 ~ 2022-10-14 17.59.00.csv").set_index("TIMESTAMP")

best_window_size = 1200
train_end_date = pd.Timestamp("2022-08-27 11:59:00")
train_begin_date = train_end_date - datetime.timedelta(minutes=best_window_size)


def predict(call_time : pd.Timestamp, current_consumption):
    global call_cnt, arima_model, total_data, train_end_date, train_begin_date

    call_cnt += 1
    if call_cnt % 100 == 0:
        arima_model = None
        train_begin_date += datetime.timedelta(minutes=100)
        train_end_date = call_time

    
    if arima_model is None:
        train = total_data.loc[str(train_begin_date) : str(train_end_date)]

        arima_model = pm.auto_arima(train, d=get_ndiff(train), seasonal=False)

    forcasted_data = arima_model.predict(3)
    arima_model.update(current_consumption)

    return forcasted_data

def get_ndiff(train):
    kpss_diffs = pm.arima.ndiffs(train, alpha=0.05, test='kpss', max_d=6)
    adf_diffs = pm.arima.ndiffs(train, alpha=0.05, test='adf', max_d=6)
    return max(adf_diffs, kpss_diffs)