package com.ezstudio.demo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp4k.Http
import okhttp4k.converter.GsonConverter

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    try_btn.setOnClickListener {
      //      async {
      val tag = Http.get<Weather> {
        //        tag = MainActivity::class.java
        url = "http://api.openweathermap.org/data/2.5/weather"

        params {
          "appid" to "8893c155c3c98fcec995f180f3d44072"
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
          Log.e("wyf", e.localizedMessage?:e.javaClass.name, e)
          text_view.text = e.localizedMessage?:e.javaClass.name
        }
//        }
      }

//      Http.cancel(tag)
    }
  }

  private fun setText(string: String) {
    text_view.text = string
  }
}
