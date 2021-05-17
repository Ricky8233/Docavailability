package com.example.android.docavailability.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.docavailability.Model.HospitalDetailModel;
import com.example.android.docavailability.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class HospitalDetailAdapter extends RecyclerView.Adapter<HospitalDetailAdapter.myViewHolder>{
    ArrayList<HospitalDetailModel>datalist;

    public HospitalDetailAdapter(ArrayList<HospitalDetailModel> datalist) {
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
        //holder.s.setChecked(datalist.get(position).getSwitch_availability() == 1);
    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            //s=itemView.findViewById(R.id.switch_availability);
            t1 = itemView.findViewById(R.id.Dn);
            t2 = itemView.findViewById(R.id.Speciality_doctor);

        }
    }
}