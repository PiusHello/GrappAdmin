package com.example.grappadmin;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grappadmin.Model.Uploads;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UploadFood extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button chooseFileButton, uploadNowButton;
    private EditText foodCatName, foodPrice, foodDescription;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Spinner spinner;
    private StorageReference myStorage;
    private DatabaseReference myDatabase;
    private String foodKey = null;
    private Uri myImage;


    String foodkey;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_food);

        chooseFileButton = (Button) findViewById(R.id.chooseFile);
        uploadNowButton = (Button) findViewById(R.id.uploadNow);
        foodCatName = (EditText) findViewById(R.id.uploadFoodCategoryName);
        foodPrice = (EditText) findViewById(R.id.foodPrice);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.uploadFoodCategoryImage);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userData = currentUser.getUid();


        myDatabase = FirebaseDatabase.getInstance().getReference("FoodList");
        myStorage = FirebaseStorage.getInstance().getReference("UploadFood");


        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });


        uploadNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFood();
            }
        });
    }

    public void FoodCategorySpinner(View view)
    {
        spinner = (Spinner) findViewById(R.id.uploadFoodCategorySpinner);
        String foodCategory = String.valueOf(spinner.getSelectedItem());

    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            myImage = data.getData();
            Picasso.get().load(myImage).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadFood() {
        Calendar calendarForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        final String saveCurrentDate = currentDate.format(calendarForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        final String saveCurrentTime = currentTime.format(calendarForDate.getTime());

        final DatabaseReference FoodList = myDatabase.child("FoodList").push();

        final HashMap<String, Object> CartMap = new HashMap<>();
        CartMap.put("FoodID", foodkey);
        CartMap.put("Name", foodCatName.getText().toString());
        CartMap.put("Price", foodPrice.getText().toString());
        CartMap.put("Image", myImage);
        CartMap.put("Date", saveCurrentDate);
        CartMap.put("Time", saveCurrentTime);


        if (myImage != null) {
            StorageReference fileReference = myStorage.child(System.currentTimeMillis()
                    + " . " + getFileExtension(myImage));

            fileReference.putFile(myImage)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadFood.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 6000);


                            Toast.makeText(UploadFood.this, " Upload was successful", Toast.LENGTH_SHORT).show();
                            Uploads uploads = new Uploads(foodCatName.getText().toString().trim(),
                                    taskSnapshot.getStorage().getDownloadUrl().toString());
                            String uploadID = myDatabase.push().getKey();
                            myDatabase.child(uploadID).setValue(uploads);
//                            myDatabase.child("Date",saveCurrentDate);
//                            myDatabase.child("Time",saveCurrentTime);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, " No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
