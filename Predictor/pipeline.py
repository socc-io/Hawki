#
# Pipeline managing bridge between prediction module & web rest server
#
# @Author becxer87
# @Email becxer87@gmail.com
#

from Predictor.Module.GNB import COEX_GNB
from Predictor.Module.APVOCA import COEX_APVOCA
import numpy as np

# Prepared Pipeline module list
module_list = {
    'COEX_GNB' : COEX_GNB.GNB(),
    'COEX_APVOCA' : COEX_APVOCA.APVOCA()
}

pipeline_list = {
    'COEX' : ['COEST_APVOCA', 'COEX_GNB'],
    'COEX_SIMPLE' : ['COEX_GNB']
}

# PIPLINE Main Module
class Pipeline:

    def __init__(self):
        self.main_pipe = None
        self.main_name = None
        
    def load_pipe(self, pipe_name):
        global pipeline_list
        self.main_pipe = pipeline_list[pipe_name]
        self.main_name = pipe_name

    def process(self, input_data):
        global module_list
        print "[Pipeline : '", self.main_name, "'] is now processing..."
        last_vector = input_data
        pipe_result = []
        for mname in self.main_pipe:
            mod = module_list[mname]
            mod.resume()
            last_vector = mod.convert(last_vector)
            pipe_result.append(last_vector)
        return pipe_result[-1]

#################### Test code for predicting test #####################

min_val = -999
test_data = np.genfromtxt('Data/WRM/RAW/TS.csv', delimiter=',')

test_lbl = test_data[[1],:3]
test_mat = test_data[[1],3:]
test_mat[np.isnan(test_mat)] = min_val
test_mat[np.isinf(test_mat)] = min_val

ppl = Pipeline()
ppl.load_pipe('COEX_SIMPLE')
res = ppl.process(test_mat)

print 'Test result : ', res

########################################################################
