package dev.tiagomarcial.netflix_remake.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import dev.tiagomarcial.netflix_remake.model.Category
import dev.tiagomarcial.netflix_remake.model.Movie
import dev.tiagomarcial.netflix_remake.model.MovieDetail
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MovieTask (private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute()
        fun onResult(movieDetail: MovieDetail)
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

                if (statusCode == 400) {
                    stream = urlConnection.errorStream
                    val buffer = BufferedInputStream(stream)
                    val jsonString = buffer.bufferedReader().use { it.readText() }
                    val json = JSONObject(jsonString)
                    val message = json.getString("message")
                    throw IOException(message)

                } else if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor: status $statusCode")
                }


                stream = BufferedInputStream(urlConnection.inputStream)
                val jsonAsString = stream.bufferedReader().use { it.readText() }
                val movieDetail = toMovieDetail(jsonAsString)

                handler.post {
                    callback.onResult(movieDetail)
                }


            } catch (e: Exception) {
                val message = e.message ?: "Erro desconhecido"
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
}
    private fun toMovieDetail(jsonAsString: String): MovieDetail {
        val json = JSONObject(jsonAsString)
        val id = json.getInt("id")
        val title = json.getString("title")
        val desc = json.getString("desc")
        val cast = json.getString("cast")
        val coverUrl = json.getString("cover_url")
        val jsonMovies = json.getJSONArray("movie")

        val similars = mutableListOf<Movie>()
        for (i in 0 until jsonMovies.length()) {
            val jsonMovie = jsonMovies.getJSONObject(i)

            val similarId = jsonMovie.getInt("id")
            val similarCoverUrl = jsonMovie.getString("cover_url")
            val m = Movie(similarId, similarCoverUrl)
            similars.add(m)
        }
        val movie = Movie(id, coverUrl, title, desc, cast)
        return MovieDetail(movie, similars)
    }
