# -*- coding: utf-8 -*-

from flask import Blueprint, request, redirect, render_template, jsonify
from werkzeug import secure_filename
from Web import ALLOWED_EXTENSIONS
from ..config import UPLOAD_FOLDER

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

@app.route('/poi', methods=['POST'])
def post_poi():
	body = request.get_json()
	requires = ['image', 'id']

	for required in requires:
		if required not in body.keys():
			return jsonify({'success': 0, 'message': '{} not found'.format(required)})

	image = base64.b64decode(body['image'].split(',')[1])
	id = int(body['id'])

	filename = '{}.jpg'.format(id)
	path = os.path.join(UPLOAD_FOLDER, filename)
	fp = open(path, 'w')
	fp.write(image)
	fp.close()

	print image

	return jsonify({'success': 1})
	