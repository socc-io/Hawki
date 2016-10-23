import os, sys
import json
import numpy as np
from sklearn import linear_model

# to fit learning model
def fit(train_mat, train_lbl, force=False):
    clf = linear_model.BayesianRidge()
    clf.fit(train_mat, train_lbl)

    return clf

# return prediction result
def predict(clf, test_mat):
    return clf.predict(test_mat)

# make train dataset
def make_train_data(build_id, source, voca, target):
    datPath = source + build_id + '.dat'
    vocaPath = voca + build_id + '.voca'
    min_val = -999

    vocaIdxMap = {}
    with open(vocaPath) as f:
        vocaList = f.read().split(',')
        for idx, val in enumerate(vocaList):
            vocaIdxMap[val] = idx

    res_mat = []
    res_lbl = []

    with open(datPath) as f:
        rawList = f.read().split('\n')
        rawList.pop()
        for line in rawList:
            row = json.loads(line)
            matItem = [min_val for x in range(len(vocaIdxMap))]
            lblItem = []

            # make rssi matrix
            for rssi in row['rssi']:
                if rssi['bssid'] in vocaIdxMap.keys():
                    idx = vocaIdxMap[rssi['bssid']]
                    matItem[idx] = rssi['dbm']
            res_mat.append(matItem)

            # make label(x,y,z) array
            lblItem.append(row['x'])
            lblItem.append(row['y'])
            lblItem.append(row['z'])
            res_lbl.append(lblItem)

    return np.array(res_mat).astype(np.float), np.array(res_lbl).astype(np.float)

def build(build_id, source='../../../Data/WRM/RAW/', voca='../APVOCA/VOCAS/', target='bin/'):
    train_mat, train_lbl = make_train_data(build_id, source, voca, target)

    # fitting
    clf_x = fit(train_mat, train_lbl[:,0].ravel())
    clf_y = fit(train_mat, train_lbl[:,1].ravel())
    #clf_z = fit(train_mat, train_lbl[:,2].ravel())

    import pickle
    # save pre trained model to pickle
    with open(target + build_id + '_RIDGE_x_0.pkl', 'wb') as f:
        pickle.dump(clf_x, f)
    with open(target + build_id + '_RIDGE_y_0.pkl', 'wb') as f:
        pickle.dump(clf_y, f)

if __name__ == '__main__':
    if len(sys.argv) > 1:
        build_id = sys.argv[1]
        build(build_id)

