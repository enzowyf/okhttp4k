# Okhttp4k
[![](https://jitpack.io/v/enzowyf/Okhttp4k.svg)](https://jitpack.io/#enzowyf/Okhttp4k)

A simple Kotlin library for http request in Android.

## Features
- [x] Support Http, Https, Http2
- [x] Support basic HTTP GET/POST/PUT/DELETE/HEAD/PATCH in a Kotlin DSL style interface
- [x] Support both asynchronous and blocking requests

## Gradle
    allprojects {
        repositories {
	    	...
	    	maven { url 'https://jitpack.io' }
    	}
	}

	dependencies {
    	compile 'com.github.enzowyf:Okhttp4k:v0.1'
	}

## Quick Start
```
Http.get<Weather> {
        //        tag = MainActivity::class.java
        url = "http://api.openweathermap.org/data/2.5/weather"

        params {
          "appid" to "xxxxxxxxxxx"
          "q" to "London"
        }
//          async = false
        converter = GsonConverter(Weather::class.java)
        observeHandler = Handler(Looper.getMainLooper())
        onResponse { response ->
          response.errorBody?.let { setText(it) }
        }
        onSuccess { weather ->
          text_view.text = weather.name
        }
        onFailure { e ->
          Log.e("wyf", e.localizedMessage ?: e.javaClass.name, e)
          text_view.text = e.localizedMessage ?: e.javaClass.name
        }
      }
```
