package com.example.android.docavailability;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoctorDetailsedit extends AppCompatActivity {


    StorageReference firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText name, id, spec,phone;
    ToggleButton toggleButton;
    Button edit;
    String doctor_name = null;
    ImageView imageView;
    Uri imageLocationPath;
    String image_url;
    Boolean ok = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detailsedit);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference("USERS");
        edit = findViewById(R.id.edit_doctor_details_);
        id = findViewById(R.id.Doctor_id_edit);
        name = findViewById(R.id.Doctor_name_edit);
        phone = findViewById(R.id.DoctorphoneN);
        spec = findViewById(R.id.Doctor_speciality_edit);
        toggleButton = findViewById(R.id.toggleButton_edit);
        imageView = findViewById(R.id.my_avatar_edit);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            doctor_name = (String) b.get("name");
        }

        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("Doctor_Details").document(doctor_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                image_url = documentSnapshot.getString("image");
                Glide.with(DoctorDetailsedit.this)
                        .load(image_url)
                        .into(imageView);
                /*imageLocationPath= Uri.parse(image_url);*/
                name.setText(documentSnapshot.getString("name"));
                id.setText(documentSnapshot.getString("id"));
                phone.setText(documentSnapshot.getString("phone"));
                spec.setText(documentSnapshot.getString("spec"));
                if (documentSnapshot.getBoolean("Available")) {
                    toggleButton.setText("Available");
                } else {
                    toggleButton.setText("Not_Available");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorDetailsedit.this, "error", Toast.LENGTH_LONG).show();
            }
        });

        imageView.setOnClickListener(this::SelectImage);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() < 1 || id.getText().length() < 1 || spec.getText().length() < 1 || phone.getText().length()<1) {
                    Toast.makeText(DoctorDetailsedit.this, "Please add all details", Toast.LENGTH_LONG).show();
                } else {
                    showToast();
                    Edit(v);
                }
            }
        });


    }

    private void Edit(View view) {
        try {

            if (imageLocationPath == null) {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("image", image_url);
                hashMap.put("name", name.getText().toString());
                hashMap.put("id", id.getText().toString());
                hashMap.put("phone",phone.getText().toString());
                hashMap.put("spec", spec.getText().toString());
                if (toggleButton.getText().toString().equals("Available")) {
                    hashMap.put("Available", true);
                } else {
                    hashMap.put("Available", false);
                }
                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("Doctor_Details").document(doctor_name).delete();
                firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("Doctor_Details").document(name.getText().toString()).set(hashMap).addOnCompleteListener(t -> Toast.makeText(DoctorDetailsedit.this, "Successfully added. ", Toast.LENGTH_SHORT).show());
                Intent i = new Intent(DoctorDetailsedit.this, Hospital_details.class);
                startActivity(i);
                finish();
            }
            else {
                String nameOfimage = doctor_name +  ".jpeg";
                StorageReference image_ref = firebaseStorage.child(firebaseAuth.getUid()).child(nameOfimage);

                UploadTask uploadTask = image_ref.putFile(imageLocationPath);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {

                            throw Objects.requireNonNull(task.getException());

                        }

                        return image_ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put("image", task.getResult().toString());
                        hashMap.put("name", name.getText().toString());
                        hashMap.put("id", id.getText().toString());
                        hashMap.put("phone",phone.getText().toString());
                        hashMap.put("spec", spec.getText().toString());
                        if (toggleButton.getText().toString().equals("Available")) {
                            hashMap.put("Available", true);
                        } else {
                            hashMap.put("Available", false);
                        }
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("Doctor_Details").document(doctor_name).delete();
                        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("Doctor_Details").document(name.getText().toString()).set(hashMap).addOnCompleteListener(t -> Toast.makeText(DoctorDetailsedit.this, "Successfully added. ", Toast.LENGTH_SHORT).show());


                        Intent i = new Intent(DoctorDetailsedit.this, Hospital_details.class);
                        startActivity(i);
                        finish();
                    } else if (!task.isSuccessful()) {
                        Toast.makeText(DoctorDetailsedit.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void SelectImage(View view) {
        try {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, 71);
        } catch (Exception e) {
            Toast.makeText(DoctorDetailsedit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 71 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageLocationPath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocationPath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private Toast mToastToShow;

    public void showToast() {
        int toastDurationInMilliSeconds = 4000;
        mToastToShow = Toast.makeText(this, "Pleas Wait while saving data..", Toast.LENGTH_LONG);
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