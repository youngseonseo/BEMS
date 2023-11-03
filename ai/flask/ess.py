from flask import request
from flask_restx import Resource, Api, Namespace, fields
import pandas as pd

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
        
        # simulation_len = 1440

        # # agent.initialize_episode(test_df[:simulation_len])
        # agent.set_data(test_df[:simulation_len + predict_len + 1])
        # agent.set_threshold()

        # state = [20, 0, train_df.iloc[start_num,0], agent.get_tou(train_df.iloc[start_num].name)]
        # # state = [1, agent.battery_capacity * 0.05, agent.data.iloc[0,0]]
        # for i in range(predict_len):
        #     state.append(agent.data.iloc[i+1, 0])
        #     state.append(agent.get_tou(agent.data.iloc[i+1].name))

        # #[SoC, battery power, power consumption_1, tou_1, power consumption_2, tou_2, pc3, tou_3, pc4,tou_4]

        # powers = [state[1]]
        # SoCs = [state[0]] 
        # net_loads = [state[1] + state[2]]
        # tous = [state[3]]
        # for step in range(1, simulation_len):
        #     q_value = agent.dqn(tf.convert_to_tensor([state], dtype=tf.float32))[0]
        #     action = np.argmax(q_value) 

        #     next_state, reward = agent.take_step(state, action, step) 
            
        #     state = next_state
        #     powers.append(state[1])
        #     SoCs.append(state[0])
        #     net_loads.append( min(state[1],0) + state[2])
        #     tous.append(state[3])

        '''
            백엔드에서 
        '''

        return {'TIMESTAMP': {0: '2018-11-28 07:23:00',
  1: '2018-11-28 07:24:00',
  2: '2018-11-28 07:25:00'},
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
    
