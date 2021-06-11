package com.example.android.docavailability;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.docavailability.Adapter.HospitalDetailAdapter;
import com.example.android.docavailability.Adapter.Hospitaladapter;
import com.example.android.docavailability.Model.HospitalDetailModel;
import com.example.android.docavailability.Model.Hospitalmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Check_availability_home extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<Hospitalmodel> hospital_list;
    Hospitaladapter adapter;
    TextView hospital_name,email_hospital,icu_beds;
    TextView phone_number;
    FirebaseFirestore dbroot;
    FirebaseAuth fauth;
    ImageView hospital_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability_home);
        rv=findViewById(R.id.recycler_view_home);
        rv.setLayoutManager(new LinearLayoutManager(this));
        hospital_list = new ArrayList<>();
        adapter = new Hospitaladapter(hospital_list,this);
        rv.setAdapter(adapter);

        hospital_image =  findViewById(R.id.check_hospital_image_fetched);
        email_hospital = findViewById(R.id.check_email_hospital);
        icu_beds = findViewById(R.id.check_icu_beds);
        phone_number = findViewById(R.id.check_Phone_number);
        hospital_name = findViewById(R.id.check_Hn);
        dbroot = FirebaseFirestore.getInstance();
        dbroot.collection("USERS").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot documentSnapshot : list) {
                String phone = documentSnapshot.getString("PhoneNumber ");
                String icu_beds = documentSnapshot.getString("ICU_Beds ");
                String name = documentSnapshot.getString("name");
                String uri = documentSnapshot.getString("uri");
                String email = documentSnapshot.getString("email");
                String uid = documentSnapshot.getString("uid");
                hospital_list.add(new Hospitalmodel(name,uri,email,phone,icu_beds,uid));
            }
            adapter.notifyDataSetChanged();
        });
    }
}