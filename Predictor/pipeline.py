
# Preprocessor
class APVOCA:
    def __init__():
        #Once Load data (only in start server)
        pass

    def resume():
        #Always load data
        pass
    
    def convert(data):
        #Return custom data or vector
        return data

# Predictor
class SIMPLE:

    def __init__():
        #Once Load module (only in start server)
        pass

    def resume():
        #Always load data (load each process)
        pass

    def convert(vector):
        #Return ( x,y,z,confidence )
        return [10,10,3,90]

    
PPlist = {
    'APTOP25' : APVOCA(25),
    'APTOP15' : APVOCA(15),
    'SVM' : SVM(),
    'BAYES' : BAYES()
}

##### BELOW IS PIPLINE #####

class Pipeline:
    
    def __init__():
        self.pipes = {}

    def load_pipes():
        # Export this to Config file
        self.pipes['FIRST'] = ['APTOP25', 'SVM']
        self.pipes['SECOND'] = ['APTOP15', 'BAYES']
        self.pipes['THIRD'] = ['APTOP25', 'BAYES']
        
    def process(input_data):
        pipes_result = {}
        for pname in self.pipes.keys():
            last_vector = input_data
            pipes_result[pname] = None
            for mname in self.pipes[pname]:
                mod = PPlist[mname]
                mod.resume()
                last_vector = mod.convert(last_vector)
            pipes_result[pname] = last_vector
        return self.postprocess(pipes_result)
            
    def postprocess(pipes_result):
        # Simple Merge
        res_list = []
        for res in pipes_result.keys():
            res_list.append(pipes_result[res])
        res_list.sort()
        return res_list[0]    
