package com.example.android.docavailability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.grpc.Compressor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
public class Doctor_details extends AppCompatActivity {
    EditText doctor_name, Doctor_id, Doctor_speciality;
    Button proceed;
    FirebaseFirestore dbroot;
    FirebaseAuth mAuth;
    private ImageView doctor_image;
    ToggleButton tb;
    Uri imageUri;
    StorageReference storageReference;
    Bitmap Compressor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        Doctor_id = findViewById(R.id.Doctor_id);
        doctor_name = findViewById(R.id.Doctor_name);
        mAuth = FirebaseAuth.getInstance();
        Doctor_speciality = findViewById(R.id.Doctor_speciality);
        proceed = findViewById(R.id.proceed_doctor_details);
        dbroot = FirebaseFirestore.getInstance();
        tb = findViewById(R.id.toggleButton);
        doctor_image = findViewById(R.id.my_avatar);
        storageReference = FirebaseStorage.getInstance().getReference();
        doctor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Doctor_details.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Doctor_details.this, "permission denied ", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(Doctor_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        ChoseImage();
                    }
                } else {
                    ChoseImage();
                }
            }
        });
        proceed.setOnClickListener(v -> {
            String id = Doctor_id.getText().toString();
            String name = doctor_name.getText().toString();
            String spec = Doctor_speciality.getText().toString();
            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(id) && TextUtils.isEmpty(spec)) {
                Toast.makeText(Doctor_details.this, "Please add all details. ", Toast.LENGTH_SHORT).show();
            } else {
                insertdata();
                Intent i = new Intent(Doctor_details.this, Hospital_details.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void insertdata() {
        Map<String, Object> doctors = new HashMap<>();

        doctors.put("name", doctor_name.getText().toString());
        doctors.put("id", Doctor_id.getText().toString());
        doctors.put("spec", Doctor_speciality.getText().toString());
        if (tb.getText().toString().equals("ON")) {
            doctors.put("Available", true);
        } else {
            doctors.put("Available", false);
        }

        String id = mAuth.getUid();
        assert id != null;
        dbroot.collection("USERS").document(id).collection("Doctor_Details").add(doctors).addOnCompleteListener(task -> Toast.makeText(Doctor_details.this, "Successfully added. ", Toast.LENGTH_SHORT).show());
    }

    private void ChoseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(Doctor_details.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                imageUri = result.getUri();
                doctor_image.setImageURI(imageUri);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }
}