#!/usr/bin/python

import os
import json
import trainer as tr
import Predictor.pipeline as pipe

ppl = pipe.Pipeline()

def readAllfiles(path):
    result = {}

    for root, dirs, files in os.walk(path):
        rootpath = os.path.join(os.path.abspath(path), root)

        for filename in files:
            filepath = os.path.join(rootpath, filename)
            seperated = filename.split('.')
            if seperated[0] not in result:
                result[seperated[0]] = {}
            result[seperated[0]][seperated[1]] = seperated[0] + '.' + seperated[1]

    return result

def testTrainedModule(train_data, test_data, pre_trained):
    total = 0
    correct = 0
    testDataPath = 'Data/TestData/'

    ppl.load_pipe('SCIKIT')

    with open(testDataPath + test_data + '.dat') as f:
        rawList = f.read().split('\n')
        rawList.pop()
        for line in rawList:
            row = json.loads(line)
            ans_x = row['x']
            ans_y = row['y']
            wrm = []
            for elem in row['rssi']:
                wrm.append({'bssid':elem['bssid'], 'dbm':elem['dbm']})
            res = ppl.process(wrm, config={'building_id':train_data, 'algorithm':pre_trained, 'min_rssi':-999})
            x_diff = (float(ans_x) - float(res[0]))
            y_diff = (float(ans_y) - float(res[1]))
            diff = (x_diff**2 + y_diff**2)**(0.5)
            if diff < 10:
                correct += 1
            total += 1
        print('[RESULT] module < %s > : %.2lf %% (%d / %d)' % \
              (pre_trained, (float(correct) / float(total) * 100), correct, total))

def startTest(buildings):
    modules = {}
    modules['sklearn_gaussianNB'] = 'GNB'
    modules['sklearn_bayesridge'] = 'RIDGE'

    for building_id in buildings:
        print('[INFO] start test with < %s >' % building_id)
        train_data = buildings[building_id]['train']
        test_data = buildings[building_id]['test']
        for key in modules:
            tr.start_train_pipe(train_data, 'Data/TestData/', key)
            testTrainedModule(train_data, test_data, modules[key])

if __name__ == '__main__':
    result = readAllfiles('Data/TestData/')
    startTest(result)
