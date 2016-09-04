
# Predictor
class GNB:

    def __init__(self):
        import pickle
        #Once Load module (only in start server)
        with open('AlgoModel/model/gnb_x_0.pkl', 'rb') as f:
            self.clf_x = pickle.load(f)
        with open('AlgoModel/model/gnb_y_0.pkl', 'rb') as f:
            self.clf_y = pickle.load(f)

    def resume(self):
        #Always load data (load each process)
        pass

    def convert(self, vector):
        #Return ( x,y,z,confidence )
        preds_x = self.clf_x.predict(vector)
        preds_y = self.clf_y.predict(vector)
        print preds_x
        print preds_y
        return preds_x[0], preds_y[0]

PPlist = {
    'GNB' : GNB()
}

##### BELOW IS PIPLINE #####

class Pipeline:

    def __init__(self):
        self.pipes = {}

    def load_pipes(self):
        # Export this to Config file
        self.pipes['GNB'] = ['GNB']

    def process(self, input_data):
        pipes_result = {}
        for pname in self.pipes.keys():
            print pname
            last_vector = input_data
            pipes_result[pname] = None
            for mname in self.pipes[pname]:
                mod = PPlist[mname]
                mod.resume()
                print last_vector
                last_vector = mod.convert(last_vector)
            pipes_result[pname] = last_vector
        return self.postprocess(pipes_result)

    def postprocess(self, pipes_result):
        # Simple Merge
        res_list = []
        for res in pipes_result.keys():
            res_list.append(pipes_result[res])
        res_list.sort()
        print res_list[0]
        return res_list[0]

import numpy as np
min_val = -999
test_data = np.genfromtxt('../Data/WRM/RAW/TS.csv', delimiter=',')
test_mat = test_data[[1],3:]
test_mat[np.isnan(test_mat)] = min_val
test_mat[np.isinf(test_mat)] = min_val
test_lbl = test_data[[1],:3]

ppl = Pipeline()
ppl.load_pipes()
ppl.process(test_mat)
