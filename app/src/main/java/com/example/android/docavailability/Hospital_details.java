package com.example.android.docavailability;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.docavailability.Adapter.HospitalDetailAdapter;
import com.example.android.docavailability.Model.HospitalDetailModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class Hospital_details extends AppCompatActivity
{
    RecyclerView rv;
    ArrayList<HospitalDetailModel> doctorlist;
    FloatingActionButton add;
    FirebaseFirestore dbroot;
    FirebaseAuth fauth;
    HospitalDetailAdapter adapter;
    TextView tv;
    TextView tb;
    ImageButton imageButton;
    ImageView hImage;
    ImageButton ib;
    String image_url;
    TextView phone_number;
    TextView icu_beds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);
        add = findViewById(R.id.button_add_doc);
        imageButton = findViewById(R.id.imageButton);
        ib = findViewById(R.id.imageButtoncardView);
        hImage = findViewById(R.id.hImage);
        tb = findViewById(R.id.txt_avail);
        rv = findViewById(R.id.RV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        doctorlist = new ArrayList<>();
        dbroot = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        icu_beds = findViewById(R.id.H_icu);
        phone_number = findViewById(R.id.H_phone_number);
        tv = findViewById(R.id.textView2);
        String id = fauth.getUid();
        adapter = new HospitalDetailAdapter(doctorlist,this);
        rv.setAdapter(adapter);
        dbroot.collection("USERS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tv.setText(documentSnapshot.getString("name"));
                icu_beds.setText(documentSnapshot.getString("ICU_Beds "));
                phone_number.setText(documentSnapshot.getString("PhoneNumber "));
            }
        });
        dbroot.collection("USERS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                image_url = documentSnapshot.getString("uri");
                Glide.with(Hospital_details.this).load(image_url).into(hImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Hospital_details.this, "error", Toast.LENGTH_LONG).show();
            }
        });

        dbroot.collection("USERS").document(id).collection("Doctor_Details").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot documentSnapshot : list) {
                String name = documentSnapshot.getString("name");
                String spec = documentSnapshot.getString("spec");
                String id1 = documentSnapshot.getString("id");
                Boolean toggle = documentSnapshot.getBoolean("Available");
                String image = documentSnapshot.getString("image");
                String phone_doctor = documentSnapshot.getString("phone");
                doctorlist.add(new HospitalDetailModel(name, spec, id1, toggle, image,phone_doctor));
            }
            adapter.notifyDataSetChanged();
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Hospital_details.this, Doctor_details.class);
                startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Hospital_details.this, v);
                popup.getMenuInflater().inflate(R.menu.edithospitaldetails, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.toString().equals("Log Out")) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Hospital_details.this, WelcomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(Hospital_details.this, Hospitaldetailsedit.class));
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

}

