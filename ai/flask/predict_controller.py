from flask import request
from flask_restx import Resource, Api, Namespace, fields

Predict_controller = Namespace(
    name="Predict",
    description="Energy Prediction related APIs"
)

predict_field = Predict_controller.model('ELEC_PREDICT_REQUEST', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2018-11-28 07:23:00"),
    'BUILDING': fields.Integer(description='동', required=True, example="561"),
    'FLOOR': fields.Integer(description='층', required=True, example="1"),
    'CONSUMPTION(W)': fields.Integer(description='소비 전력', required=True, example="62")
})


predict_response_field = Predict_controller.model('ELEC_PREDICT_RESPONSE', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2018-11-28 07:23:00"),
    'BUILDING': fields.Integer(description='동', required=True, example="561"),
    'FLOOR': fields.Integer(description='층', required=True, example="1"),
    'CONSUMPTION(W)': fields.Integer(description='소비 전력', required=True, example="62")
})


@Predict_controller.route('/elec/')
class PredictElec(Resource):
    @Predict_controller.expect(predict_field)
    @Predict_controller.response(201, 'Success', predict_response_field)
    @Predict_controller.response(500, 'Fail')
    def post(self):
        """아파트 층 별 소비 전력 에측 결과를 제공합니다."""
        
        
        return {'TIMESTAMP': {0: '2018-11-28 07:23:00',
  1: '2018-11-28 07:24:00',
  2: '2018-11-28 07:25:00'},
 'PREdIDCT_ELEC': {0: 224.0, 1: 176.1, 2: 176.0}}, 201
