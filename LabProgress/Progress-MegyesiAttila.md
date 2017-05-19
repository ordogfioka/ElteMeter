# Smart Home lab progress - Megyesi Attila #

## 2017 03 14
- Proof of concept android application
- The application can receive location coordinates from gps and interner
- The coordinates are shown on the UI
## 2017 03 21
- Desingning the componenest
- Refactor the service
- Fix the apk install crash
## 2017 03 28
- Looking up for SDK in order to integrate Mi Band 2
- https://github.com/Freeyourgadget/Gadgetbridge
- https://github.com/pangliang/miband-sdk-android
## 2017 04 04
- Refactor service
- Now we can bind and register listeners to the service
- The service long running
- The service and the registered classes must be in the same thread.
- If you want to do a havy action make your own class in this thread and register to the listener and do the heavy action in 
another [Thread](https://developer.android.com/reference/java/lang/Thread.html) or use implement the [Runnable](https://developer.android.com/reference/java/lang/Runnable.html) interface instead.
![Alt text](https://github.com/ordogfioka/ElteMeter/blob/master/LabProgress/Picture/GpsData.png "Gps sensor data")
## 2017 04 18
- we have published the app in the google play store
- https://play.google.com/store/apps/details?id=hu.elte.ordogfioka.eltemeter&hl=en
## 2017 05 02
- trying some sample applications
- trying to make connection to the mi band 2
- integrate mi band 2 sdk to the application (https://github.com/pangliang/miband-sdk-android)
## 2017 05 09
- establish bluetooth connection between the mi band and the phone
- proof of concept: we can get heart rate data from mi band
## 2017 05 09
- merge two branches
- fix broken build
- establish Continuouse integration on Travis-CI
- publish v1.1 of the application on github.
## RESULT OF THE SEMESTER
- you can download the application from https://github.com/ordogfioka/ElteMeter/releases/download/1.1/app-release-unsigned.apk
- the application can show your location information
- the application can connect to bluetooth device
- the applcationn can get heart rate from the device
