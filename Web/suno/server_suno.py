from flask import Flask, request,json

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'hello world!'

@app.route('/test', methods=['GET','POST'])
def test():
    if request.method=='POST':
        if request.headers['Content-Type'] == 'application/json; charset=utf-8':
            print "json data!!"
            return json.dumps(request.json)
        else:
            print "data error"
            return "check data!"

    elif request.method == 'GET':
        print "GET method!"
        return 'GET method!'
    else:
        print "method error!"
        'method error!'

@app.route('/test/<testId>')
def api_test(testId):
    return 'test/' + testId

# GET /hello?name=suno
# Hello suno
@app.route('/hello')
def api_hello():
    if 'name' in request.args:
        return 'Hello ' + request.args['name']
    else:
        return 'Hello!'


if __name__ == '__main__':
    app.run(debug=True,port=4000,host='0.0.0.0')
