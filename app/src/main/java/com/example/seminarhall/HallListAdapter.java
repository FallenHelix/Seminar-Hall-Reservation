package com.example.seminarhall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HallListAdapter extends RecyclerView.Adapter<HallListAdapter.HallListViewHolder> {


    List<Hall> halls;
    private LayoutInflater mInflater;


    HallListAdapter(Activity context, ArrayList<Hall> halls) {
        this.halls=halls;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_layout, parent, false);
        return new HallListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallListViewHolder holder, int position) {
//        Hall currHall = halls.get(position);
//        holder.hallName.setText(currHall.getName());
//        holder.hallSize.setText(currHall.getSize());

        Hall temp = halls.get(position);
        String hName=temp.getName();
        int size=temp.getSize();
        holder.hallSize.setText(String.valueOf(size));
        holder.hallName.setText(hName);
    }

    @Override
    public int getItemCount() {
        return halls.size();
    }

    public class HallListViewHolder extends RecyclerView.ViewHolder {
        TextView hallName;
        TextView hallSize;

         HallListViewHolder(@NonNull View itemView) {
            super(itemView);
            hallName = (TextView) itemView.findViewById(R.id.view1);
            hallSize = (TextView) itemView.findViewById(R.id.view2);
        }

    }
}
