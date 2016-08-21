import numpy as np
#from sklearn.naive_bayes import GaussianNB
from sklearn import linear_model

raw_data = np.genfromtxt('../../Data/WRM/RAW/LS.csv', delimiter=',')
learn_mat = raw_data[ : ,3: ]
learn_mat[np.isnan(learn_mat)] = 0
learn_mat[np.isinf(learn_mat)] = 0
learn_label = raw_data[ : ,0:3]
learn_label[np.isnan(learn_label)] = 0
learn_label[np.isinf(learn_label)] = 0

#gaussian_nb = GaussianNB()
lin_reg = linear_model.LinearRegression()
lin_reg.fit(learn_mat, learn_label)

test_data = np.genfromtxt('../../Data/WRM/RAW/TS.csv', delimiter=',')
test_mat = test_data[ : ,3: ]
test_mat[np.isnan(test_mat)] = 0
test_mat[np.isinf(test_mat)] = 0
test_label = test_data[ : ,0:3]
test_label[np.isnan(test_label)] = 0
test_label[np.isinf(test_label)] = 0

preds = lin_reg.predict(test_mat)
for i in range(len(preds)):
    print("predict: [%lf, %lf, %lf] -- answer: [%lf, %lf, %lf] => Distnace: %lf" % \
            (preds[i][0], preds[i][1], preds[i][2] \
           , test_label[i][0], test_label[i][1], test_label[i][2], np.linalg.norm(preds[i]-test_label[i])))
