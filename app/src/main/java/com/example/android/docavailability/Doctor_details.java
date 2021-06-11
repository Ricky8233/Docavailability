package com.example.android.docavailability;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Doctor_details extends AppCompatActivity {
    EditText doctor_name, Doctor_id, Doctor_speciality,Doctor_phone_number;
    Button proceed;
    FirebaseFirestore dbroot;
    FirebaseAuth mAuth;
    private ImageView doctor_image;
    ToggleButton tb;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    private Bitmap compressor;
    Uri imageLocationPath;
    String user_id;
    String HOS_name;
    String name,id,spec,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        Doctor_id = findViewById(R.id.Doctor_id);
        doctor_name = findViewById(R.id.Doctor_name);
        Doctor_phone_number = findViewById(R.id.Doctor_phone_number);
        mAuth = FirebaseAuth.getInstance();
        Doctor_speciality = findViewById(R.id.Doctor_speciality);
        proceed = findViewById(R.id.edit_doctor_details_);
        dbroot = FirebaseFirestore.getInstance();
        tb = findViewById(R.id.toggleButton_edit);
        doctor_image = findViewById(R.id.my_avatar);
        storageReference = FirebaseStorage.getInstance().getReference("USERS");
        user_id = mAuth.getCurrentUser().getUid();
        doctor_image.setOnClickListener(this::SelectImage);
        proceed.setOnClickListener(v ->
        {
            phone = Doctor_phone_number.getText().toString();
            id = Doctor_id.getText().toString();
            name = doctor_name.getText().toString();
            spec = Doctor_speciality.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(spec) || doctor_image.getDrawable() == null || TextUtils.isEmpty(phone)) {
                Toast.makeText(Doctor_details.this, "Please add all details. ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                showToast();
                uploadImage(v);
            }

        });
}
    public void SelectImage(View view)
    {
        try{
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,71);
        }
        catch(Exception e)
        {
            Toast.makeText(Doctor_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 71 && resultCode==RESULT_OK && data!=null&&data.getData()!=null)
        {
            imageLocationPath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageLocationPath);
                doctor_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void uploadImage(View view) {
        try
        {
            if(imageLocationPath!=null)
            {
                HOS_name = mAuth.getUid();
                String nameOfimage = doctor_name.getText().toString() +  ".jpeg";
                StorageReference image_ref=storageReference.child(HOS_name).child(nameOfimage);
                UploadTask uploadTask=image_ref.putFile(imageLocationPath);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return image_ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Map<String,Object> hashMap= new HashMap<>();
                        hashMap.put("image",task.getResult().toString());
                        hashMap.put("name",name);
                        hashMap.put("id",id);
                        hashMap.put("phone",phone);
                        hashMap.put("spec",spec);
                        if (tb.getText().toString().equals("Available")) {
                            hashMap.put("Available", true);
                        } else {
                            hashMap.put("Available", false);
                        }
                        dbroot.collection("USERS").document(mAuth.getUid()).collection("Doctor_Details").document(name).set(hashMap).addOnCompleteListener(t -> Toast.makeText(Doctor_details.this, "Successfully added. ", Toast.LENGTH_SHORT).show());
                        Intent i = new Intent(Doctor_details.this, Hospital_details.class);
                        startActivity(i);
                        finish();
                    }
                    else if(!task.isSuccessful())
                    {
                        Toast.makeText(Doctor_details.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                    }
                });


            }
            else
            {
                Toast.makeText(this,"Please enter Valid Details",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private String  Extension(Uri uri) {
        try {
            ContentResolver object = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(object.getType(uri));
        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return null;
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

