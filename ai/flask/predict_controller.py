from flask import request
from flask_restx import Resource, Api, Namespace, fields

import Elec_arima_agent

import pandas as pd

Predict_controller = Namespace(
    name="Predict",
    description="Energy Prediction related APIs"
)

predict_field = Predict_controller.model('ELEC_PREDICT_REQUEST', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2018-11-28 07:20:00"),
    'BUILDING': fields.Integer(description='동', required=True, example="561"),
    'FLOOR': fields.Integer(description='층', required=True, example="1"),
    'CONSUMPTION(W)': fields.Integer(description='소비 전력', required=True, example="62")
})


predict_response_field = Predict_controller.model('ELEC_PREDICT_RESPONSE', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대 + 10분',example="2018-11-28 07:30:00"),
    'PREDICTION': fields.Integer(description='예측 소비 전력(W)', example="62")
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
