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
daum_app_key = '1b53126df63a2e4ea0dc1a236c67d0d8'  # DAUM API KEY
daum_search_query = 'https://apis.daum.net/local/v1/search/keyword.json?apikey='

# Init Pipeline
ppl = Pipeline()

class BuildingInfo(Resource):
    # Request location info to DAUM
    def getBuildInfoByName(self, name='국제캠퍼스'):  # Request location info to DAUM
        daum_search_keyword = '&query=' + name
        daym_search_latitude = ''  # latitude
        daum_search_logitude = ''  # longitude
        daum_search_location = ''  # var for searcing query(get) (&location)
        daum_search_radius = ''  # radius (&radius)

        daum_search_full_query = daum_search_query + daum_app_key + daum_search_keyword;
        r = requests.get(daum_search_full_query)

        return self.parsingBuildInfoJsonData(json_data=r.json())

    # Parse json data from DAUM API
    def parsingBuildInfoJsonData(self, json_data):
        data_list = json_data['channel']['item']
        results = []
        for r in data_list:
            temp = {'id': r['id'],
                    'phone': r['phone'],
                    'title': r['title'],
                    'address': r['address']}
            results.append(temp)

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

    def post(self):
        return json.dumps(self.predictPositionByRssi())

class CollectRssi(Resource):
    def saveRssiInfo(self, data={"bid":"TEST","x":2,"y":2,"z":2,"rssi":[{"sid":"39:33:34:f2:dd:cc","dbm":-47}]}):
        bid = data['bid']
        savePath = 'Data/WRM/RAW/' + bid + '.dat'
        with open(savePath, 'a') as f:
            f.write(json.dumps(data) + '\n')

        return 'save data'

    def post(self):
        jsonObj = request.get_json()
        return self.saveRssiInfo(jsonObj)

# Request Routing
api.add_resource(BuildingInfo, '/buildinginfo')
api.add_resource(GetPosition, '/getposition')
api.add_resource(CollectRssi, '/collectrssi')

