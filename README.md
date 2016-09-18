# Hawki (Indoor Positioning Framework)

![alt Hawki](https://github.com/socc-io/Hawki/raw/master/Hawki.png)

### Hawki is the framework system for indoor positioning service. 
Indoor positioning technology will use in a variety of ways including IOT, Indoor-navigation. Hawki allows you to find where you are in the building or subway by using your wifi-enabled device such as android, iphone, etc.

Simply, Hawki provide whole systems for indoor positioning that include Server-side, Client-side, Predicting model

Hawki system is built on three main components,

  1. Server : Mediating between Predictor and client. Built with Flask (python)
  2. Client Application : Collect wifi rssi map, show position on the map. Android >= 6.0, iPhone(Not-Implemented) 
  3. Predictors (server) : Predicting user's position in the building. Built with Scikit, Pytrain

# Quick start

### 1. Install server

    $ git clone https://github.com/socc-io/Hawki.git

    $ cd Hawki

    $ ./start.py --port 4000

### 2. Install Client

    For Developers,

    Install Anroid-Studio : https://developer.android.com/studio/index.html?hl=ko

    File -> Import Existing Project -> PATH_CLONE_HAWKI/APP/Hawki

### 3. Collecting your indoor data using APP

    ****** Application Capture Should be here!!!!!!!!!!


### 4. Training indoor data

    1) After Step 3, You can show your collected indoor data in below path
     - PROJECT_HOME/Data/WRM/RAW/[buildingName].dat
    2) Make bssid vocabulary mapping file (.voca)
     $ cd PROJECT_HOME/Predictor/Module/APVOCA/
     $ python testBuild_APVOCA.py
     - You can see your vocabulary file in below path
     - PROJECT_HOME/Predictor/Module/APVOCA/VOCAS/[buildingName].voca
    3) Training Indoor data and make pickle file (.pkl)
     $ cd PROJECT_HOME/Predictor/Module/GNB/
     $ python sklearn_gaussianNB.py [.dat file] [.voca file] [outfilename]
       - e.g. python sklearn_gaussianNB.py ex.dat ex.voca ex
     - You can see your pre-trained file in below path
     - PROJECT_HOME/Predictor/Module/GNB/bin/[outfilename]_gnb_[x|y]_0.pkl
    ******  NEED TO EXPLAIN !!!!!!

### 5. Predicting location using APP

    ****** Application Capture Should be here!!!!!!!!!!


# References

An Unsupervised Indoor Localization Method based on Received Signal Strength RSS Measurements [http://www.merl.com/publications/docs/TR2015-129.pdf]

Unsupervised Indoor Localization No Need to War-Drive [http://engr.uconn.edu/~song/classes/nes/unloc.pdf]

글로벌 실내 위치인식 및 실내,외 통합 내비게이션 시스템, 한동수, 정석훈, 한국과학기술원 전산학과, 한국과학기술원 정보통신공학과

Building a Practical Wifi-Based Indoor Navigation System, Dongsoo Han, Sukhoon Jung, Minkyu Lee and Giwan Yoon, KAIST

Indoor Location Sensing Using Geo-Magnetism, Jaewoo Chung, Matt Donahoe, Chris Schmandt, Ig-Jae Kim, Pedram Razavai, Micaela Wiseman, MIT Media Laboratory 20 Ames St.

Vessel integrated information management system based on Wifi Positioning technology, Hyuk-soon Kwan, Dongsoo Han, Song-Que Park, Won-Hee An, Taehyun Park, Net Co.Ltd.


# Copyright

Copyright (c) 2016 Captain-Americano

Hyeok Oh [ oh4851@gmail.com ] site : https://github.com/oh4851

Sunho Jung [  ] site : 

Youngjae Cho [  ] site : 

Jinwon Lee  [  ] site : 

SeoHyun Back [ becxer87@gmail.com ] site : https://github.com/becxer

# License

The MIT License (MIT)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

