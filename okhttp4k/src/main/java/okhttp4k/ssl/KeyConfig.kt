package okhttp4k.ssl

import java.io.InputStream

/**
 * Created by enzowei on 2017/12/16.
 */
@SSLDslMarker
class KeyConfig(val inputStream: InputStream) {
    var algorithm: String? = null
    var password: CharArray? = null
    var fileType: String = "JKS"

    @Suppress("unused")
    infix fun withPass(pass: String) = apply {
        password = pass.toCharArray()
    }

    @Suppress("unused")
    infix fun ofType(type: String) = apply {
        fileType = type
    }

    @Suppress("unused")
    infix fun using(algorithm: String) = apply {
        this.algorithm = algorithm
    }

}


