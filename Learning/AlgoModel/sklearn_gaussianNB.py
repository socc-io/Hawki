import os, sys
import numpy as np
from sklearn import naive_bayes

def fit(train_mat, train_lbl, force=False):
    clf = naive_bayes.GaussianNB()
    clf.fit(train_mat, train_lbl)

    return clf

def predict(clf, test_mat):
    return clf.predict(test_mat)

def make_train_data():
    min_val = -999
    raw_data = np.genfromtxt('../../Data/WRM/RAW/LS.csv', delimiter=',')
    train_mat = raw_data[1:,3:]
    train_mat[np.isnan(train_mat)] = min_val
    train_mat[np.isinf(train_mat)] = min_val
    train_lbl = raw_data[1:,:3]

    return train_mat, train_lbl

def make_test_data():
    min_val = -999
    test_data = np.genfromtxt('../../Data/WRM/RAW/TS.csv', delimiter=',')
    test_mat = test_data[1:,3:]
    test_mat[np.isnan(test_mat)] = min_val
    test_mat[np.isinf(test_mat)] = min_val
    test_lbl = test_data[1:,:3]

    return test_mat, test_lbl

if __name__ == '__main__':
    # test data load
    train_mat, train_lbl = make_train_data()
    test_mat, test_lbl = make_test_data()

    # label x,y to mapped area (each 3m)
    train_lbl[:, :2] = (train_lbl[:, :2] / 3).astype('int')
    test_lbl[:, :2] = (test_lbl[:, :2] / 3).astype('int')

    # fitting
    clf_x = fit(train_mat, train_lbl[:,0].ravel())
    clf_y = fit(train_mat, train_lbl[:,1].ravel())
#    clf_z = fit(train_mat, train_lbl[:,2].ravel()) # z has variance 0 issue

    # predict
    preds_x = predict(clf_x, test_mat)
    preds_y = predict(clf_y, test_mat)
#    preds_z = predict(clf_z, test_mat) # z has variance 0 issue

    # show result
    distances_x = [(abs(preds_x[i] - test_lbl[:,0][i])) for i in range(len(test_lbl))]
    print("error distance(x): %lf" % (np.mean(distances_x)))

    distances_y = [(abs(preds_y[i] - test_lbl[:,1][i])) for i in range(len(test_lbl))]
    print("error distance(y): %lf" % (np.mean(distances_y)))

#    distances_z = [(abs(preds_z[i] - test_lbl[:,2][i])) for i in range(len(test_lbl))]
#    print("error distance(z): %lf" % (np.mean(distances_z)))
