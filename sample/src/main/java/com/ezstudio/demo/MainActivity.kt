package com.ezstudio.demo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.async
import okhttp4k.Http
import okhttp4k.converter.GsonConverter

class MainActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    async_get_btn.setOnClickListener {
      val tag = Http.get<Weather> {
        //        tag = MainActivity::class.java
        url = "http://api.openweathermap.org/data/2.5/weather"

        params {
          "appid" to "xxxxxxxxxxx"
          "q" to "shenzhen"
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

//      Http.cancel(tag)
    }

    sync_get_btn.setOnClickListener {
      async {
        val tag = Http.get<Weather> {
          url = "http://api.openweathermap.org/data/2.5/weather"

          params {
            "appid" to "xxxxxxxxxxx"
            "q" to "shenzhen"
          }
          async = false
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
      }

//      Http.cancel(tag)
    }
  }

  private fun setText(string: String) {
    text_view.text = string
  }
}
