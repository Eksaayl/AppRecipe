package com.example.apprecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class Upload_Recipe extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    EditText txt_recipe_name, txt_description, txt_price;
    String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        recipeImage = (ImageView)findViewById(R.id.iv_foodImage);
        txt_recipe_name = (EditText)findViewById(R.id.txt_recipe_name);
        txt_description = (EditText)findViewById(R.id.txt_description);
        txt_price = (EditText)findViewById(R.id.txt_price);

    }

    public void btnSelectImage(View view) {

        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            uri = data.getData();
            recipeImage.setImageURI(uri);
        }
        else Toast.makeText(this, "You haven't picked a picture", Toast.LENGTH_SHORT).show();
    }

    public void uploadImage(){

        StorageReference sf = FirebaseStorage.getInstance()
                .getReference().child("RecipeImage").child(uri.getLastPathSegment());

        sf.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadRecipe();
            }
        });
    }

    public void btnUploadRecipe(View view) {
        uploadImage();
    }

    public void uploadRecipe(){

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Recipe uploading...");
        pd.show();

        FoodData foodData = new FoodData(
          txt_recipe_name.getText().toString(),
          txt_description.getText().toString(),
          txt_price.getText().toString(),
          imageUrl
        );

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                        .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance("https://apprecipe-53242-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Recipe")
                .child(myCurrentDateTime).setValue(foodData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Upload_Recipe.this, "Recipe Uploaded", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                finish();
            }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload_Recipe.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    }
                });
    }
}