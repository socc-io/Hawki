#!venv/bin/python
import sys
from Web import app
if __name__ == '__main__':
    if len(sys.argv) > 1:
        portNo = sys.argv[1]
        portNo = int(portNo)
        app.run(debug=True,port=portNo,host='0.0.0.0')
    else:
        print 'usage: ./start.py [PORT_NUMBER]'
