package com.example.apprecipe

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var mRecyclerView: RecyclerView? = null
    var myFoodList: List<FoodData>? = null
    var mFoodData: FoodData? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val gridLayoutManager = GridLayoutManager(this, 1)
        mRecyclerView!!.layoutManager = gridLayoutManager

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading Items....")

        myFoodList = ArrayList()
        val myAdapter = MyAdapter(this, myFoodList as ArrayList<FoodData>)
        mRecyclerView!!.adapter = myAdapter


        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe")
        eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle data change
                (myFoodList as ArrayList<FoodData>)?.clear()
                for (itemSnapshot in dataSnapshot.children) {
                    val foodData: FoodData? = itemSnapshot.getValue(FoodData::class.java)
                    foodData?.let { (myFoodList as ArrayList<FoodData>)?.add(it) }
                }
                myAdapter.notifyDataSetChanged()
                progressDialog.dismiss();
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                progressDialog.dismiss();
            }
        }
        databaseReference.addValueEventListener(eventListener)
    }

    fun btn_uploadActivity(view: View) {
        startActivity(Intent(this, Upload_Recipe::class.java))
    }
}