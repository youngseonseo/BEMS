from flask import request
from flask_restx import Resource, Api, Namespace, fields
import pandas as pd
import Agent

Ess = Namespace(
    name="ESS",
    description="ESS scheduling related APIs"
)

ess_fields = Ess.model('ESS', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2018-11-28 07:23:00"),
    '561_CONSUMPTION(kW)': fields.Float(description='561동 소비 전력', required=True, example="62.1733"),
    '562_CONSUMPTION(kW)': fields.Float(description='562동 소비 전력', required=True, example="62.1733"),
    '563_CONSUMPTION(kW)': fields.Float(description='563동 소비 전력', required=True, example="62.1733"),
})

# todo_fields_with_id = Todo.inherit('Todo With ID', todo_fields, {
#     'todo_id': fields.Integer(description='a Todo ID')
# })

'''
백엔드에서 한 스텝 {타임/각 동별 실제 소비전력} 포스팅
또는
걍 백엔드 서버에서 데이터 한스텝 가져와서
모델 갱신, 60분 예측 -> 60분 스케줄링 결과 전송

'''

# 모든 동 소비전력의 합 예측
def predict():
    return {'TIMESTAMP': {0: '2018-11-28 07:23:00',
  1: '2018-11-28 07:24:00',
  2: '2018-11-28 07:25:00'}, 
  'PREDICT_LOAD': {
      {0: 224.0, 1: 176.1, 2: 176.0}
  }}

@Ess.route('/optimal')
class EssOpitmalSchedule(Resource):
    @Ess.expect(ess_fields)
    # @Todo.response(201, 'Success', todo_fields_with_id)
    def post(self):
        """최적의 배터리 스케줄링 결과를 제공합니다."""

        agent = Agent.DQNAgent()
        
        


        return {'TIMESTAMP': {0: '2018-11-28 07:23:00',1: '2018-11-28 07:24:00',2: '2018-11-28 07:25:00'},
        'SOC': {0: 50.0, 1: 50.00576, 2: 50.01728},
        'BatteryPower': {0: 0, 1: 64, 2: 128},
        'NetLoad': {0: 224.0, 1: 176.0, 2: 176.0},
        'PredictLoad': {0: 224.0, 1: 176.1, 2: 176.0},
        'TOU(원/kWh)': {0: 98.1, 1: 98.1, 2: 98.1}}, 201
    
@Ess.route('/common')
class EssCommonSchedule(Resource):
    # @Ess.expect(ess_fields)
    # @Todo.response(201, 'Success', todo_fields_with_id)
    def post(self):
        """일반적인 배터리 스케줄링 결과를 제공합니다."""
        
        return {
            'hello': 'world',
        }, 201
    
