import pickle
from PIL import Image

# Predictor
class GNB:
    def __init__(self):
        #Once Load module (only in start server)
        pass

    def resume(self, config):
        #Always load data (load each process)
        self.build_id = config['building_id']
        algorithm = config['algorithm']
        pklPath = 'Predictor/Module/GNB/bin/'
        pklPath = '{}{}_{}.pkl'.format(pklPath, self.build_id, algorithm)
        with open(pklPath, 'rb') as f:
            self.clf = pickle.load(f)

    def convert(self, vector, verbose=False):
        #Return ( x,y,z,confidence )i
        preds = self.clf.predict(vector)
        if verbose:
            print('result_set: ' + str(preds))

        img = Image.open('Web/static/map/{}.jpg'.format(self.build_id))
        img_size = img.size
        img_size_unit = (img_size[0] / 200, img_size[1] / 200)

        preds = int(preds)
        x = int(preds / 200) * img_size_unit[0] + img_size_unit[0] / 2
        y = int(preds % 200) * img_size_unit[1] + img_size_unit[1] / 2
        
        return x, y

