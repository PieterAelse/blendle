# Blendle by Pieter Otten
Android app for Blendle (https://blendle.com) (job application assignment)

# Features 
* List of popular articles with endless scrolling
* Pull to refresh to get the newest articles
* Switch to landscape for horizontal browsing
* Daydream viewing of popular articles (browses automatically)
* A Blendle themed user interface, supporting back to older Android devices

### Bonus:
* Check at startup if Daydream is enabled and if the Blendle Daydream is selected. If not, instruct the user to enable this.
* Click on an article to full its "full" contents and maybe buy it
* When viewing an article it`s possible to share it using P2P NFC! (note: both devices need to have NFC enabled and this Blendle app installed)
*(Wait a few seconds when viewing an article to get a pop-up about NFC, if your device has NFC.)*

# Screenshots
From left to right:

* Browsing articles
* Reading an article
* Daydream of articles
* Article sharing hint
* Browsing articles in landscape

![Screenshots of Blendle by Pieter Otten](https://raw.githubusercontent.com/PieterAelse/blendle/master/Screenshots/Blendle_PieterOtten_Screenshots.png "Screenshots of Blendle by Pieter Otten")

# Demo movie
[![Demo movie, click to view!](https://raw.githubusercontent.com/PieterAelse/blendle/master/Screenshots/screenshot_video.png)](http://youtu.be/7T6BsbV8ee4)

# Downloading the APK 
[Click here to download the latest APK!](https://raw.githubusercontent.com/PieterAelse/blendle/master/APK/Blendle_by_PieterOtten.apk "Blendle by PieterOtten")

# Building the app
You can build the app using Gradle and the `gradlew.bat` file in this repo. Just open a command window and run:

`gradlew aR` *(which is short for `gradlew assembleRelease`)*
 
The configured gradle build file will take care of all the rest.

You can find the freshly build and signed .APK in  `..\mobile\build\outputs\apk` named `mobile-release.apk`