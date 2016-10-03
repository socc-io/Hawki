# Hawki (Indoor Positioning Framework)

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/Hawki.png)

### Hawki is the framework system for indoor positioning service. 
Indoor positioning technology will use in a variety of ways including IOT, Indoor-navigation. Hawki allows you to find where you are in the building or subway by using your wifi-enabled device such as android, iphone, etc.

Simply, Hawki provide whole systems for indoor positioning that include Server-side, Client-side, Predicting model

Hawki system is built on three main components,

  1. Server : Mediating between Predictor and client. Built with Flask (python)
  2. Client Application : Collect wifi radio map, show position on the map. Android >= 6.0, iPhone(Not-Implemented) 
  3. Predictors (server) : Predicting user's position in the building. Built with Scikit, Pytrain


* Hawki test video -> https://www.youtube.com/watch?v=EifW9AjWF0g&feature=youtu.be 

* Hawki field test video ( in coex mall ) -> https://www.youtube.com/watch?v=PaCcq-pzsbY

# Quick start

### 1. Install server

    $ git clone https://github.com/socc-io/Hawki.git

    $ cd Hawki

    $ ./start.py [PORT_NUMBER]

      - ex) ./start.py 4000

### 2. Install Client

    For Developers,

    Install Android-Studio : https://developer.android.com/studio/index.html?hl=ko

    File -> Import Existing Project -> PATH_CLONE_HAWKI/APP/Hawki

### 3. Collecting your indoor data using APP
 ![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/collector1.jpg)
 
    1)After Open application, Click the Collector button
    
    2)Input building name that you are located on and next click search button
    
    3)Select building name from searched lists
    
    4)At intervals of one meter, Input x, y, z(repeat 10 times) in a single place and click collection button
    If you do, a toast message that shows input values appears on the screen
    - x means x-axis in building.
    - y means y-axis in building.
    - z means level of floor
    

### 4. Training indoor data
![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/raw_data.PNG)

    1) After Step 3, You can see your collected indoor data in below path

     - PROJECT_HOME/Data/WRM/RAW/[BUILDING_ID].dat

    2) Make bssid vocabulary mapping file (.voca)

     $ cd PROJECT_HOME/Predictor/Module/APVOCA/

     $ python SUPER_APVOCA.py [BUILDING_ID]

       - ex) python SUPER_APVOCA.py testbuilding

     - You can see your vocabulary file in below path

     - PROJECT_HOME/Predictor/Module/APVOCA/VOCAS/[BUILDING_ID].voca

    3) Training Indoor data and make pickle file (.pkl)

     $ cd PROJECT_HOME/Predictor/Module/GNB/

     $ python [TRAINING_MODULE_NAME].py [BUILDING_ID]

       - ex) python sklearn_gaussianNB.py testbuilding

     - You can see your pre-trained file in below path

     - PROJECT_HOME/Predictor/Module/GNB/bin/[BUILDING_ID]_gnb_[x|y]_0.pkl

### 5. Predicting location using APP

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/finder1.jpg)
    
    1)After Open application, Click the Finder button
    
    2)Input building name that you are located on and next click search button
    
    3)Select building name from searched lists
    
    4)And if you click find button, a toast message confirms a location that you are on 

# References

An Unsupervised Indoor Localization Method based on Received Signal Strength RSS Measurements [http://www.merl.com/publications/docs/TR2015-129.pdf]

Unsupervised Indoor Localization No Need to War-Drive [http://engr.uconn.edu/~song/classes/nes/unloc.pdf]

Building a Practical Wifi-Based Indoor Navigation System, Dongsoo Han, Sukhoon Jung, Minkyu Lee and Giwan Yoon, KAIST

Indoor Location Sensing Using Geo-Magnetism, Jaewoo Chung, Matt Donahoe, Chris Schmandt, Ig-Jae Kim, Pedram Razavai, Micaela Wiseman, MIT Media Laboratory 20 Ames St.

Vessel integrated information management system based on Wifi Positioning technology, Hyuk-soon Kwan, Dongsoo Han, Song-Que Park, Won-Hee An, Taehyun Park, Net Co.Ltd.


# Copyright

Copyright (c) 2016 Captain-Americano

Hyeok Oh [ oh4851@gmail.com ] site : https://github.com/oh4851

Sunho Jung [ tnsgh1992@gmail.com ] site : https://github.com/sunhojeong 

Youngje jo [ siosio3103@gmail.com ] site : https://github.com/siosio34

Jinwon Lee  [ jino3871@gmail.com ] site : https://github.com/jino678

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

