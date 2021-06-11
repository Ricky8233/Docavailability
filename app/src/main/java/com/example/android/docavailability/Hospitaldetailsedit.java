package com.example.android.docavailability;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class Hospitaldetailsedit extends AppCompatActivity {
    EditText Hospital_name;
    private Uri imageUri;
    StorageReference storageReference;
    private Bitmap compressor;
    String user_id;
    FirebaseFirestore dbroot;
    ImageView hospital_image;
    Button editbutton;
    FirebaseAuth fauth;
    Uri imageLocationPath;
    EditText Icu_beds;
    EditText phone_number;
    String name;
    String pass,email;
    String phone_n;
    String icu_b;
    String url,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitaldetailsedit);
        Hospital_name = findViewById(R.id.edit_hospitalname);
        Icu_beds = findViewById(R.id.icu_beds);
        phone_number = findViewById(R.id.Hospital_Phone_number);
        dbroot = FirebaseFirestore.getInstance();
        editbutton = findViewById(R.id.edit_button);
        fauth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("hospitals");
        hospital_image = findViewById(R.id.hospital_image);
        id= fauth.getUid();
        dbroot.collection("USERS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Hospital_name.setText(documentSnapshot.getString("name"));
                email= documentSnapshot.getString("email");
                pass=documentSnapshot.getString("pass");
                Icu_beds.setText(documentSnapshot.getString("ICU_Beds "));
                phone_number.setText(documentSnapshot.getString("PhoneNumber "));
                icu_b = documentSnapshot.getString("ICU_Beds ");
                phone_n = documentSnapshot.getString("PhoneNumber ");
                url = documentSnapshot.getString("uri");
                Glide.with(Hospitaldetailsedit.this).load(url).into(hospital_image);
            }
        });
        Hospital_name.setText(name);
        Icu_beds.setText(icu_b);
        phone_number.setText(phone_n);
        hospital_image.setOnClickListener(this::SelectImage);
        editbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(Hospitaldetailsedit.this,"Saving data please wait...",Toast.LENGTH_LONG).show();
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
            Toast.makeText(Hospitaldetailsedit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 71 && resultCode==RESULT_OK && data!=null&&data.getData()!=null) {

                imageLocationPath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageLocationPath);
                    hospital_image.setImageBitmap(bitmap);
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
                String nameOfimage = Hospital_name.getText().toString() +  ".jpeg";
                StorageReference image_ref=storageReference.child(nameOfimage);
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
                        Map<String,String> hashMap= new HashMap<>();
                        hashMap.put("uri",task.getResult().toString());
                        hashMap.put("pass",pass);
                        hashMap.put("email",email);
                        hashMap.put("name",Hospital_name.getText().toString());
                        hashMap.put("PhoneNumber ",phone_number.getText().toString());
                        hashMap.put("ICU_Beds " ,Icu_beds.getText().toString());
                        hashMap.put("uid",id);
                        dbroot.collection("USERS").document(fauth.getUid()).delete();
                        dbroot.collection("USERS").document(fauth.getUid()).set(hashMap);
                        Intent i = new Intent(Hospitaldetailsedit.this, Hospital_details.class);
                        startActivity(i);
                    }
                    else if(!task.isSuccessful())
                    {
                        Toast.makeText(Hospitaldetailsedit.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
            else
            {
                Map<String,String> hashMap= new HashMap<>();
                hashMap.put("uri",url);
                hashMap.put("pass",pass);
                hashMap.put("email",email);
                hashMap.put("name",Hospital_name.getText().toString());
                hashMap.put("PhoneNumber ",phone_number.getText().toString());
                hashMap.put("ICU_Beds " ,Icu_beds.getText().toString());
                hashMap.put("uid",id);
                dbroot.collection("USERS").document(fauth.getUid()).delete();
                dbroot.collection("USERS").document(fauth.getUid()).set(hashMap);
                Intent i = new Intent(Hospitaldetailsedit.this, Hospital_details.class);
                startActivity(i);
            }

        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
