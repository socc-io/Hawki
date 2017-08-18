# -*- coding: utf-8 -*-

from Web.service.daumAPI import DaumSearchEngine
from flask_restful import Resource
from Web import ppl
from flask import jsonify, request

import CONFIG, json

class BuildingInfo(Resource):
    # Request location info to DAUM
    def getBuildInfoByName(self, name='김포공항'):  # Request location info to DAUM
        building_search_engine = None
        if CONFIG.COMMON['BUILDING_SEARCH_ENGINE'] == 'DAUM':
            building_search_engine = DaumSearchEngine()

        return building_search_engine.getBuildInfoByName(name)

    def get(self):
        buildName = request.args.get('buildName', '김포공항')
        res = self.getBuildInfoByName(name=buildName)
        if not res:
            return u'다음 API요청에 실패했습니다. 키 값이 만료되었을 수 있습니다'
        res = jsonify(res)
        print 'requested daum api: ' + str(res)
        return res

class GetPosition(Resource):
    global ppl
    def predictPositionByRssi(self, wrm={'abcd':-99}, buildId='COEX',\
            module='SCIKIT', algorithm='GNB'):
        print('Request position at: ' + buildId)
        ppl.load_pipe(module)

        res = ppl.process(wrm, config={'building_id':buildId, 'algorithm':algorithm, 'min_rssi':-999})
        resJson = {
            'x': res[0],
            'y': res[1],
            'z': 0
        }
        return resJson

    def post(self):
        json = request.get_json()
        rssi, bid = json['rssi'], json['bid']
        return jsonify(\
            self.predictPositionByRssi(\
                rssi, bid \
            )\
        )

class CollectRssi(Resource):
    def saveRssiInfo(self, data = \
    {
        "bid":"TEST",
        "x": 2, "y": 2, "z": 2,
        "rssi":[
            {
                "bssid": "39:33:34:f2:dd:cc",
                "dbm": -47
            }
        ]
    }):
        print(data['bid'])
        savePath = 'Data/WRM/RAW/{}.dat'.format(data['bid'])
        with open(savePath, 'a') as f:
            f.write(json.dumps(data) + '\n')
        return 'save data'

    def post(self):
        return self.saveRssiInfo(data=request.get_json())

