import pickle

# Predictor
class GNB:
    def __init__(self):
        #Once Load module (only in start server)
        with open('Predictor/Module/GNB/bin/gnb_x_0.pkl', 'rb') as f:
            self.clf_x = pickle.load(f)
        with open('Predictor/Module/GNB/bin/gnb_y_0.pkl', 'rb') as f:
            self.clf_y = pickle.load(f)

    def resume(self, config):
        #Always load data (load each process)
        pass

    def convert(self, vector):
        #Return ( x,y,z,confidence )
        preds_x = self.clf_x.predict(vector)
        preds_y = self.clf_y.predict(vector)
        return preds_x[0], preds_y[0]
