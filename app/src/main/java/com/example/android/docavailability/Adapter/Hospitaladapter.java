package com.example.android.docavailability.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.docavailability.DoctorDetailsedit;
import com.example.android.docavailability.Doctor_details;
import com.example.android.docavailability.Hospital_details;
import com.example.android.docavailability.Model.Hospitalmodel;
import com.example.android.docavailability.R;
import com.example.android.docavailability.check2;

import java.util.ArrayList;

public class Hospitaladapter extends RecyclerView.Adapter<Hospitaladapter.myViewHolder>{
    ArrayList<Hospitalmodel>datalist;
    Context context;
    public Hospitaladapter(ArrayList<Hospitalmodel> datalist, Context context) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_home,parent,false);
        return new myViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.t1.setText(datalist.get(position).getHospital_Name());
        holder.t2.setText(datalist.get(position).getHospital_email_id());
        holder.t3.setText(datalist.get(position).getHospital_phone_number());
        holder.icu_beds.setText(datalist.get(position).getIcu_beds());
        Glide.with(holder.itemView.getContext()).load(datalist.get(position).getHospital_image()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, check2.class).putExtra("uid",datalist.get(position).getUid()));
            }
        });
    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2,icu_beds;
        TextView t3;
        ImageView img;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.check_Hn);
            t2 = itemView.findViewById(R.id.check_email_hospital);
            t3 = itemView.findViewById(R.id.check_Phone_number);
            img = itemView.findViewById(R.id.check_hospital_image_fetched);
            icu_beds=itemView.findViewById(R.id.check_icu_beds);
        }
    }
}