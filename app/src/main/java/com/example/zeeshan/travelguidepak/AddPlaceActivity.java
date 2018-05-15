package com.example.zeeshan.travelguidepak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class AddPlaceActivity extends AppCompatActivity {

    private Toolbar addPlaceToolbar;
    private ImageView newPlaceImage;
    private EditText newPlaceTitle;
    private EditText newPlaceDescription;
    private Button addPlaceBtn;
    private ProgressBar newPlaceProgress;
    private Uri placeImageUri = null;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private City city;
    private Bitmap compressedImageFile;

    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        newPlaceImage = (ImageView) findViewById(R.id.new_place_image);
        newPlaceTitle = (EditText) findViewById(R.id.new_place_title);
        newPlaceDescription = (EditText) findViewById(R.id.new_place_desc);
        addPlaceBtn = (Button) findViewById(R.id.add_place_btn);
        newPlaceProgress = (ProgressBar) findViewById(R.id.newPlaceProgress);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUserId = firebaseAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        addPlaceToolbar =  findViewById(R.id.add_place_toolbar);
        setSupportActionBar(addPlaceToolbar);
        getSupportActionBar().setTitle("New Place");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        city = (City) intent.getSerializableExtra("city");


        newPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2,1)
                        .start(AddPlaceActivity.this);
            }
        });

        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = newPlaceTitle.getText().toString();
                final String desc = newPlaceDescription.getText().toString();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && placeImageUri != null){
                    newPlaceProgress.setVisibility(View.VISIBLE);

                    // Posting to Firebase

                    // 1. Guardamos la foto en storage.
                    final String nombreFichero = UUID.randomUUID().toString(); // generamos un id random para la foto.
                    StorageReference ficheros = storageReference.child("images_places").child(nombreFichero + ".jpg");
                    ficheros.putFile(placeImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            final String url = task.getResult().getDownloadUrl().toString();
                            if(task.isSuccessful()){
                                // Uploading the ThumbNail.
                                File newPlaceImage = new File(placeImageUri.getPath());
                                try {
                                    compressedImageFile = new Compressor(AddPlaceActivity.this)
                                            //.setMaxHeight(200)
                                           // .setMaxWidth(200)
                                            //.setQuality(1)
                                            .compressToBitmap(newPlaceImage);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask thumbNail = storageReference.child("images_places/thumbnails").child(nombreFichero  + ".jpg").putBytes(data);

                                thumbNail.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String downloadThumb = taskSnapshot.getDownloadUrl().toString();

                                        // 2. Ahora Subimos la info a firebase firestore.
                                        Map<String, Object> placeMap = new HashMap<>();
                                        placeMap.put("title", title);
                                        placeMap.put("image", url.toString());
                                        placeMap.put("thumbnail", downloadThumb);
                                        placeMap.put("description", desc);
                                        placeMap.put("userId", currentUserId);
                                        placeMap.put("city", city.getName());
                                        placeMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Places").add(placeMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if(task.isSuccessful()){

                                                    Toast.makeText(AddPlaceActivity.this, "Place Saved", Toast.LENGTH_LONG).show();

                                                    //Intent intent = new Intent(AddPlaceActivity.this, CityDetailsActivity.class);
                                                    //intent.putExtra("city", city);
                                                    //startActivity(intent);
                                                    finish();

                                                } else {

                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddPlaceActivity.this, "Information not Uploaded Correctly: " + error, Toast.LENGTH_LONG).show();

                                                }

                                                newPlaceProgress.setVisibility(View.INVISIBLE);

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(AddPlaceActivity.this, "IMAGE Not Uploaded Correctly: " + error, Toast.LENGTH_LONG).show();
                                newPlaceProgress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }

            }
        });




    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                placeImageUri = result.getUri();
                newPlaceImage.setImageURI(placeImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
