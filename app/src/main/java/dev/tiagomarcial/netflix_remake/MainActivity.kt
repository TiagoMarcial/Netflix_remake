package dev.tiagomarcial.netflix_remake

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tiagomarcial.netflix_remake.model.Category
import dev.tiagomarcial.netflix_remake.model.Movie

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
    }
}