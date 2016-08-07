                   COEX-WRM (Wi-Fi Radio Map Fingerprint Dataset, B1-floor)
                            
                            Version beta-1

ABOUT

  The COEX-WRM open dataset is collected for Wi-Fi fingerprint-based positioning
  algorithms including Bayesian probability, SVM, k-means, and so on. 
  at COEX (A huge shopping mall and convention center in Seoul, Korea)
  Conventionally many others call the WRM sa the radio map and it is essential 
  to provide a Wi-Fi-based indoor positioning service.

  And about the data collection,

  We divided the area into a grid by the map and road.
  We used Galaxy-S device (brand Samsung) as a receive client and developed an application
  which is a data collect planing tool to easy to draw grid point on the map. 
  This tool can adjust the meter interval so the meter is the unit of x y coordinates. 
  So the unit of data is the meter. If you draw the line on the map, the tool add point according to interval setting. 

  For example, (if we set the interval 3 meters)

  (start point of line) |——--|—--—|—--—|—--—|  (end point of line)
  			            | 3m | 3m | 3m | 3m |

  The COEX area size is about 630m x 300m. Below attachment image is example of collect points 
  and shows the COEX B1 shopping mall. 

DATA FORM

  The data (radio map) consists of RSSIs (Received Signal Strength Indicators) 
  collected point by point at B1 floor of COEX. This radio map forms a huge matrix 
  in which rows represent collected location information and RSSIs, and columns represent 
  all BSSIDs of existing access points on the floor.

  For example: 

  	* x,y - geometric coordinates
  	* AP(x) - RSSI values

   x (coord.)|  y (coord.)|  z (floor)| AP1  | AP2  | AP3  |  ....
  ---------------------------------------------------
  10.0       |   10.0     |   -1      | -60  |  NaN |  -70 |  ....
  13.0       |   10.0     |   -1      | -70  | -60  |  -65 |  ....

FILES

  * coex_b1_LS.xlsx - Leaning dataset.
  * coex_b1_TS.xlsx - Test dataset.

CHANGES
  beta-1     Initial public release.