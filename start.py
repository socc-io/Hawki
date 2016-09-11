#!/usr/bin/python
#from Predictor import pipeline
#from Predictor.Module.APVOCA import APlist2Voca
from Web import routes

if __name__ == '__main__':
    routes.app.run(debug=True,port=4000,host='0.0.0.0')
