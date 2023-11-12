import pandas as pd
import numpy as np

import pmdarima as pm

import datetime

total_df = pd.read_csv("preprocessed_data/[AI]아파트_층별_소비전력_2023-03-16 06.30.00 ~ 2023-08-30 10.30.00.csv", usecols=[1,2,3,4])
total_df['TIMESTAMP'] = pd.to_datetime(total_df['TIMESTAMP'])
total_df.set_index(total_df['TIMESTAMP'], inplace=True)
total_df.drop('TIMESTAMP', inplace=True, axis = 1)

models = {}

CHECK_SIZE = 10
WINDOW_SIZE = 670
initial_date = total_df.index[0]

def get_ndiff(train):
    kpss_diffs = pm.arima.ndiffs(train, alpha=0.05, test='kpss', max_d=6)
    adf_diffs = pm.arima.ndiffs(train, alpha=0.05, test='adf', max_d=6)
    return max(adf_diffs, kpss_diffs)

def predict(building:int, floor:int, calltime:pd.Timestamp):
    key = building*100 + floor
   
    if key not in models:
        end_date = initial_date + (WINDOW_SIZE - 1) * pd.Timedelta(minutes=10) 
        train = total_df.query("`BUILDING` == @building and `FLOOR` == @floor").loc[initial_date:end_date].drop(['BUILDING', 'FLOOR'], axis=1)
        model = pm.auto_arima(train, d=get_ndiff(train), seasonal=False)
        value = {'model':model, 'call_cnt':0, 'initial_date':initial_date}
        models[key] = value

        return model.predict(1)[0]
    
    model = None
    value = models[key]
    if value["call_cnt"] % CHECK_SIZE == 0 :
        value["call_cnt"] = 0
        value["initial_date"] += CHECK_SIZE * pd.Timedelta(minutes=10) 
        end_date = value["initial_date"] + (WINDOW_SIZE - 1) * pd.Timedelta(minutes=10) 
        train = total_df.query("`BUILDING` == @building and `FLOOR` == @floor").loc[value["initial_date"]:end_date].drop(['BUILDING', 'FLOOR'], axis=1)
        model = pm.auto_arima(train, d=get_ndiff(train), seasonal=False)
        value["model"] = model

        models[key] = value

    else :
        value["call_cnt"] += 1
        model = value["model"]

        models[key] = value
    
    return model.predict(1)[0]

