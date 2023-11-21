import pandas as pd
import numpy as np

from xgboost.sklearn import XGBRegressor
from sklearn.metrics import mean_absolute_error

import datetime

total_df = pd.read_csv("preprocessed_data/[AI]아파트_층별_소비전력_1시간_2023-03-16 06.00.00 ~ 2023-08-30 10.00.00.csv", usecols=[1,2,3,4])
total_df['TIMESTAMP'] = pd.to_datetime(total_df['TIMESTAMP'])
total_df.set_index(total_df['TIMESTAMP'], inplace=True)
total_df.drop('TIMESTAMP', inplace=True, axis = 1)

models = {}

PREDICT_SIZE = 24
initial_date = total_df.index[0]
end_date = total_df.iloc[int(len(total_df) * 0.9)].name

def predict(building:int, floor:int, calltime:pd.Timestamp):
    key = building*100 + floor

    floor_df = total_df.query("`BUILDING` == @building and `FLOOR` == @floor")
   
    if key not in models:
        train_df = floor_df.loc[initial_date:end_date].drop(['BUILDING', 'FLOOR'], axis=1)
        
        train_df['y'] = train_df.shift(PREDICT_SIZE)
        train_df.dropna(inplace=True)

        model = XGBRegressor(
            objective =  'reg:squarederror',
            eval_metric = mean_absolute_error,
            n_estimators=1000,
            gamma = 1,
            reg_lambda = 0, 
            learning_rate = 0.01,
            max_depth=10, 
            scale_pos_weight=100
        )

        x = train_df.values[:,:1]
        y = train_df.values[:,-1:]
        model.fit(x, y)

        value = {'model':model}
        models[key] = value

    predict_df = floor_df.loc[calltime + pd.Timedelta(hours=1) : calltime + pd.Timedelta(hours=24)]
    
    return models[key]['model'].predict(predict_df['CONSUMPTION(kW)']).tolist()

