#-*-coding:utf-8
from flask import Flask, request
from flask_restful import Resource, Api
import simplejson
import requests

# urllib2 는 https 를 지원하지 않는다고함
# kakao restapi https://developers.kakao.com/docs/restapi

app = Flask(__name__)
api = Api(app)

client_id = 'sqp83uhdtH1HAE7jPREy'
client_secret = 'HJEBO3Aod8'
search_url = "http://openapi.naver.com/v1/search/local.xml"
daum_app_key = '1b53126df63a2e4ea0dc1a236c67d0d8' # 다음 api 키
daum_search_query = 'https://apis.daum.net/local/v1/search/keyword.json?apikey='

class BuildingInfo(Resource):

    def getBuildInfoByName(self):
        daum_search_keyword = '&query=' + '국제캠퍼스'
        daym_search_latitude = ''  # 경도
        daum_search_logitude = ''  # 위도
        daum_search_location = ''  # 다음 서치 쿼리를 만들기 위한 경도,위도 형식으로 만들어줘야함 &location
        daum_search_radius = ''  # 반경 &radius
        daum_search_full_query = daum_search_query + daum_app_key + daum_search_keyword;
        r = requests.get(daum_search_full_query)
        print 'url = ' + r.url
        print 'text = ' + r.text
        print 'status code = ' + str(r.status_code)
        print daum_search_full_query

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