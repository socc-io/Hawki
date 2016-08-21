from flask import Flask, request
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)

class BuildingInfo(Resource):
	# TODO young je name을이용해서 빌딩 id 를 가져오기
	# TODO soon ho 안드로이드랑 파이썬 플라스크 request response
    def get(self):
	print request.json['bid']
	print request.json['name']
	print request.json['longitude']
	print request.json['latitude']
        return {'message': 'building good!'}

class IndoorPosition(Resource):
    def get(self):
	print request.json['bid']
	print request.json['rssi']
        return {'message': 'indoor good!'}

# Request Routing
api.add_resource(BuildingInfo, '/buildinginfo')
api.add_resource(IndoorPosition, '/indoorposition')

# Init Flask server
if __name__ == '__main__':
    app.run(debug=True,port=3000)
