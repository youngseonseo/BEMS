from flask import Flask
from flask_restx import Resource, Api
from ess import Ess
from predict import Predict

app = Flask(__name__)
api = Api(
    app,
    version='0.1',
    title="임재동 BEMS API Server",
    # contact="jaedong0im@gmail.com",
    license="MIT"
)

api.add_namespace(Ess, '/ess')
api.add_namespace(Predict, '/predict')

if __name__ == "__main__":
    app.run(debug=True, port=10000)