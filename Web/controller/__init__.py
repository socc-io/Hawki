# -*- coding: utf-8 -*-

def register(app, api):
	# register base.py
	from web.controller.building import \
	    BuildingInfo,\
	    GetPosition,\
	    CollectRssi
	api.add_resource(BuildingInfo, '/buildinginfo')
	api.add_resource(GetPosition, '/getposition')
	api.add_resource(CollectRssi, '/collectrssi')

	# register building.py
	from web.controller.base import app as baseCnt
	app.register_blueprint(baseCnt)
