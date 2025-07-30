package dev.tiagomarcial.netflix_remake.util

import android.util.Log
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask {

    fun execute(url: String) {
        try {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val requestUrl = URL(url) // abriu URL
                val urlConnection =
                    requestUrl.openConnection() as HttpsURLConnection // abriu a conexão
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) {
                    throw  IOException("Erro na comunicação com o servidor")
                }
                val stream = urlConnection.inputStream
                val jsonAsString = stream.bufferedReader().use { it.readText() }
                Log.i("teste", jsonAsString)
            }
        } catch (e: Exception) {
            Log.e("teste", e.message?:"erro desconhecido", e)
        }
    }
}