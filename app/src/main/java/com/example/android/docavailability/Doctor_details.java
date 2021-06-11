package com.example.android.docavailability;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;


public class Doctor_details extends AppCompatActivity {
    EditText doctor_name, Doctor_id, Doctor_speciality;
    Button proceed;
    FirebaseFirestore dbroot;
    FirebaseAuth mAuth;
    private ImageView doctor_image;
    ToggleButton tb;
    FirebaseFirestore firebaseFirestore;
    private Uri imageUri;
    StorageReference storageReference;
    private Bitmap compressor;
    String user_id;
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
        user_id = mAuth.getCurrentUser().getUid();
        doctor_image.setOnClickListener(v ->
        {
            boolean result = Utility.checkPermission(Doctor_details.this);
            if (result) {
                Toast.makeText(Doctor_details.this, "permission denied ", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(Doctor_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                ChoseImage();
            }
        });
        proceed.setOnClickListener(v ->
        {
            String id = Doctor_id.getText().toString();
            String name = doctor_name.getText().toString();
            String spec = Doctor_speciality.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(spec) || doctor_image.getDrawable() == null) {
                Toast.makeText(Doctor_details.this, "Please add all details. ", Toast.LENGTH_SHORT).show();
            } else {
                showToast();
                File file = new File(imageUri.getPath());
                try {
                    compressor = new Compressor(Doctor_details.this)
                            .setMaxHeight(125)
                            .setMaxWidth(125)
                            .setQuality(50)
                            .compressToBitmap(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressor.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] thumb = byteArrayOutputStream.toByteArray();

                UploadTask image_path = storageReference.child("USERS").child(user_id).child(doctor_name.getText().toString()+ ".jpg").putBytes(thumb);

                image_path.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storeDoctorData(task);
                    } else {
                        Toast.makeText(Doctor_details.this, "Please Try again later", Toast.LENGTH_LONG).show();
                    }
                });

            }

        });
    }

    private void storeDoctorData(Task<UploadTask.TaskSnapshot> taskSnapshotTask) {
        Uri download_uri;
        String down_uri;
        Map<String, Object> doctors = new HashMap<>();
        doctors.put("name", doctor_name.getText().toString());
        doctors.put("id", Doctor_id.getText().toString());
        doctors.put("spec", Doctor_speciality.getText().toString());
        if (taskSnapshotTask != null) {
            down_uri = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(taskSnapshotTask.getResult()).getMetadata()).getReference()).getDownloadUrl().toString();
            doctors.put("image", down_uri);
        } else {
            download_uri = imageUri;
            doctors.put("image", download_uri.toString());
        }

        if (tb.getText().toString().equals("ON")) {
            doctors.put("Available", true);
        } else {
            doctors.put("Available", false);
        }
        dbroot.collection("USERS").document(user_id).collection("Doctor_Details").add(doctors).addOnCompleteListener(task -> Toast.makeText(Doctor_details.this, "Successfully added. ", Toast.LENGTH_SHORT).show());
        Intent i = new Intent(Doctor_details.this, Hospital_details.class);
        startActivity(i);
        finish();
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
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                doctor_image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }

    private Toast mToastToShow;

    public void showToast() {
        int toastDurationInMilliSeconds = 4000;
        mToastToShow = Toast.makeText(this, "Pleas Wait while adding data..", Toast.LENGTH_LONG);
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }

            public void onFinish() {
                mToastToShow.cancel();
            }
        };
        mToastToShow.show();
        toastCountDown.start();
    }
}

class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}