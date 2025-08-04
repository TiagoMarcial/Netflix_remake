package dev.tiagomarcial.netflix_remake

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.tiagomarcial.netflix_remake.model.Movie
import dev.tiagomarcial.netflix_remake.model.MovieDetail
import dev.tiagomarcial.netflix_remake.util.MovieTask
import java.lang.Exception

class MovieActivity : AppCompatActivity(), MovieTask.Callback {

    private lateinit var  txtTitle: TextView
    private lateinit var  txtDesc: TextView
    private lateinit var  txtCast: TextView
    private lateinit var  adapter: MovieAdapter
    private lateinit var progress: ProgressBar
    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie)

        txtTitle = findViewById(R.id.movie_txt_title)
        txtDesc = findViewById(R.id.movie_txt_desc)
        txtCast = findViewById(R.id.movie_txt_cast)
        progress = findViewById(R.id.movie_progress)

        val rv: RecyclerView = findViewById(R.id.movie_rv_similar)
        val id = intent?.getIntExtra("id", 0) ?: throw IllegalStateException("ID não foi encontrado!")
        val url = "https://api.tiagoaguiar.co//netflixapp/movie/$id?apiKey=e3947471-3c1d-448c-9748-1fa9b986fde3"

        MovieTask(this).execute(url)




        adapter = MovieAdapter(movies, R.layout.movie_item_similar)
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = adapter

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.outline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

//        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable
//        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)
//        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
//
//        val coverImg: ImageView = findViewById(R.id.movie_img)
//        coverImg.setImageDrawable(layerDrawable)
    }

    override fun onPreExecute() {
        progress.visibility = View.VISIBLE

    }

    override fun onFailure(message: String) {
        progress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onResult(movieDetail: MovieDetail) {
        progress.visibility = View.GONE

        txtTitle.text = movieDetail.movie.title
        txtDesc.text = movieDetail.movie.desc
        txtCast.text = getString(R.string.cast, movieDetail.movie.cast)

        movies.clear()
        movies.addAll(movieDetail.similars)
        adapter.notifyDataSetChanged()

        val coverImg: ImageView = findViewById(R.id.movie_img)


        val target = object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap != null) {
                    val movieCover = BitmapDrawable(resources, bitmap)

                    val layerDrawable = ContextCompat.getDrawable(
                        this@MovieActivity,
                        R.drawable.shadows
                    ) as LayerDrawable


                    layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

                    coverImg.setImageDrawable(layerDrawable)
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                coverImg.setImageResource(R.drawable.placeholder_bg)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                coverImg.setImageDrawable(placeHolderDrawable)
            }
        }


        coverImg.tag = target


        Picasso.get()
            .load(movieDetail.movie.coverUrl)
            .placeholder(R.drawable.placeholder_bg)
            .error(R.drawable.placeholder_bg)
            .into(target)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}