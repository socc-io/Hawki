# -*-coding:utf-8
from flask import Flask, request
from flask_restful import Resource, Api
import simplejson
import requests
import json

# urllib2 는 https 를 지원하지 않는다고함
# kakao restapi https://developers.kakao.com/docs/restapi

app = Flask(__name__)
api = Api(app)

client_id = 'sqp83uhdtH1HAE7jPREy'
client_secret = 'HJEBO3Aod8'
search_url = "http://openapi.naver.com/v1/search/local.xml"
daum_app_key = '1b53126df63a2e4ea0dc1a236c67d0d8'  # 다음 api 키
daum_search_query = 'https://apis.daum.net/local/v1/search/keyword.json?apikey='

''' keyword 검색 response json data
{
  "channel": {
    "item": [
      {
        "phone": "02-6002-1880", # 핸드폰 번호
        "newAddress": "서울 강남구 영동대로 513", # 주소
        "imageUrl": "", #이미지 유얼엘
        "direction": "남서", #방향
        "zipcode": "135731",
        "placeUrl": "http://place.map.daum.net/26338954", # 장소 url
        "id": "26338954", # 빌딩 id
        "title": "카카오프렌즈 코엑스점", # 빌딩이름 full name
        "distance": "450",
        "category": "가정,생활 > 문구,사무용품 > 디자인문구 > 카카오프렌즈",
        "address": "서울 강남구 삼성동 159 코엑스몰 지하2층 G209호",
        "longitude": "127.05862601856802",
        "latitude": "37.51203258014444",
        "addressBCode": "1168010500"
      },
      ...
      "info": {
      "samename": {
        "keyword": "카카오프렌즈",
        "selected_region": ""
      },
      "count": "11",
      "page": "1",
      "totalCount": "11"
    }
  }

'''

class BuildingInfo(Resource):
    def getBuildInfoByName(self):  # 다음 키워드 검색 api를 사용, 다음에 장소정보 검색을 요청
        daum_search_keyword = '&query=' + '국제캠퍼스'
        daym_search_latitude = ''  # 경도
        daum_search_logitude = ''  # 위도
        daum_search_location = ''  # 다음 서치 쿼리를 만들기 위한 경도,위도 형식으로 만들어줘야함 &location
        daum_search_radius = ''  # 반경 &radius
        # 한글입력은 uriencode 를 사용해서 호출해야된다는데 그냥 되는걸 확인
        daum_search_full_query = daum_search_query + daum_app_key + daum_search_keyword;
        r = requests.get(daum_search_full_query)
        print 'url = ' + r.url
        print 'text = ' + r.text
        print 'status code = ' + str(r.status_code)
        print daum_search_full_query

    def parsingBuildInfoJsonData(self, json_data):  # 다음에서 받아온 json 정보를 파싱함
        data_list = json_data['channel']['item']
        for r in data_list:
            print r
        print json_data['channel']['info']['count']  # 저 이름이 들어간 마커의 갯수
        print 'parsing json data'

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
    app.run(debug=True, port=3000, host='0,0,0,0')
