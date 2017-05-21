# -*-coding:utf-8

from flask import Flask
from flask_restful import Api
from Predictor.pipeline import Pipeline

app = Flask(__name__)
api = Api(app)
# Init Pipeline
ppl = Pipeline()

# Request for static page
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
app.config['UPLOAD_FOLDER'] = 'static/map'

@app.route("/static/<string:path>")
def static_pages(path):
    return app.send_static_file(path)

from web.controller import register
register(app, api)
