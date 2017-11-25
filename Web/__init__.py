# -*-coding:utf-8

from flask import Flask
from flask_restful import Api
from flask_cors import CORS
from Predictor.pipeline import Pipeline

app = Flask(__name__)
CORS(app)
api = Api(app)
# Init Pipeline
ppl = Pipeline()

# Request for static page
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'gif', 'bmp'])

@app.route("/static/<string:path>")
def static_pages(path):
    return app.send_static_file(path)

from Web.controller import register
register(app, api)
