# -*-coding:utf-8
from flask import Flask, request, json, render_template, url_for, send_from_directory, redirect
from flask_restful import Resource, Api, reqparse
from Predictor.pipeline import Pipeline
from werkzeug import secure_filename
import CONFIG

import requests
import os
import string

app = Flask(__name__)
api = Api(app)

# Init Pipeline
ppl = Pipeline()

def chexc(content):
    exclude = set(string.punctuation)
    content_punc_removed = ''.join(ch for ch in content if ch not in exclude)
    return content_punc_removed

class DaumSearchEngine:

    def getBuildInfoByName(self, name):
        daum_search_query = 'https://apis.daum.net/local/v1/search/keyword.json?apikey='
        daum_app_key = CONFIG.DAUM['DAUM_KEY']
        daum_search_keyword = '&query=' + name
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

class BuildingInfo(Resource):
    # Request location info to DAUM
    def getBuildInfoByName(self, name='김포공항'):  # Request location info to DAUM
        building_search_engine = None
        if CONFIG.COMMON['BUILDING_SEARCH_ENGINE'] == 'DAUM':
            building_search_engine = DaumSearchEngine()
        return building_search_engine.getBuildInfoByName(name)

    def get(self):
        buildName = request.args.get('buildName')
        if buildName == '':
            res = json.dumps(self.getBuildInfoByName())
            return res
        else:
            res = json.dumps(self.getBuildInfoByName(name=buildName))
            return res

class GetPosition(Resource):
    global ppl
    def predictPositionByRssi(self, wrm={'abcd':-99}, buildId='COEX',\
            module='SCIKIT', algorithm='GNB'):
        print('Request position at: ' + buildId)
        ppl.load_pipe(module)

        res = ppl.process(wrm, config={'building_id':buildId, 'algorithm':algorithm, 'min_rssi':-999})
        resJson = {
            'position': {
                'x': res[0],
                'y': res[1],
                'z': "0"
            }
        }
        return resJson

    def post(self):
        jsonObj = request.get_json()
        rssiSet = jsonObj['rssi']
        buildingId = jsonObj['bid']
        return json.dumps(self.predictPositionByRssi(rssiSet, buildingId))

class CollectRssi(Resource):
    def saveRssiInfo(self, data = \
      {"bid":"TEST","x":2,"y":2,"z":2,"rssi":[{"bssid":"39:33:34:f2:dd:cc","dbm":-47}]}):
        bid = data['bid']
        savePath = 'Data/WRM/RAW/' + bid + '.dat'
        with open(savePath, 'a') as f:
            f.write(json.dumps(data) + '\n')
        return 'save data'

    def get(self):
        return 'Unvalid access'

    def post(self):
        jsonObj = request.get_json()
        return self.saveRssiInfo(jsonObj)

# Request Routing
api.add_resource(BuildingInfo, '/buildinginfo')
api.add_resource(GetPosition, '/getposition')
api.add_resource(CollectRssi, '/collectrssi')

# Request for static page
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
app.config['UPLOAD_FOLDER'] = 'static/map'

@app.route("/admin")
def admin_pages():
    return app.send_static_file("admin.html")

@app.route("/static/<string:path>")
def static_pages(path):
    return app.send_static_file(path)

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/mapupload', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        if 'file' not in request.files:
            return redirect(request.url)
        file = request.files['file']
        if file.filename == '':
            return redirect(request.url)
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            path = os.path.join(os.path.dirname(__file__), app.config['UPLOAD_FOLDER'], filename)
            file.save(path)
            return 'Save success'
    return 'Save failed'
