package com.ezstudio.demo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ezstudio.demo.model.WeatherData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.async
import okhttp4k.Http
import okhttp4k.converter.ByteArrayConverter
import okhttp4k.converter.GsonConverter
import okhttp4k.converter.StringConverter

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Http.init {
            connectTimeout = 60_000
            readTimeout = 30_0000
            //for https
//      sslSocketFactory("SSL") {
//        keyManager {
//          open("keyStorePath") withPass "123456" ofType "jks" using "SunX509"
//        }
//        trustManager {
//          open("trustStorePath") withPass "123456" ofType "jks"
//        }
//      }
            //or
            sslSocketFactory("TLS") {
                x509TrustManager { MyTrustManager() }
            }
            hostnameVerifier { hostname, session -> true }
        }

        async_get_btn.setOnClickListener {
            val tag = Http.get<String> {
                tag = MainActivity::class.java
                url = "http://api.openweathermap.org/data/2.5/weather"
                params {
                    "appid" to "xxxxxxxxxxx"
                    "q" to "london"
                }
                converter = StringConverter()
                observeHandler = Handler(Looper.getMainLooper())
                onResponse { response ->
                    response.errorBody?.let { setText(it) }
                }
                onSuccess { str ->
                    text_view.text = str
                }
                onFailure { e, errorMsg ->
                    Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                    text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                }
            }

        }

        get_gson_btn.setOnClickListener {
            val tag = Http.get<WeatherData> {
                url = "http://api.openweathermap.org/data/2.5/weather"
                params {
                    "appid" to "xxxxxxxxxx"
                    "q" to "london"
                }
                converter = GsonConverter(WeatherData::class.java)
                observeHandler = Handler(Looper.getMainLooper())
                onResponse { response ->
                    response.errorBody?.let { setText(it) }
                }
                onSuccess { weather ->
                    text_view.text = weather.toString()
                }
                onFailure { e, errorMsg ->
                    Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                    text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                }
            }

        }

        sync_get_btn.setOnClickListener {
            async {
                val tag = Http.get<String> {
                    url = "http://api.openweathermap.org/data/2.5/weather"

                    params {
                        "appid" to "xxxxxxxxxxx"
                        "q" to "london"
                    }
                    async = false
                    converter = StringConverter()
                    onResponse { response ->
                        runOnUiThread { response.errorBody?.let { setText(it) } }
                    }
                    onSuccess { str ->
                        runOnUiThread { text_view.text = str }
                    }
                    onFailure { e, errorMsg ->
                        runOnUiThread {
                            Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                            text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                        }
                    }
                }
            }

        }

        post_btn.setOnClickListener {
            val tag = Http.post<String> {
                url = "http://httpbin.org/post"
                params {
                    "foo" to "foo"
                    "bar" to "bar"
                }
                converter = StringConverter()
                observeHandler = Handler(Looper.getMainLooper())
                onResponse { response ->
                    response.errorBody?.let { setText(it) }
                }
                onSuccess { result ->
                    text_view.text = result
                }
                onFailure { e, errorMsg ->
                    Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                    text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                }
            }

        }

        post_json_btn.setOnClickListener {
            val tag = Http.post<String> {
                url = "http://httpbin.org/post"
                params {
                    "foo" to "foo"
                    "bar" to "bar"
                }
                contentType = "application/json; charset=utf-8"
                converter = StringConverter()
                observeHandler = Handler(Looper.getMainLooper())
                onResponse { response ->
                    response.errorBody?.let { setText(it) }
                }
                onSuccess { result ->
                    text_view.text = result
                }
                onFailure { e, errorMsg ->
                    Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                    text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                }
            }

        }

        http2_btn.setOnClickListener {
            val tag = Http.get<ByteArray> {
                url = "https://http2.akamai.com/"

                converter = ByteArrayConverter()
                observeHandler = Handler(Looper.getMainLooper())
                onResponse { response ->
                    Log.d("okhttp4k", "protocol: ${response.protocol}")
                    response.protocol.let { setText(it) }
                }
                onSuccess { bytes ->
                    text_view.text = String(bytes)
                }
                onFailure { e, errorMsg ->
                    Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                    text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                }
            }

        }

        https_btn.setOnClickListener {
            val tag = Http.get<String> {
                url = "https://httpbin.org/get"
                converter = StringConverter()
                observeHandler = Handler(Looper.getMainLooper())
                onResponse { response ->
                    response.errorBody?.let { setText(it) }
                }
                onSuccess { str ->
                    text_view.text = str
                }
                onFailure { e, errorMsg ->
                    Log.e("okhttp4k", e?.localizedMessage ?: e?.javaClass?.name, e)
                    text_view.text = e?.localizedMessage ?: e?.javaClass?.name
                }
            }

        }

    }

    private fun setText(string: String) {
        text_view.text = string
    }

    private fun cancel(tag: Any) {
        Http.cancel(tag)
    }
}
