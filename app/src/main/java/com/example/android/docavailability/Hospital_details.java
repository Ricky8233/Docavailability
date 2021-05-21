package com.example.android.docavailability;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.docavailability.Adapter.HospitalDetailAdapter;
import com.example.android.docavailability.Model.HospitalDetailModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
public class Hospital_details extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<HospitalDetailModel> doctorlist;
    Button add;
    FirebaseFirestore dbroot;
    FirebaseAuth fauth;
    HospitalDetailAdapter adapter;
    TextView tv;
    ToggleButton tb;
    ImageButton imageButton;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);
        add = findViewById(R.id.button_add_doc);
        imageButton = findViewById(R.id.imageButton);
        tb = findViewById(R.id.TB);
        rv = findViewById(R.id.RV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        doctorlist = new ArrayList<>();
        dbroot = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        tv = findViewById(R.id.textView2);
        String id = fauth.getUid();
        adapter = new HospitalDetailAdapter(doctorlist);
        rv.setAdapter(adapter);
        dbroot.collection("USERS").document(id).get().addOnSuccessListener(documentSnapshot -> tv.setText(documentSnapshot.getString("name")));
        dbroot.collection("USERS").document(id).collection("Doctor_Details").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot documentSnapshot : list) {
                String name = documentSnapshot.getString("name");
                String spec = documentSnapshot.getString("spec");
                String id1 = documentSnapshot.getString("id");
                Boolean toggle = documentSnapshot.getBoolean("Available");
                String image = documentSnapshot.getString("image");
                doctorlist.add(new HospitalDetailModel(name, spec, id1, toggle));
            }
            adapter.notifyDataSetChanged();
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Hospital_details.this, Doctor_details.class);
                startActivity(i);
                finish();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Hospital_details.this, v);
                popup.getMenuInflater().inflate(R.menu.edithospitaldetails, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.toString().equals("Log Out"))
                        {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Hospital_details.this, WelcomeActivity.class));
                            finish();
                        }
                        else
                            {
                            Toast.makeText(getApplicationContext(),
                                    item.toString() + " clicked at position 1",
                                    Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });
    }
}

