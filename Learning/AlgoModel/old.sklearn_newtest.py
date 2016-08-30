import numpy as np
from sklearn import linear_model
from sklearn import gaussian_process

min_val = -999
raw_data = np.genfromtxt('../../Data/WRM/RAW/LS.csv', delimiter=',')

learn_mat = raw_data[1: ,3: ]
learn_mat[np.isnan(learn_mat)] = min_val
learn_mat[np.isinf(learn_mat)] = min_val
learn_label = raw_data[1: ,0]

log_reg = linear_model.BayesianRidge(compute_score=False, verbose=True)
#log_reg = gaussian_process.GaussianProcess()
#log_reg = linear_model.LinearRegression()
#log_reg = linear_model.LogisticRegression()
log_reg.fit(learn_mat, learn_label)

test_data = np.genfromtxt('../../Data/WRM/RAW/TS.csv', delimiter=',')
test_mat = test_data[1: ,3: ]
test_mat[np.isnan(test_mat)] = min_val
test_mat[np.isinf(test_mat)] = min_val
test_label = test_data[1: ,0]

preds = log_reg.predict(test_mat)
for i in range(len(preds)):
    print("predict: [%lf] -- answer: [%lf] => Distnace: %lf" % \
            (preds[i] \
           , test_label[i] \
           , np.linalg.norm(preds[i]-test_label[i])))

