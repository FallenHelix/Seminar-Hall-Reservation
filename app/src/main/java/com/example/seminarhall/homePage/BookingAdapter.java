package com.example.seminarhall.homePage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminarhall.Hall;
import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.example.seminarhall.dataBase.HallListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    List<ReservedHall> rList;

    public BookingAdapter(List<ReservedHall> list) {
        rList=list;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new BookingAdapter.BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
//        holder.hallName.setText(rList.get(position).getPurpose());
//        holder.hallSize.setText(rList.get(position).getUserId());
        holder.hallName.setText(rList.get(position).getPurpose());
        holder.hallSize.setText(rList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return rList.size();

    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView hallName;
        TextView hallSize;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            hallName = (TextView) itemView.findViewById(R.id.textView);
            hallSize = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
