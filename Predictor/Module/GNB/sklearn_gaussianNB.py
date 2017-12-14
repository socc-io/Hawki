import os, sys
import json
import numpy as np
from sklearn import naive_bayes
from PIL import Image

MAP_CELL_NUM = 200

# to fit learning model
def fit(train_mat, train_lbl, force=False):
    clf = naive_bayes.GaussianNB()
    clf.fit(train_mat, train_lbl)

    return clf

# return prediction result
def predict(clf, test_mat):
    return clf.predict(test_mat)


# get size of image of building
def get_building_image_size(building_id):

    pathname = 'Web/static/map/{}.jpg'.format(building_id)

    try:
        img = Image.open(pathname)
    except:
        print('Failed to load image')
        return None

    return img.size

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

    img_size = get_building_image_size(build_id)
    img_size_unit = (img_size[0] / MAP_CELL_NUM, img_size[1] / MAP_CELL_NUM)

    with open(datPath) as f:
        rawList = f.read().split('\n')
        rawList.pop()
        for line in rawList:
            row = json.loads(line)
            matItem = [min_val for x in range(len(vocaIdxMap))]
            lblItem = []
            

            if 'rssi' not in row:
                rssi_list = row['data']
            else:
                rssi_list = row['rssi']

            # make rssi matrix
            for rssi in rssi_list:
                if rssi['bssid'] in vocaIdxMap.keys():
                    idx = vocaIdxMap[rssi['bssid']]
                    matItem[idx] = rssi['dbm']
            res_mat.append(matItem)

            cell_num = int(row['x'] / img_size_unit[0]) * MAP_CELL_NUM + int(row['y'] / img_size_unit[1])
            res_lbl.append([cell_num])

    return np.array(res_mat), np.array(res_lbl)

def build(build_id, source='../../../Data/WRM/RAW/', voca='../APVOCA/VOCAS/', target='bin/'):
    train_mat, train_lbl = make_train_data(build_id, source, voca, target)

    # fitting
    clf = fit(train_mat, train_lbl.ravel())

    import pickle
    # save pre trained model to pickle
    with open(target + build_id + '_GNB.pkl', 'wb') as f:
        pickle.dump(clf, f)

if __name__ == '__main__':
    if len(sys.argv) > 1:
        build_id = sys.argv[1]
        build(build_id)

