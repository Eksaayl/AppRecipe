package com.example.apprecipe

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    var foodDescription: TextView? = null
    var foodImage: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        foodDescription = findViewById<View>(R.id.txtDescription) as TextView
        foodImage = findViewById<View>(R.id.ivImage2) as ImageView
        val mBundle = intent.extras
        if (mBundle != null) {
            foodDescription!!.text = mBundle.getString("Description")
            foodImage!!.setImageResource(mBundle.getInt("Image"))

            Glide.with(this)
                .load(mBundle.getInt("Image"))
                .into(foodImage!!);
        }
    }
}