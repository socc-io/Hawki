#!venv/bin/python

import sys, json
import time
import importlib
import Predictor.Module.APVOCA.SUPER_APVOCA as apvoca
import glob

def start_train_pipe(building_id, datPath='Data/WRM/RAW/',\
                          train_algorithm='sklearn_gaussianNB'):
    vocaPath = 'Predictor/Module/APVOCA/VOCAS/'
    targetPath = 'Predictor/Module/GNB/bin/'

    print('[START] training with < %s >' % train_algorithm)
    start_time = time.time()
    apvoca.build(building_id, datPath, vocaPath)
    gnb = importlib.import_module('Predictor.Module.GNB.' + train_algorithm)
    gnb.build(building_id, datPath, vocaPath, targetPath)
    end_time = time.time()
    print('[END] training with < %s >' % train_algorithm)
    print('[INFO] spend time : %.2lf' % (end_time - start_time))

if __name__ == '__main__':
    if len(sys.argv) == 2:
        building_id = sys.argv[1]
        start_train_pipe(building_id)
    elif len(sys.argv) == 3:
        building_id = sys.argv[1]
        train_algorithm = sys.argv[2]
        start_train_pipe(building_id, train_algorithm)
    else:
        files = [i.split('/')[-1].split('.')[0] for i in glob.glob('Data/WRM/RAW/*.dat')]
        for file in files:
            start_train_pipe(file)
        # print('please run with argument!')
        # print('> python trainer.py [BUILDING_ID] [algorithm]')
