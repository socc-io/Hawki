import pickle

# Predictor
class GNB:
    def __init__(self):
        #Once Load module (only in start server)
        pass

    def resume(self, config):
        #Always load data (load each process)
        building_id = config['building_id']
        pklXPath = 'Predictor/Module/GNB/bin/' + building_id + '_gnb_x_0.pkl'
        pklYPath = 'Predictor/Module/GNB/bin/' + building_id + '_gnb_y_0.pkl'
        with open(pklXPath, 'rb') as f:
            self.clf_x = pickle.load(f)
        with open(pklYPath, 'rb') as f:
            self.clf_y = pickle.load(f)

    def convert(self, vector):
        #Return ( x,y,z,confidence )
        preds_x = self.clf_x.predict(vector)
        print('x_result_set: ' + preds_x)
        preds_y = self.clf_y.predict(vector)
        print('y_result_set: ' + preds_y)
        return preds_x[0], preds_y[0]
