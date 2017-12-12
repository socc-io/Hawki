# -*- coding: utf-8 -*-

from flask import Blueprint, request, redirect, render_template, jsonify
from werkzeug import secure_filename
from Web import ALLOWED_EXTENSIONS
from ..config import UPLOAD_FOLDER
from ..db import session
from ..db.mappers.POI import POI, POITag
from PIL import Image

import os, base64

app = Blueprint('base', __name__)

@app.route("/admin")
def admin_pages():
    return render_template("admin.html")

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/mapupload', methods=['POST'])
def upload_file():
	# check if file exists in request
	file = request.files.get('file', None)
	if not file:
		return redirect(request.url)
	# validate secure filename
	if not allowed_file(file.filename):
		return 'Save failed'
	# save filestream
	filename = secure_filename(file.filename)
	path = os.path.join(UPLOAD_FOLDER, filename)
	file.save(path)

	return 'Save success'

@app.route('/building/<int:id>/mapimage', methods=['GET', 'POST'])
def get_post_mapimage(id):
	if request.method == 'GET':
		path = os.path.join(UPLOAD_FOLDER, '{}.jpg'.format(id))
		if os.path.isfile(path):
			img = Image.open(path)
			return jsonify({'success':1 , 'size': img.size})
		else:
			return jsonify({'success':0})
	elif request.method == 'POST':
		body = request.get_json()
		requires = ['image']

		for required in requires:
			if required not in body.keys():
				return jsonify({'success': 0, 'message': '{} not found'.format(required)})

		image = base64.b64decode(body['image'].split(',')[1])

		filename = '{}.jpg'.format(id)
		path = os.path.join(UPLOAD_FOLDER, filename)
		fp = open(path, 'w')
		fp.write(image)
		fp.close()

		return jsonify({'success': 1})

@app.route('/building/<int:id>/poi', methods=['GET', 'POST'])
def get_post_poi(id):
	if request.method == 'GET':
		pois = session.query(POI).filter(POI.building_id == id).all()

		return jsonify({'success': 1, 'pois': [i.serialize for i in pois]})
	elif request.method == 'POST':
		body = request.get_json()
		requires = ['name', 'url', 'x', 'y']

		for required in requires:
			if required not in body.keys():
				return jsonify({'success': 0, 'message': '{} not found'.format(required)})

		name = body['name']
		url = body['url']
		x = int(body['x'])
		y = int(body['y'])

		poi = POI(id, name, url, x, y)
		session.add(poi)
		session.commit()

		return jsonify({'success': 1})

@app.route('/building/<int:id>/poi/<int:poi_id>', methods=['DELETE'])
def delete_poi(id, poi_id):
	poi = session.query(POI).filter(POI.id == poi_id and POI.building_id == id).first()

	if poi is None:
		return jsonify({'success': 0, 'message': 'POI not found'})

	session.delete(poi)
	session.commit()

	return jsonify({'success': 1})

@app.route('/building/<int:id>/poi/all', methods=['DELETE'])
def delete_poi_all(id):
	pois = session.query(POI).filter(POI.building_id == id).all()

	for poi in pois:
		session.delete(poi)
	session.commit()

	return jsonify({'success': 1})

@app.route('/building/<int:id>/poi/<int:poi_id>/tag', methods=['POST', 'DELETE'])
def post_delete_building_tag(id, poi_id):
	json = request.get_json()

	poi = session.query(POI).filter(POI.id == poi_id and POI.building_id == id).first()

	if poi is None:
		return jsonify({'success': 0, 'msg': 'POI not found'})

	tag_name = json.get('tag_name', None)
	if tag_name is None:
		return jsonify({'success': 0, 'msg': 'Failed to get tag_name'})

	tag = session.query(POITag).filter(POITag.name == tag_name).first()

	if request.method == 'POST':
		if tag is None:
			tag = POITag(name=tag_name)
		
		session.add(tag)
		poi.tags.append(tag)

		session.commit()
		
		return jsonify({'success': 1, 'tag_id': tag.id, 'msg': 'Successfully added tag'})
	elif request.method == 'DELETE':
		if tag_name is None:
			return jsonify({'success': 0, 'msg': 'Tag not found'})

		try:
			poi.tags.remove(tag)
		except:
			pass
		session.commit()

		return jsonify({'success': 1, 'msg': 'Successfully removed tag from poi'})

@app.route('/building/<int:id>/poi/<int:poi_id>/tags', methods=['GET'])
def get_building_tags(id, poi_id):
    	
	poi = session.query(POI).filter(POI.id == poi_id and POI.building_id == id).first()

	if poi is None:
		return jsonify({'success': 0, 'msg': 'POI not found'})
	
	tags = [tag.serialize for tag in poi.tags]

	return jsonify({'success': 1, 'tags': tags})
