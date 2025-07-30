package dev.tiagomarcial.netflix_remake.util

import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask {

    fun execute(url: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val requestUrl = URL(url) // abriu URL
            val urlConnection = requestUrl.openConnection() as HttpsURLConnection // abriu a conexão
            urlConnection.readTimeout = 2000
            urlConnection.connectTimeout = 2000

            val statusCode: Int = urlConnection.responseCode

        }
    }
}