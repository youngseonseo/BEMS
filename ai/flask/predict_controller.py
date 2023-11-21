from flask import request
from flask_restx import Resource, Api, Namespace, fields

import Elec_arima_agent
import Elec_xgboost_agent


import pandas as pd

Predict_controller = Namespace(
    name="Predict",
    description="Energy Prediction related APIs"
)

predict_field = Predict_controller.model('ELEC_PREDICT_REQUEST', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2023-08-13 17:00:00"),
    'BUILDING': fields.Integer(description='동', required=True, example="561"),
    'FLOOR': fields.Integer(description='층', required=True, example="1"),
    'CONSUMPTION(W)': fields.Integer(description='소비 전력', required=True, example="62")
})


predict_response_field = Predict_controller.model('ELEC_PREDICT_RESPONSE', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대 + 10분',example="2023-08-13 17:00:00"),
    'PREDICTION': fields.Integer(description='예측 소비 전력(W)', example="62")
})

predict_24_field = Predict_controller.model('ELEC_PREDICT_24HOUR_REQUEST', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대 1시간 단위', required=True, example="2023-08-13 17:00:00"),
    'BUILDING': fields.Integer(description='동', required=True, example="561"),
    'FLOOR': fields.Integer(description='층', required=True, example="1"),
    'CONSUMPTION(kW)': fields.Float(description='소비 전력(kW)', required=True, example="2.1")
})

predict_24_response_field = Predict_controller.model('ELEC_PREDICT_24HOUR_RESPONSE', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대 + 1시간 ~ 호출 시간대 + 24시간',example="[2023-08-13 18:00:00,2023-08-13 18:00:00, 2023-08-13 17:00:00, ... , 2023-08-14 17:00:00]"),
    'PREDICTION': fields.Integer(description='예측 소비 전력 배열(kW)', example="[2.424023389816284,2.505211353302002, 2.5154974460601807, ... ,2.5154974460601807]")
})

@Predict_controller.route('/elec/')
class PredictElec(Resource):
    @Predict_controller.expect(predict_field)
    @Predict_controller.response(201, 'Success', predict_response_field)
    @Predict_controller.response(500, 'Fail')
    def post(self):
        
        param = request.get_json()
        calltime = param["TIMESTAMP"]
        building = param["BUILDING"]
        floor = param["FLOOR"]

        prediction = Elec_arima_agent.predict(building, floor, calltime)

        return {'TIMESTAMP': str(pd.Timestamp(calltime) + pd.Timedelta(minutes=10)),
        'PREDICTION': prediction}, 201

@Predict_controller.route('/elec_24/')
class PredictElec24(Resource):
    @Predict_controller.expect(predict_24_field)
    @Predict_controller.response(201, 'Success', predict_24_response_field)
    @Predict_controller.response(500, 'Fail')
    def post(self):
        
        param = request.get_json()
        calltime = param["TIMESTAMP"]
        building = param["BUILDING"]
        floor = param["FLOOR"]

        prediction = Elec_xgboost_agent.predict(building, floor, pd.Timestamp(calltime))

        timestamps = []
        for i in range(1, 25):
            timestamps.append(str(pd.Timestamp(calltime) + pd.Timedelta(hours=i)))

        return {'TIMESTAMP': timestamps,
        'PREDICTION': prediction}, 201
