package okhttp4k

import okhttp3.OkHttpClient


/**
 * OkHttpClientManager
 * Created by enzowei on 17/11/14.
 */
object Http {

  private val okHttpClient by lazy { OkHttpClient() }

  fun <T> get(build: Request<T>.() -> Unit): Any = with(Request<T>(okHttpClient)) {
    build()
    return get()
  }

  fun <T> post(build: Request<T>.() -> Unit): Any = with(Request<T>(okHttpClient)) {
    build()
    return post()
  }


  /**
   * do cacel by tag
   * @param tag tag
   */
  fun cancel(tag: Any) = okHttpClient.dispatcher().let {
    (it.runningCalls() + it.queuedCalls())
        .filter { tag == it.request().tag() }
        .forEach { it.cancel() }
  }

}



