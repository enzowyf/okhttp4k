package okhttp4k

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp4k.ssl.DEFAULT_TLS_PROTOCOL
import okhttp4k.ssl.SSLContextBuilder
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by enzowei on 2017/12/15.
 */
@HttpDslMarker
class OkHttpBuilder(private val okHttpClient: OkHttpClient = OkHttpClient()) {
    private val builder by lazy { okHttpClient.newBuilder() }

    /**
     * Sets the default connect timeout for new connections. A value of 0 means no timeout,
     * otherwise values must be between 1 and {@link Integer#MAX_VALUE} when converted to
     * milliseconds.
     */
    fun connectTimeout(timeout: () -> Long) {
        builder.connectTimeout(timeout(), TimeUnit.MILLISECONDS)
    }

    /**
     * Sets the default read timeout for new connections. A value of 0 means no timeout, otherwise
     * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
     */
    fun readTimeout(timeout: () -> Long) {
        builder.readTimeout(timeout(), TimeUnit.MILLISECONDS)
    }

    /**
     * Sets the default write timeout for new connections. A value of 0 means no timeout, otherwise
     * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
     */
    @Suppress("unused")
    fun writeTimeout(timeout: () -> Long) {
        builder.writeTimeout(timeout(), TimeUnit.MILLISECONDS)
    }

    /** Sets the response cache to be used to read and write cached responses. */
    @Suppress("unused")
    fun cache(cache: () -> Cache) {
        builder.cache(cache())
    }

    @Suppress("unused")
    fun networkInterceptor(networkInterceptor: () -> Interceptor) {
        builder.addNetworkInterceptor(networkInterceptor())
    }

    @Suppress("unused")
    fun interceptor(interceptor: () -> Interceptor) {
        builder.addInterceptor(interceptor())
    }

    @Suppress("unused")
    fun sslSocketFactory(
        protocol: String = DEFAULT_TLS_PROTOCOL,
        config: SSLContextBuilder.() -> Unit
    ) {
        val (context, trustManager) = SSLContextBuilder().apply(config).createSSLContext(protocol)
        builder.sslSocketFactory(context.socketFactory, trustManager)
    }

    /**
     * Verify that the host name is an acceptable match with
     * the server's authentication scheme.
     *
     * @param hostname the host name
     * @param session SSLSession used on the connection to host
     * @return true if the host name is acceptable
     */
    @Suppress("unused")
    fun hostnameVerifier(verify: (hostname: String, session: SSLSession) -> Boolean) {
        builder.hostnameVerifier(HostnameVerifier(verify))
    }

    fun build(config: OkHttpBuilder.() -> Unit): OkHttpClient {
        config()
        return builder.build()
    }
}

@DslMarker
annotation class HttpDslMarker

