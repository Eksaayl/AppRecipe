package com.example.apprecipe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    var mRecyclerView: RecyclerView? = null
    var myFoodList: List<FoodData>? = null
    var mFoodData: FoodData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val gridLayoutManager = GridLayoutManager(this, 1)
        mRecyclerView!!.layoutManager = gridLayoutManager
        myFoodList = ArrayList()
        val myAdapter = MyAdapter(this, myFoodList as ArrayList<FoodData>)
        mRecyclerView!!.adapter = myAdapter
    }

    fun btn_uploadActivity(view: View) {
        startActivity(Intent(this, Upload_Recipe::class.java))
    }
}