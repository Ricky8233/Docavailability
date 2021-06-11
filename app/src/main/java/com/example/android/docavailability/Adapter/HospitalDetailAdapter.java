package com.example.android.docavailability.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.docavailability.DoctorDetailsedit;
import com.example.android.docavailability.Doctor_details;
import com.example.android.docavailability.Hospital_details;
import com.example.android.docavailability.Model.HospitalDetailModel;
import com.example.android.docavailability.R;
import com.example.android.docavailability.WelcomeActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class HospitalDetailAdapter extends RecyclerView.Adapter<HospitalDetailAdapter.myViewHolder>{
    ArrayList<HospitalDetailModel>datalist;
    Context context;
    public HospitalDetailAdapter(ArrayList<HospitalDetailModel> datalist,Context context) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new myViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.t1.setText(datalist.get(position).getDoctor_Name());
        holder.t2.setText(datalist.get(position).getSpeciality_doctor());
        holder.D_id.setText(datalist.get(position).getId());
        holder.D_phone.setText(datalist.get(position).getDoctor_phone());
        if(datalist.get(position).getTb())
        {
            holder.tb.setText("Avaialble");
        }
        else
        {
            holder.tb.setText("Not Available");
        }
        Glide.with(holder.itemView.getContext()).load(datalist.get(position).getDoctor_image()).into(holder.img);
    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView t1,t2,D_id,D_phone;
        TextView tb;
        ImageButton imageButton;
        ImageView img;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.Dn);
            D_phone = itemView.findViewById(R.id.DNP);
            t2 = itemView.findViewById(R.id.Speciality_doctor);
            img = itemView.findViewById(R.id.doctor_image_fetched);
            D_id=itemView.findViewById(R.id.F_id);
            imageButton = itemView.findViewById(R.id.imageButtoncardView);
            imageButton.setOnClickListener(this);
            tb=itemView.findViewById(R.id.txt_avail);

        }

        @Override
        public void onClick(View v) {
            showPopMenu(v);
        }
        private void showPopMenu (View v)
        {
            if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                imageButton.setVisibility(View.VISIBLE);
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.editdoctordetails);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }
            else
                imageButton.setVisibility(View.INVISIBLE);


        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId())
            {

                case R.id.edit_doctor:
                    context.startActivity(new Intent(context, DoctorDetailsedit.class).putExtra("name",t1.getText().toString()));
                    break;

                case R.id.delete_doctor:
                        new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("Doctor_Details").document(t1.getText().toString())
                                        .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Deleting Doctor..", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, Hospital_details.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                        break;
            }
            return false;
        }
    }
}