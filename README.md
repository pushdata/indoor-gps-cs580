**Project Name** : 1-indoorgps-cs580 

**Platform** : Android

**Minimum sdk version** : Android API level 15 

**Target SDK version** : Android  API Level 25 



**GroupMembers**: 

1.SamaraSimhaReddy Yerramada
2. Sai Prudhvi Chode


**Motivation:**

      Through our application we intend to use  wifi signal strength and detect the locations indoor and provide feedback through vibration pattern to assist 
	  disabled(blind) people.   
	

**Application Usage** :

Our application has two phases Training and Run time phases, the application opens in the default mode.

**Training phase** : Inorder to learn  about the locations indoor we provided a training mode, the user can enter into training mode by clicking the top right corner menu item.
Once the user is in training mode by chosing the location on the screen user can register a location name and then scan the wifi strength by clicking scan option on 
top right corner menu item of the application, a pop up toast appears on the screen when recording the signal strengths of access points. We reister the 
two coordinate(x,y) location and average of the signal strengths in database.Similarly multiple finger prints can be saved at different locations. User can exit the training 
mode by clicking the exit training mode menu item.



 
**Real Time** : Once the user exits the  training phase and standing at a particular finger print location our application scans the current signal strength and checks against 
the finger prints recorded during the training phase. We utilized euclidian distance algorithm to figure out the closest location against the fingerprints recorded earlier.
Once the closest location is found  we point the location of the finger print on map, pop up the specific indoor location Name (for example living room or bedroom in indoor)
and provide a pattern to the user through vibration. 


**Vibration feedback to disabled users**: Currently we have hardcoded the patterns to some locations.

**bedroom** :	long[] pattern = new long[]{0, 100, 1000}; 

		The above pattern signifies a gap in interval of 100 ms in vibration pattern of 1000ms

**kitchen** :	pattern = new long[]{0, 400, 1000}; 

		The above pattern signifies a gap in interval of 400 ms in vibration pattern of 1000ms

**hall** :		pattern = new long[]{0, 1000, 1000};

		The above pattern signifies a gap in interval of 1000 ms in vibration pattern of 1000ms
				
 
**Technical Overview**: 
				
		GraphActivity : The base activity in which the we registered the wifi manager and broad cast receiver, This activity is extended by other activities and wifi signal strength results are received in the particular intent currently loaded.
				
		MainActivity : This loads when application is in  default mode and real time phase, it listens to the scan results  of wifi and matches to the closest finger print.

		TrainActivity : This loads when the user is in training mode and listens to the scan results  of wifi  and saves the fingerprints.

		FloorMap:  This is listener activity for touch, When user click the location on map, this activity registers the coordinates of the location.

		Model : Entity object with attributes of coordinates  location and  finger print details.

		Wifi capture : Entity object holding the attributes of signal strength and access points.

		ResultDB : This class  is responsible  interacting with Sqlite Database.
				
		FingerPrintManager : Application manager class to interact with database.
				 
		SplashScreen : To load the  application logo.
					
					
								
**Data Filtration** : In the training phase we obtain the signal strength for each access points for three scans  and maintain a map we take the average of the signal strengths(Signal strength- minimum(-119) dB and store in the database against each location.


											
**Data Retrieval** : In the real time phase we made use of Euclidean distance i.e  square of difference in finger pint strengths to the current signal strength of the location and the least Euclidean distance will give us the  finger print corresponding to the location.


**Algorithm Reference research paper**: RSSI-based Euclidean Distance algorithm for indoor positioning adapted for the use in dynamically changing W-LAN