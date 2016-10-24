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
    'SCIKIT' : ['MAPPER_APVOCA', 'SUPER_GNB']
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

    def process(self, input_data, config, verbose=False):
        global module_list
        if verbose:
            print('[Pipeline : ', self.main_name, '] is now processing...')
        last_vector = input_data
        pipe_result = []
        for mname in self.main_pipe:
            mod = module_list[mname]
            mod.resume(config)
            last_vector = mod.convert(last_vector)
            pipe_result.append(last_vector)
        return pipe_result[-1]

