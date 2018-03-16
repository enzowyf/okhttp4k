package okhttp4k

import okhttp3.OkHttpClient


/**
 * OkHttpClientManager
 * Created by enzowei on 17/11/14.
 */
object Http {

    private var okHttpClient = OkHttpClient()

    /**
     *  Init okhttp client
     *
     */
    @Suppress("unused")
    fun init(config: OkHttpBuilder.() -> Unit) {
        okHttpClient = with(OkHttpBuilder(okHttpClient)) {
            build(config)
        }
    }

    /**
     *  Http get
     *
     */
    @Suppress("unused")
    fun <T> get(config: Request<T>.() -> Unit): Any =
        with(Request<T>(okHttpClient)) {
            config()
            get()
        }

    /**
     *  Http post
     *
     */
    @Suppress("unused")
    fun <T> post(config: Request<T>.() -> Unit): Any =
        with(Request<T>(okHttpClient)) {
            config()
            post()
        }


    /**
     * Cancel a request by tag
     * @param tag tag
     */
    @Suppress("unused")
    fun cancel(tag: Any) =
        with(okHttpClient.dispatcher()) {
            (runningCalls() + queuedCalls())
                .filter { tag == it.request().tag() }
                .forEach { it.cancel() }
        }
}





