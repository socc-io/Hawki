# Hawki (Indoor Positioning Framework)

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/logo_Hawki.png)


### Hawki is the framework system for indoor positioning service.
Indoor positioning technology will use in a variety of ways including IOT, Indoor-navigation. Hawki allows you to find where you are in the building or subway by using your wifi-enabled device such as android, iphone, etc.

Simply, Hawki provide whole systems for indoor positioning that include Server-side, Client-side, Predicting model

Hawki system is built on three main components,

  1. Server : Mediating between Predictor and client. Built with Flask (python)
  2. Client Application : Collect wifi radio map or etc, show position on the map. Android >= 6.0, iPhone(Not-Implemented)
  3. Predictors (server) : Predicting user's position in the building. Built with Scikit, Pytrain


# Test video

* Hawki test video -> https://www.youtube.com/watch?v=EifW9AjWF0g&feature=youtu.be

* Hawki field test video ( in coex mall ) -> https://www.youtube.com/watch?v=PaCcq-pzsbY


# Quick start

### 1. Install server

    $ git clone https://github.com/socc-io/Hawki.git

    $ cd Hawki

    $ ./start.py [PORT_NUMBER]
      - ex) ./start.py 4000

### 2. Install Client

    Install Android-Studio : https://developer.android.com/studio/index.html?hl=ko

    File -> Import Existing Project -> PATH_CLONE_HAWKI/APP/Hawki

### 3. Collecting your indoor data using APP

 ![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/collector1.jpg)

    1) After Open application, Click the Collector button

    2) Search building name that you are located on and select

    3) Input your coordinate on map and push collect button

### 4. Training indoor data

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/raw_data.PNG)

    1) After Step 3, You can see your collected building's indoor data

     $ ./lsbid.sh
       YOUR BUILDING DATAS =================================================
       12665691.dat  17573702.dat  18059921.dat  22251293.dat  27539636.dat

    2) Train your building data

     $ ./trainer.py [BUILDING_ID]
       - ex) ./trainer.py 12665691

### 5. Predicting location using APP

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/finder1.jpg)

    1) After Open application, Click the Finder button

    2) Search building name that you are located on and push find button


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

YoungJin Kim [ smliup2244@gmail.com ] site : https://github.com/smliu97

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
