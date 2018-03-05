package okhttp4k.ssl

import okhttp4k.HttpDslMarker
import java.io.FileInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * Created by enzowei on 2017/12/16.
 */
const val DEFAULT_TLS_PROTOCOL = "TLSv1.2"

@SSLDslMarker
@HttpDslMarker
class SSLContextBuilder {

    private var trustManagers: Array<TrustManager>? = null
    private var keyManagers: Array<KeyManager>? = null

    internal fun createSSLContext(protocol: String = DEFAULT_TLS_PROTOCOL): Pair<SSLContext, X509TrustManager> {
        if (protocol.isEmpty()) {
            throw IllegalArgumentException("At least one protocol must be provided.")
        }
        val trustManager = chooseTrustManager(trustManagers)
        val context = SSLContext.getInstance(protocol).apply {
            init(keyManagers, arrayOf(trustManager), SecureRandom())
        }
        return context to trustManager
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>?): X509TrustManager =
        when {
            trustManagers == null || trustManagers.isEmpty() -> UnsafeTrustManager()
            else -> trustManagers.filterIsInstance<X509TrustManager>().firstOrNull()
                    ?: UnsafeTrustManager()
        }

    @Suppress("unused")
    fun open(name: String) = KeyConfig(FileInputStream(name))

    @Suppress("unused")
    fun open(inputStream: InputStream) = KeyConfig(inputStream)

    @Suppress("unused")
    fun trustManager(store: () -> KeyConfig) {
        trustManagers = trustManagerFactory(store()).trustManagers
    }

    @Suppress("unused")
    fun x509TrustManager(trustManager: () -> X509TrustManager) {
        trustManagers = arrayOf(trustManager())
    }

    @Suppress("unused")
    fun trustManager(trustManagers: Array<TrustManager>) {
        this.trustManagers = trustManagers
    }

    @Suppress("unused")
    fun keyManager(store: () -> KeyConfig) {
        keyManagers = keyManagerFactory(store()).keyManagers
    }

    @Suppress("unused")
    fun keyManager(keyManagers: Array<KeyManager>) {
        this.keyManagers = keyManagers
    }

    private fun trustManagerFactory(store: KeyConfig): TrustManagerFactory = with(store) {
        val algorithm = algorithm ?: TrustManagerFactory.getDefaultAlgorithm()
        val key = this@SSLContextBuilder.loadKeyStore(this)
        TrustManagerFactory.getInstance(algorithm).apply {
            init(key)
        }
    }

    private fun keyManagerFactory(store: KeyConfig): KeyManagerFactory = with(store) {
        val algorithm = algorithm ?: KeyManagerFactory.getDefaultAlgorithm()
        val key = this@SSLContextBuilder.loadKeyStore(this)
        KeyManagerFactory.getInstance(algorithm).apply {
            init(key, password)
        }
    }


    private fun loadKeyStore(store: KeyConfig) = KeyStore.getInstance(store.fileType).apply {
        load(store.inputStream, store.password)
    }
}

@DslMarker
annotation class SSLDslMarker

class UnsafeTrustManager : X509TrustManager {

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}