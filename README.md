# Hawki

Hawki is the framework system for indoor positioning service. Indoor positioning technology will use in a variety of ways including IOT, Indoor-navigation. Hawki allows you to find where you are in the building or subway by using your wifi-enabled device such as android, iphone, etc.

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
    
    File -> Import Existing Project -> PATH_CLONE_HAWKI/APP/HawkiApp

### 3. Collecting your indoor data using APP

### 4. Predicting location using APP

# License

The MIT License (MIT)

Copyright (c) 2016 Captain-Americano team

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
