**Project Name** :  Indoor Location Tracking using Wi-Fi

**Project Requirements** :
•	Android Device
•	OS Version > 4.0.3

**Group Members**: 
•	Sai Prudhvi Chode
•	Samara Simha Reddy Yerramada
	
**Motivation** :
	Our goal is to build an application that assists disabled people to navigate in an indoor environment. This application will trigger certain vibration patterns specific to each location in an indoor environment.

**Usage** :
The Application currently has two modes 
	1) Training mode
	2) Query mode

**Training mode** : This mode can be selected by going into Menu Options from the Main Screen. The user is presented with a Map where he can ‘Tap’ anywhere on the screen which should be his current location. Then he will be asked to Enter a location name. The location name has to be one of the following : Bedroom , Kitchen or Hall. When he Enters the location and hits OK. He should then go to menu and select Scan to collect the fingerprints (RSSI) values at that location which will eventually gets updated in the database. The user has to tap different areas of the map and collect fingerprints at those areas. The more fingerprints the much accurate the prediction is going to be! After collecting all the fingerprints the User can Exit from the Training Mode. Now the user will receive a vibration pattern and a Toast message that will display his current location.

**Query mode** : This is the main screen of the Application. When the application is launched fresh(assuming that we already collected the fingerprints). He needs to go to Menu and choose Get Location this will trigger Query Mode and the Application will start collecting Wi-fi Fingerprints of the user and compare them with those that are already saved in the database. After performing computation it will result the X,Y co-ordinates of the user and place the marker on the map and trigger the vibration and display Toast.

**Removing Fingerprints** : User can go to Training Mode and can delete Fingerprints from the database to start from Scratch.

**Vibration Patterns** : We made use of specific vibration patterns so that Disabled person can sense those vibrations. These vibration patterns are demonstrated in the Tutorial Page. Following are the vibration patterns that are currently used in the application
•	Bedroom :  Vibrate for 100ms with 1000ms Interval. 
•	Kitchen : Vibrate for 400ms with 1000ms Interval. 
•	Hall : Vibrate for 1000ms with 1000ms Interval. 
								
**Algorithm Reference research paper**: RSSI-based Euclidean Distance algorithm for indoor positioning adapted for the use in dynamically changing W-LAN