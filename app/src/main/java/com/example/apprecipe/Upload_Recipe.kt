package com.example.apprecipe

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class Upload_Recipe : AppCompatActivity() {
    var recipeImage: ImageView? = null
    var uri: Uri? = null
    var txt_recipe_name: EditText? = null
    var txt_description: EditText? = null
    private var txt_price: EditText? = null
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_recipe)
        recipeImage = findViewById<View>(R.id.iv_foodImage) as ImageView
        txt_recipe_name = findViewById<View>(R.id.txt_recipe_name) as EditText
        txt_description = findViewById<View>(R.id.txt_description) as EditText
        txt_price = findViewById<View>(R.id.txt_price) as EditText
    }

    fun btnSelectImage(view: View?) {
        val photoPicker = Intent(Intent.ACTION_PICK)
        photoPicker.type = "image/*"
        startActivityForResult(photoPicker, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            uri = data!!.data
            recipeImage!!.setImageURI(uri)
        } else Toast.makeText(this, "You haven't picked a picture", Toast.LENGTH_SHORT).show()
    }

    fun uploadImage() {
        val sf = FirebaseStorage.getInstance()
            .reference.child("RecipeImage").child(uri!!.lastPathSegment!!)
        sf.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageUrl = urlImage.toString()
            uploadRecipe()
        }
    }

    fun btnUploadRecipe(view: View?) {
        uploadImage()
    }

    fun uploadRecipe() {
        val pd = ProgressDialog(this)
        pd.setMessage("Recipe uploading...")
        pd.show()
        val foodData = FoodData(
            txt_recipe_name!!.text.toString(),
            txt_description!!.text.toString(),
            txt_price!!.text.toString(),
            imageUrl!!
        )
        val myCurrentDateTime = DateFormat.getDateTimeInstance()
            .format(Calendar.getInstance().time)
        FirebaseDatabase.getInstance("https://apprecipe-53242-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Recipe")
            .child(myCurrentDateTime).setValue(foodData).addOnSuccessListener {
                Toast.makeText(this, "Recipe Uploaded", Toast.LENGTH_SHORT).show()
                pd.dismiss()
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                pd.dismiss()
            }
    }
}