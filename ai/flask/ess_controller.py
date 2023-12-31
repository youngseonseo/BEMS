from flask import request
from flask_restx import Resource, Api, Namespace, fields
import pandas as pd
import Ess_agent 
import datetime

Ess_controller = Namespace(
    name="ESS",
    description="ESS scheduling related APIs"
)

ess_fields = Ess_controller.model('ESS_REQUEST', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2018-11-28 07:23:00"),
    'SOC': fields.Float(description='현재 배터리 충전 상태(%)', required=True, example="50.0"),
    'BATTERY_POWER': fields.Float(description='현재 배터리 파워(kWh)', required=True, example="0.0"),
    '561_CONSUMPTION(kW)': fields.Float(description='561동 소비 전력', required=True, example="62.1733"),
    '562_CONSUMPTION(kW)': fields.Float(description='562동 소비 전력', required=True, example="62.1733"),
    '563_CONSUMPTION(kW)': fields.Float(description='563동 소비 전력', required=True, example="62.1733"),
})

Ess_controller.add_model

ess_response_fields = Ess_controller.model('ESS_RESPONSE', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대 다음 1분'),
    'SOC': fields.Float(description='호출 시간대 다음 1분 배터리 충전 상태(%)'),
    'BATTERY_POWER': fields.Float(description='호출 시간대 다음 1분 배터리 파워(kWh)'),
    'PREDICTED_CONSUMPTION': fields.Float(description='호출 시간대 다음 1분 예측 소비전력(kWh)'),
    'TOU(원/kWh)': fields.Float(description='호출 시간대 다음 1분 TOU(원/kWh)'),
    'THRESHOLD': fields.Float(description='고정 Threshold 값'),
    'NET_LOAD': fields.Float(description='min(0,BATTERY_POWER) + 예측 소비전력 -> 배터리 방전량으로 소비전력을 낮췄을 때 결과')
})

@Ess_controller.route('/optimal')
class EssOpitmalSchedule(Resource):
    @Ess_controller.expect(ess_fields)
    @Ess_controller.response(201, 'Success', ess_response_fields)
    @Ess_controller.response(500, 'Fail')
    def post(self):
        """최적의 배터리 스케줄링 결과를 제공합니다."""

        param = request.get_json()
        call_time = pd.Timestamp(param['TIMESTAMP'])
        agent = Ess_agent.DQNAgent(call_time)

        current_consumption = (param['561_CONSUMPTION(kW)'] + param['562_CONSUMPTION(kW)'] + param['563_CONSUMPTION(kW)']) / 60.0

        predicted_data = Ess_agent.predict(call_time, current_consumption)

        state = [param['SOC'], param['BATTERY_POWER']]
        for i in range(agent.predict_len):
            state.append(predicted_data[i])
            state.append(agent.get_tou(call_time + datetime.timedelta(minutes=i+1)))
       
        #[SoC, battery power, predicted_consumption ,TOU(원/kWh)]
        next_state = agent.take_step(state, agent.get_action(state))

        ret = {'TIMESTAMP': str(call_time + datetime.timedelta(minutes=1)),
               'SOC': next_state[0],
               'BATTERY_POWER': next_state[1],
               'PREDICTED_CONSUMPTION': predicted_data[0],
               'TOU(원/kWh)': state[3],
                'THRESHOLD' : agent.threshold,
                'NET_LOAD' : min(0, next_state[1]) + predicted_data[0]
               }
        
        return ret, 201
    
# @Ess.route('/common')
# class EssCommonSchedule(Resource):
#     # @Ess.expect(ess_fields)
#     # @Todo.response(201, 'Success', todo_fields_with_id)
#     def post(self):
#         """일반적인 배터리 스케줄링 결과를 제공합니다."""
        
#         return {
#             'hello': 'world',
#         }, 201
    
