import sys, json
import importlib
import Module.APVOCA.SUPER_APVOCA as apvoca

def start_train_pipe(building_id, train_algorithm='sklearn_gaussianNB'):
    datPath = '../Data/WRM/RAW/'
    vocaPath = 'Module/APVOCA/VOCAS/'
    targetPath = 'Module/GNB/bin/'

    apvoca.build(building_id, datPath, vocaPath)
    gnb = importlib.import_module('Module.GNB.' + train_algorithm)
    gnb.build(building_id, datPath, vocaPath, targetPath)

if __name__ == '__main__':
    if len(sys.argv) == 2:
        building_id = sys.argv[1]
        start_train_pipe(building_id)
    elif len(sys.argv) == 3:
        building_id = sys.argv[1]
        train_algorithm = sys.argv[2]
        start_train_pipe(building_id, train_algorithm)
    else:
        print('please run with argument!')
        print('> python trainer.py [BUILDING_ID] [algorithm]')
