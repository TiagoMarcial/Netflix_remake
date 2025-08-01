package dev.tiagomarcial.netflix_remake.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import dev.tiagomarcial.netflix_remake.model.Category
import dev.tiagomarcial.netflix_remake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask (private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute()
        fun onResult(categories: List<Category>)
        fun onFailure(message: String)
    }

    fun execute(url: String) {
        callback.onPreExecute()
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null

            try {
                val requestUrl = URL(url)
                urlConnection = requestUrl.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor: status $statusCode")
                }

                stream = BufferedInputStream(urlConnection.inputStream)
                val jsonAsString = stream.bufferedReader().use { it.readText() }
                val categories = toCategories(jsonAsString)

                handler.post {
                    callback.onResult(categories)
                }


            } catch (e: Exception) {
                val message = e.message?: "Erro desconhecido"
                Log.e("teste", message ?: "Erro desconhecido", e)
                handler.post {
                    callback.onFailure(message)
                }
            } finally {
                stream?.close()
                urlConnection?.disconnect()
            }
        }
    }

    private fun toCategories(jsonAsString: String): List<Category> {
        val categories = mutableListOf<Category>()
        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")

        for (i in 0 until jsonCategories.length()) {
            val jsonCategory = jsonCategories.getJSONObject(i)
            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")
            val movies = mutableListOf<Movie>()

            for (j in 0 until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl = jsonMovie.getString("cover_url")

                movies.add(Movie(id, coverUrl))
            }

            categories.add(Category(title, movies))
        }

        return categories
    }
}
