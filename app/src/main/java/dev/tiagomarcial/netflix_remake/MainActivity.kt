package dev.tiagomarcial.netflix_remake

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val adapter = MainAdapter()
        val rv: RecyclerView =  findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private inner class MainAdapter: RecyclerView.Adapter<MainAdapter.MovieViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = layoutInflater.inflate(R.layout.movie_item, parent, false)
            return MovieViewHolder(view)
        }

        override fun getItemCount(): Int {
            return 60
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        private inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }
}