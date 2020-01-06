package com.example.seminarhall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HallListAdapter extends RecyclerView.Adapter<HallListAdapter.HallListViewHolder> {


//    List<Hall> halls;
    String []data;
    int count=1;
//    public HallListAdapter(List<Hall> halls) {
//        this.halls=halls;
//    }

    HallListAdapter(String[] data) {
        this.data=data;
    }

    @NonNull
    @Override
    public HallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_layout, parent, false);
        return new HallListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallListViewHolder holder, int position) {
//        Hall currHall = halls.get(position);
//        holder.hallName.setText(currHall.getName());
//        holder.hallSize.setText(currHall.getSize());

        String temp = data[position];
        holder.hallSize.setText("1");
        holder.hallName.setText(temp);
        //count++;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class HallListViewHolder extends RecyclerView.ViewHolder {
        TextView hallName;
        TextView hallSize;

        public HallListViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView hallName = (TextView) itemView.findViewById(R.id.view1);
            TextView hallSize = (TextView) itemView.findViewById(R.id.view2);
        }

    }
}
