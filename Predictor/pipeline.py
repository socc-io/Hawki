#
# Pipeline managing bridge between prediction module & web rest server
#
# @Author becxer87
# @Email becxer87@gmail.com
#

from Predictor.Module.GNB import SUPER_GNB
from Predictor.Module.APVOCA import MAPPER_APVOCA
import numpy as np

# Prepared Pipeline module list
module_list = {
    'MAPPER_APVOCA' : MAPPER_APVOCA.APVOCA(),
    'SUPER_GNB' : SUPER_GNB.GNB()
}

pipeline_list = {
    'GNB' : ['MAPPER_APVOCA', 'SUPER_GNB']
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

    def process(self, input_data, config):
        global module_list
        print "[Pipeline : '", self.main_name, "'] is now processing..."
        last_vector = input_data
        pipe_result = []
        for mname in self.main_pipe:
            mod = module_list[mname]
            mod.resume(config)
            last_vector = mod.convert(last_vector)
            pipe_result.append(last_vector)
        return pipe_result[-1]

'''
#################### Test code for predicting test #####################
test_data = np.genfromtxt('Data/WRM/RAW/TS.csv', delimiter=',')
test_rssi = open('Data/WRM/RAW/LS.csv').read().split('\n')[0].split(',')[3:]
test_lbl = test_data[[1],:3]
test_mat = test_data[[1],3:]

test_wrm = {}
for idx, v in enumerate(test_mat[0]):
    if not np.isnan(v) and not np.isinf(v) :
        test_wrm[test_rssi[idx]] = v


print test_wrm
ppl = Pipeline()
ppl.load_pipe('COEX')
res = ppl.process(test_wrm, config = {"building_id" : 'COEX', "min_rssi" : -999})

print 'Test result : ', res
print 'Original Answer : ', (test_lbl[0][0], test_lbl[0][1])

########################################################################
'''

