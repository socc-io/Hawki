#!/usr/bin/python

import sys, json
import importlib
import Predictor.Module.APVOCA.SUPER_APVOCA as apvoca

def start_train_pipe(building_id, ratio):
    datPath = 'Data/WRM/RAW/' + building_id + ".dat"
    tgTrainPath = 'Data/TestData/' + building_id + ".train.dat"
    tgTestPath = 'Data/TestData/' + building_id + ".test.dat"
    with open(datPath,'r') as datf:
        trainf = open(tgTrainPath, 'w')
        testf = open(tgTestPath, 'w')
        datc = datf.read().split("\n")
        datc.pop()
        stand = int(1/ratio)
        train_count = 0
        test_count = 0
        for idx, row in enumerate(datc):
            if idx % stand == 0: # for test data split
                testf.write(row+"\n")
                test_count += 1
            else: # for train data split
                trainf.write(row+"\n")
                train_count += 1
        trainf.close()
        testf.close()
        print "Origin[", len(datc),"] : " + datPath
        print "Train[", train_count, "] : " + tgTrainPath
        print "Test[", test_count, "] : " + tgTestPath
        return
    print "Fail to split data"
    return
 
if __name__ == '__main__':
    if len(sys.argv) == 3:
        building_id = sys.argv[1]
        ratio = float(sys.argv[2])
        start_train_pipe(building_id, ratio)
    else:
        print('please run with argument!')
        print('> python make_data.py [BUILDING_ID] [RATIO_FOR_TEST]')
