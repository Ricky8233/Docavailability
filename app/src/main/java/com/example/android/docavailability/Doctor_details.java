package com.example.android.docavailability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.grpc.Compressor;

public class Doctor_details extends AppCompatActivity {
    ImageView doctorimage;
    EditText doctor_name , Doctor_id , Doctor_speciality;
    Button proceed;
    FirebaseFirestore dbroot;
    FirebaseAuth mAuth;
    ImageView image;
    ToggleButton tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        Doctor_id=findViewById(R.id.Doctor_id);
        doctor_name=findViewById(R.id.Doctor_name);
        mAuth = FirebaseAuth.getInstance();
        doctorimage = findViewById(R.id.Doctor_image);
        Doctor_speciality=findViewById(R.id.Doctor_speciality);
        proceed = findViewById(R.id.proceed_doctor_details);
        dbroot=FirebaseFirestore.getInstance();
        tb=findViewById(R.id.toggleButton);
        image=findViewById(R.id.doctor_image);
        proceed.setOnClickListener(v -> {
            String id = Doctor_id.getText().toString();
            String name = doctor_name.getText().toString();
            String spec = Doctor_speciality.getText().toString();
            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(id) && TextUtils.isEmpty(spec)) {
                Toast.makeText(Doctor_details.this, "Please add all details. ", Toast.LENGTH_SHORT).show();
            }
            else
                {
                    insertdata();
                    Intent i = new Intent(Doctor_details.this,Hospital_details.class);
                    startActivity(i);
                    finish();
            }
        });
        Intent i = new Intent(Doctor_details.this,Hospital_details.class);
        startActivity(i);
        finish();

    }

    private void insertdata() {
        Map<String,Object> doctors=new HashMap<>();

        doctors.put("name", doctor_name.getText().toString());
        doctors.put("id", Doctor_id.getText().toString());
        doctors.put("spec", Doctor_speciality.getText().toString());
        if(tb.getText().toString().equals("ON"))
        {
            doctors.put("Available",true);
        }
        else
        {
            doctors.put("Available",false);
        }

        String id=mAuth.getUid();
        assert id != null;
        dbroot.collection("USERS").document(id).collection("Doctor_Details").add(doctors).addOnCompleteListener(task -> Toast.makeText(Doctor_details.this, "Successfully added. ", Toast.LENGTH_SHORT).show());

    }

}