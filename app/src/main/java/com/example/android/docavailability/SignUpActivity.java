package com.example.android.docavailability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity";
    EditText edittext_email,edittext_password,edittext_hospital_name;
    Button signUpButton;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    private Bitmap compressor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        fstore= FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        edittext_hospital_name = findViewById(R.id.Hospital_name);
        edittext_email = findViewById(R.id.signUpEmailTextInput);
        edittext_password = findViewById(R.id.Signup_password);
        signUpButton = findViewById(R.id.signUpButton);

        if(fauth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Hospital_details.class));
            finish();
        }
        signUpButton.setOnClickListener(v -> {
            String email=edittext_email.getText().toString();
            String pass=edittext_password.getText().toString();
            String name =edittext_hospital_name.getText().toString();

            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(name))
            {
                Toast.makeText(SignUpActivity.this, "Please fill all details.", Toast.LENGTH_SHORT).show();
                return;
            }

            fauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    Toast.makeText(SignUpActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                    String userid=fauth.getCurrentUser().getUid();
                    DocumentReference dr=fstore.collection("USERS").document(userid);
                    Map<String,Object> map=new HashMap<>();
                    map.put("name",name);
                    map.put("pass",pass);
                    map.put("email",email);
                    map.put("PhoneNumber "," ");
                    map.put("ICU_Beds " , " ");
                    map.put("uri","");
                    map.put("uid",userid);
                    dr.set(map).addOnSuccessListener(aVoid -> Toast.makeText(SignUpActivity.this, "Done", Toast.LENGTH_SHORT).show());
                    byte[] thumb = new byte[0];
                    fauth = FirebaseAuth.getInstance();
                    storageReference= FirebaseStorage.getInstance().getReference();
                    UploadTask image_path = storageReference.child("hospitals").child(fauth.getUid()+ ".jpg").putBytes(thumb);
                    startActivity(new Intent(getApplicationContext(),Hospital_details.class));
                    finish();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Error please try again", Toast.LENGTH_SHORT).show();

                }
            });
        });





    }
}