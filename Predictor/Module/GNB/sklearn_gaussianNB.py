import os, sys
import json
import numpy as np
from sklearn import naive_bayes

# to fit learning model
def fit(train_mat, train_lbl, force=False):
    clf = naive_bayes.GaussianNB()
    clf.fit(train_mat, train_lbl)

    return clf

# return prediction result
def predict(clf, test_mat):
    return clf.predict(test_mat)

# (deprecated) make training dataset
def make_train_data():
    min_val = -999
    raw_data = np.genfromtxt('../../Data/WRM/RAW/LS.csv', delimiter=',')
    train_mat = raw_data[1:,3:]
    train_mat[np.isnan(train_mat)] = min_val
    train_mat[np.isinf(train_mat)] = min_val
    train_lbl = raw_data[1:,:3]

    return train_mat, train_lbl

# (deprecated) make test dataset
def make_test_data():
    min_val = -999
    test_data = np.genfromtxt('../../Data/WRM/RAW/TS.csv', delimiter=',')
    test_mat = test_data[1:,3:]
    test_mat[np.isnan(test_mat)] = min_val
    test_mat[np.isinf(test_mat)] = min_val
    test_lbl = test_data[1:,:3]

    return test_mat, test_lbl

# make train dataset
def make_train_data2(inName, vocaName):
    inPath = '../../../Data/WRM/RAW/' + inName
    vocaPath = '../APVOCA/VOCAS/' + vocaName
    min_val = -999

    vocaIdxMap = {}
    with open(vocaPath) as f:
        vocaList = f.read().split(',')
        for idx, val in enumerate(vocaList):
            vocaIdxMap[val] = idx

    res_mat = []
    res_lbl = []

    with open(inPath) as f:
        rawList = f.read().split('\n')
        rawList.pop()
        for line in rawList:
            row = json.loads(line)
            matItem = [min_val for x in range(len(vocaIdxMap))]
            lblItem = []

            # make rssi matrix
            for rssi in row['rssi']:
                idx = vocaIdxMap[rssi['sid']]
                matItem[idx] = rssi['dbm']
            res_mat.append(matItem)

            # make label(x,y,z) array
            lblItem.append(row['x'])
            lblItem.append(row['y'])
            lblItem.append(row['z'])
            res_lbl.append(lblItem)

    return np.array(res_mat), np.array(res_lbl)

if __name__ == '__main__':
    if len(sys.argv) > 1:
        build_id = sys.argv[1]
        datName = build_id + '.dat'
        vocaName = build_id + '.voca'
        train_mat, train_lbl = make_train_data2(datName, vocaName)

        # fitting
        clf_x = fit(train_mat, train_lbl[:,0].ravel())
        clf_y = fit(train_mat, train_lbl[:,1].ravel())
#        clf_z = fit(train_mat, train_lbl[:,2].ravel()) # z has variance 0 issue

        import pickle
        # save pre trained model to pickle
        with open('bin/' + build_id + '_gnb_x_0.pkl', 'wb') as f:
            pickle.dump(clf_x, f)
        with open('bin/' + build_id + '_gnb_y_0.pkl', 'wb') as f:
            pickle.dump(clf_y, f)

