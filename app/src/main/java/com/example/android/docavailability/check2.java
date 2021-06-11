package com.example.android.docavailability;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.docavailability.Adapter.HospitalDetailAdapter;
import com.example.android.docavailability.Model.HospitalDetailModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class check2 extends AppCompatActivity {

    FirebaseFirestore dbroot;
    String uid;
    TextView hname;
    ArrayList<HospitalDetailModel> doctorlist;
    RecyclerView rv;
    HospitalDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check2);
        dbroot = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.recycler_view_home);
        rv.setLayoutManager(new LinearLayoutManager(this));
        doctorlist = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            uid = (String) b.get("uid");
        }
        hname = findViewById(R.id.hname_check2);
        adapter = new HospitalDetailAdapter(doctorlist,this);
        rv.setAdapter(adapter);

        dbroot.collection("USERS").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot documentSnapshot : list) {
                String current_uid = documentSnapshot.getString("uid");

                if(current_uid.equals(uid))
                {
                    hname.setText(documentSnapshot.getString("name"));
                    dbroot.collection("USERS").document(uid).collection("Doctor_Details").get().addOnSuccessListener(q -> {
                        List<DocumentSnapshot> list2 = q.getDocuments();
                        for (DocumentSnapshot d : list2) {
                            String name = d.getString("name");
                            String spec = d.getString("spec");
                            String id1 = d.getString("id");
                            Boolean toggle = d.getBoolean("Available");
                            String image = d.getString("image");
                            String phone_doctor = d.getString("phone");
                            doctorlist.add(new HospitalDetailModel(name, spec, id1, toggle, image,phone_doctor));
                        }
                        adapter.notifyDataSetChanged();
                    });
                    break;
                }
            }
        });
    }
}