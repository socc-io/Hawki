# -*- coding: utf-8 -*-

def register(app, api):
	# register base.py
        from .building import app as building_cnt
        from .base     import app as base_cnt

        app.register_blueprint(building_cnt)
        app.register_blueprint(base_cnt)
        
