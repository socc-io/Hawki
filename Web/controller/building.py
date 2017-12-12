# -*- coding: utf-8 -*-

from Web.service.daumAPI import DaumSearchEngine
from Web import ppl
from flask import jsonify, request, Blueprint

import CONFIG, json

app = Blueprint('__building_route__', __name__)

@app.route('/buildinginfo', methods=['GET'])
def get_building_info():
    building_name = request.args.get('buildName', '김포공항')
    search_engine = DaumSearchEngine()
    res = search_engine.getBuildInfoByName(building_name)
    if not res:
        return u'Failed to request daum API'
    print('requested daum api: ' + repr(res).decode('unicode-escape'))
    return jsonify(res)

@app.route('/getposition', methods=['POST'])
def post_get_position():
    global ppl
    json = request.get_json()
    rssi, bid = json['data'], json['bid']
    ppl.load_pipe('SCIKIT')
    res = ppl.process(rssi, config={'building_id':bid, 'algorithm':'GNB', 'min_rssi':-999})
    res = {'x': res[0], 'y': res[1], 'z': 0}

    print('predict result: {}'.format(repr(res).decode('unicode-escape')))
    
    return jsonify(res)

@app.route('/collectrssi', methods=['POST'])
def post_collect_rssi():
    data = request.get_json()
    print(data['bid'])
    save_path = 'Data/WRM/RAW/{}.dat'.format(data['bid'])
    with open(save_path, 'a') as f:
        f.write(json.dumps(data) + '\n')
    return jsonify({'success': 1, 'msg':'Successfully saved'})

