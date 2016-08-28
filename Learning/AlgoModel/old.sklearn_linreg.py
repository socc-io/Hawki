import numpy as np
#from sklearn.naive_bayes import GaussianNB
from sklearn import linear_model

min_val = -999
raw_data = np.genfromtxt('../../Data/WRM/RAW/LS.csv', delimiter=',')

learn_mat = raw_data[1: ,3: ]
learn_mat[np.isnan(learn_mat)] = min_val
learn_mat[np.isinf(learn_mat)] = min_val
learn_label = raw_data[1: ,0:3]

lin_reg = linear_model.LinearRegression()
lin_reg.fit(learn_mat, learn_label)

test_data = np.genfromtxt('../../Data/WRM/RAW/TS.csv', delimiter=',')
test_mat = test_data[1: ,3: ]
test_mat[np.isnan(test_mat)] = min_val
test_mat[np.isinf(test_mat)] = min_val
test_label = test_data[1: ,0:3]

preds = lin_reg.predict(test_mat)
for i in range(len(preds)):
    print("predict: [%lf, %lf, %lf] -- answer: [%lf, %lf, %lf] => Distnace: %lf" % \
            (preds[i][0], preds[i][1], preds[i][2] \
           , test_label[i][0], test_label[i][1], test_label[i][2], np.linalg.norm(preds[i]-test_label[i])))

