from flask import request
from flask_restx import Resource, Api, Namespace, fields

Predict = Namespace(
    name="Predict",
    description="Energy Prediction related APIs"
)

predict_fields = Predict.model('Predict', {  
    'TIMESTAMP': fields.DateTime(description='호출 시간대', required=True, example="2018-11-28 07:23:00"),
    'BUILDING': fields.Integer(description='동', required=True, example="561"),
    'FLOOR': fields.Integer(description='층', required=True, example="1"),
    'CONSUMPTION(W)': fields.Integer(description='소비 전력', required=True, example="62")
})


# todo_fields_with_id = Todo.inherit('Todo With ID', todo_fields, {
#     'todo_id': fields.Integer(description='a Todo ID')
# })

'''
백엔드에서 {시간대/동/층/소비전력} 포스팅
또는
걍 바로 api 호출하고 
여기서 한 스텝 긁어오기
'''

@Predict.route('/Elec/')
class PredictElec(Resource):
    @Predict.expect(predict_fields)
    # @Todo.response(201, 'Success', todo_fields_with_id)
    def post(self):
        """아파트 층 별 소비 전력 에측 결과를 제공합니다."""
        
        
        return {'TIMESTAMP': {0: '2018-11-28 07:23:00',
  1: '2018-11-28 07:24:00',
  2: '2018-11-28 07:25:00'},
 'PREdIDCT_ELEC': {0: 224.0, 1: 176.1, 2: 176.0}}, 201
