  [![](https://jitpack.io/v/ShankarKakumani/YoutubeVideoPlayer.svg)](https://jitpack.io/#ShankarKakumani/YoutubeVideoPlayer)

- Edited from https://github.com/PierfrancescoSoffritti/android-youtube-player.git
 
## Demo

![](https://s2.gifyu.com/images/glide_slider.gif)
 
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
        mavenCentral()
        maven {
            url "https://jitpack.io"
        }
    }
}
```

add GlideSlider

```groovy
dependencies {
    
    implementation 'com.github.ShankarKakumani:YoutubeVideoPlayer:1.0.2'
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
YoutubeVideoPlayer.playVideo(this,VideoID);

```        
Replace VideoID with youtube video ID.

 ```java
 
 YoutubeVideoPlayer.playVideo(this,"h2JH0vqDcYc");

```   

