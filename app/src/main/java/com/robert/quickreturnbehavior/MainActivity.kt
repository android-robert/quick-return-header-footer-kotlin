package com.robert.quickreturnbehavior

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "App Title"
        toolbar.subtitle = "Subtitle"
        val recyclerView = findViewById<View>(R.id.recyclerview) as RecyclerView
        recyclerView.adapter = SimpleStringRecyclerViewAdapter(listOf(*Cheeses.sCheeseStrings))
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}