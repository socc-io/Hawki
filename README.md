[한글 (in Korean)](#호크아이-실내-위치-측정-프레임워크)

# Hawki (Indoor Positioning Framework)

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/Hawki.png)


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


---


# 호크아이 (실내 위치 측정 프레임워크)

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/Hawki.png)

### 실내 위치 측정 프레임워크 호크아이
실내 위치 측정 기술은 IOT 시대를 맞이하여 점점 더 그 중요성이 커져가고 있습니다. 호크아이 프레임워크는 와이파이와 같은 신호를 사용하여 실내에서 사용자의 디바이스가 어디에 위치되어있는지 알기위한 전체 시스템을 제공합니다.
호크아이는 실내위치 수집/학습/판단 서버와 클라이언트 송수신모듈 그리고 학습 알고리즘 모두를 제공합니다.

호크아이는 크게 3가지 중요 모듈로 구성되어있습니다.

  1. 수집/학습/판단 서버 : 빌딩 맵정보를 관리하고, 알고리즘과 클라이언트 사이의 중개자 역할을 합니다
  2. 클라이언트 모듈 : WRM등의 신호를 수집하고, 맵상에 추정된 현재 위치를 표시합니다. 현재 안드로이드만 지원합니다.
  3. 알고리즘 : Gaussian Naive Bayesian 알고리즘 등 여러가지 실내위치 추정 알고리즘을 지원합니다.

# 테스트 영상

* Hawki 테스트 영상 -> https://www.youtube.com/watch?v=EifW9AjWF0g&feature=youtu.be

* Hawki 햔징 테스트 영상 ( 코엑스몰 ) -> https://www.youtube.com/watch?v=PaCcq-pzsbY


# 설치 및 실행방법

### 1. 서버 설치

    $ git clone https://github.com/socc-io/Hawki.git

    $ cd Hawki

    $ ./start.py [PORT_NUMBER]
      - ex) ./start.py 4000

### 2. 클라이언트 설치

    Android-Studio 설치 : https://developer.android.com/studio/index.html?hl=ko

    File -> Import Existing Project -> PATH_CLONE_HAWKI/APP/Hawki

### 3. 실내 신호 수집

 ![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/collector1.jpg)

    1) 어플리케이션을 켠후에 Collector 버튼을 눌러주세요

    2) 실내 신호를 수집하고 싶은 빌딩이름을 검색후에 선택해주세요

    3) 현재 위치를 입력하고, 수집 버튼을 눌러주세요

### 4. 수집한 데이터 학습

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/raw_data.PNG)

    1) 3번에서 수집한 빌딩데이터 확인

     $ ./lsbid.sh
       YOUR BUILDING DATAS =================================================
       12665691.dat  17573702.dat  18059921.dat  22251293.dat  27539636.dat

    2) 빌딩학습 시키기

     $ ./trainer.py [BUILDING_ID]
       - ex) ./trainer.py 12665691

### 5. 실내 위치 확인

![alt Hawki](https://github.com/socc-io/Hawki/blob/master/image/finder1.jpg)

    1)어플리케이션을 켠후에 Finder 버튼을 눌러주세요

    2)실내 위치를 확인하고 싶은 빌딩이름을 검색한후에 측정 버튼을 눌러주세요


# 참조

An Unsupervised Indoor Localization Method based on Received Signal Strength RSS Measurements [http://www.merl.com/publications/docs/TR2015-129.pdf]

Unsupervised Indoor Localization No Need to War-Drive [http://engr.uconn.edu/~song/classes/nes/unloc.pdf]

Building a Practical Wifi-Based Indoor Navigation System, Dongsoo Han, Sukhoon Jung, Minkyu Lee and Giwan Yoon, KAIST

Indoor Location Sensing Using Geo-Magnetism, Jaewoo Chung, Matt Donahoe, Chris Schmandt, Ig-Jae Kim, Pedram Razavai, Micaela Wiseman, MIT Media Laboratory 20 Ames St.

Vessel integrated information management system based on Wifi Positioning technology, Hyuk-soon Kwan, Dongsoo Han, Song-Que Park, Won-Hee An, Taehyun Park, Net Co.Ltd.


# 저작권

Copyright (c) 2016 팀 캡틴아메리카노 Captain-Americano

Hyeok Oh [ oh4851@gmail.com ] site : https://github.com/oh4851

Sunho Jung [ tnsgh1992@gmail.com ] site : https://github.com/sunhojeong

Youngje jo [ siosio3103@gmail.com ] site : https://github.com/siosio34

Jinwon Lee  [ jino3871@gmail.com ] site : https://github.com/jino678

SeoHyun Back [ becxer87@gmail.com ] site : https://github.com/becxer

# 라이센스

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

