package dev.tiagomarcial.netflix_remake

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tiagomarcial.netflix_remake.model.Category
import dev.tiagomarcial.netflix_remake.model.Movie
import dev.tiagomarcial.netflix_remake.util.CategoryTask

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val categories = mutableListOf<Category>()

        val adapter = CategoryAdapter(categories)
        val rv: RecyclerView =  findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        CategoryTask().execute("https://api.tiagoaguiar.co//netflixapp/home?apiKey=e3947471-3c1d-448c-9748-1fa9b986fde3")
    }
}