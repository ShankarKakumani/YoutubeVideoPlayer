  [![](https://jitpack.io/v/ShankarKakumani/YoutubeVideoPlayer.svg)](https://jitpack.io/#ShankarKakumani/YoutubeVideoPlayer)

- Edited from https://github.com/PierfrancescoSoffritti/android-youtube-player.git
 
## APK
Download apk file here [YoutubeVideoPlayer](https://github.com/ShankarKakumani/SampleAPK/blob/main/YoutubeVideoPlayer.apk)
```
https://github.com/ShankarKakumani/SampleAPK/blob/main/YoutubeVideoPlayer.apk
```

 
## Usage

### Step 1

#### Gradle

add jitpack.io

```groovy
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.1.1"
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://jitpack.io"
        }
    }
}
```

add YoutubeVideoPlayer

```groovy
dependencies {
    
    implementation 'com.github.ShankarKakumani:YoutubeVideoPlayer:1.0.6'
}
```

### Step 2

Add permissions (if necessary) to your `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" /> 

```

### Step 3

Usage:
 
```java
YoutubeVideoPlayer.playVideo(MainActivity.this,VideoID);

```        
Replace VideoID with youtube video ID.
Replace MainActivity.this with your Activity

 ```java
 
 YoutubeVideoPlayer.playVideo(MainActivity.this,"h2JH0vqDcYc");

```   

