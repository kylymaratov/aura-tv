## AuraTv - (AndoridTv application)

This is an Android TV app that lets you watch movies, anime, and TV series for free and without restrictions.

<img src="https://i.ibb.co/zHBTP2L/img.png" alt="img" border="0" />

In this image you can see the main page of the application.


### How build the project?
You need to install Android Studio or Kotlin/Java build tools. From here https://developer.android.com/studio


#### Step 1.
Clone this repository to your local repository.
```
git clone https://github.com/kylymaratov/aura-tv.git
```

### Step 2.
Run this command to install dependencies and build the project.
```
./gradlew assembleRelease
```
Or for debug
```
./gradlew assembleDebug
```

### Step 3.
The output file will be compiled at the path
```
app/build/outputs/apk/release/app-release.apk
```
You can download this APK file and install it on your Android TV.

Enjoy! 👐
