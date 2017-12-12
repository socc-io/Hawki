import pickle
import numpy as np

# Predictor
class APVOCA:
    def __init__(self):
        #Once Load module (only in start server)
        pass

    def resume(self, config):
        #Always load data (load each process)
        bd_voca_file = 'Predictor/Module/APVOCA/VOCAS/' + config['building_id'] + ".voca"
        try:
            f = open(bd_voca_file, 'rb')
        except:
            print('Failed to find {}'.format(bd_voca_file))

        try:
            self.voca = f.read().split(',')
            self.voca_idx_map = {}
            for idx, v in enumerate(self.voca):
                    self.voca_idx_map[v] = idx
            self.min_val = config['min_rssi']
        except:
            print('Failed to parse bssi data')

    def convert(self, vector):
        try:
            res = [self.min_val for x in range(len(self.voca))]
            for jsonRow in vector:
                bssid = jsonRow['bssid']
                rssi = jsonRow['dbm']
                if bssid in self.voca:
                    res[self.voca_idx_map[bssid]] = rssi
            return np.array([res])
        except:
            print('Failed to convert in MAPPER_APVOCA')
            return None
