# -*-coding:utf-8
from flask import Flask, request, json
from flask_restful import Resource, Api, reqparse
from Predictor.pipeline import Pipeline
import requests

app = Flask(__name__)
api = Api(app)

client_id = 'sqp83uhdtH1HAE7jPREy'
client_secret = 'HJEBO3Aod8'
search_url = "http://openapi.naver.com/v1/search/local.xml"
daum_app_key = '1b53126df63a2e4ea0dc1a236c67d0d8'  # 다음 api 키
daum_search_query = 'https://apis.daum.net/local/v1/search/keyword.json?apikey='

# Init Pipeline
ppl = Pipeline()

''' keyword 검색 response json data
{
  "channel": {
    "item": [
      {
        ///"phone": "02-6002-1880", # 핸드폰 번호
        "newAddress": "서울 강남구 영동대로 513", # 주소
        "imageUrl": "", #이미지 유얼엘
        "direction": "남서", #방향
        "zipcode": "135731",
        "placeUrl": "http://place.map.daum.net/26338954", # 장소 url
        ///"id": "26338954", # 빌딩 id
        ///"title": "카카오프렌즈 코엑스점", # 빌딩이름 full name
        "distance": "450",
        "category": "가정,생활 > 문구,사무용품 > 디자인문구 > 카카오프렌즈",
        ///"address": "서울 강남구 삼성동 159 코엑스몰 지하2층 G209호",
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
    def getBuildInfoByName(self, name='국제캠퍼스'):  # 다음 키워드 검색 api를 사용, 다음에 장소정보 검색을 요청
        #args = parser.parse_args()
        #daum_search_keyword = '&query=' + args['buildName']
        #print daum_search_keyword
        daum_search_keyword = '&query=' + name
        daym_search_latitude = ''  # 경도
        daum_search_logitude = ''  # 위도
        daum_search_location = ''  # 다음 서치 쿼리를 만들기 위한 경도,위도 형식으로 만들어줘야함 &location
        daum_search_radius = ''  # 반경 &radius
        # 한글입력은 uriencode 를 사용해서 호출해야된다는데 그냥 되는걸 확인
        daum_search_full_query = daum_search_query + daum_app_key + daum_search_keyword;
        r = requests.get(daum_search_full_query)
        print r
        #print 'url = ' + r.url
        #print 'text = ' + r.text
        #print 'status code = ' + str(r.status_code)
        return self.parsingBuildInfoJsonData(json_data=r.json())
        #print daum_search_full_query

    def parsingBuildInfoJsonData(self, json_data):  # 다음에서 받아온 json 정보를 파싱함
        data_list = json_data['channel']['item']
        results = []
        for r in data_list:
            temp = {'id': r['id'],
                    'phone': r['phone'],
                    'title': r['title'],
                    'address': r['address']}
            results.append(temp)
            #print r['id']
            #print r['phone']
            #print r['title']
            #print r['address']

        #print json_data['channel']['info']['count']  # 저 이름이 들어간 마커의 갯수
        print 'parsing json data'
        return {'Build':results}

    def get(self):
        buildName = request.args.get('buildName')
        if buildName == '':
            return json.dumps(self.getBuildInfoByName())
        else:
            return json.dumps(self.getBuildInfoByName(name=buildName))

class GetPosition(Resource):
    global ppl
    def predictPositionByRssi(self, wrm={'abcd':-99}, buildId='COEX'):
        ppl.load_pipe(buildId)

        res = ppl.process(wrm, config={'building_id':buildId, 'min_rssi':-999})
        resJson = {
            'position': {
                'x': res[0],
                'y': res[1]
            }
        }
        return resJson

    def get(self):
        return json.dumps(self.predictPositionByRssi())

    def post(self):
        return json.dumps(self.predictPositionByRssi())

# Request Routing
#api.add_resource(BuildingInfo, '/buildinginfo')
api.add_resource(BuildingInfo, '/test')
api.add_resource(GetPosition, '/getPosition')

