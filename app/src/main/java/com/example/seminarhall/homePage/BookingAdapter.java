package com.example.seminarhall.homePage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    List<ReservedHall> rList;
    public static itemClick mClickListener;

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
        holder.hallSize.setText(rList.get(position).getStartDate());
    }

    @Override
    public int getItemCount() {
        return rList.size();

    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView hallName;
        TextView hallSize;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            hallName = (TextView) itemView.findViewById(R.id.textView);
            hallSize = (TextView) itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition());
            Log.d("Onclick", "Onclick view view");

        }

    }
    public void setClickListener(itemClick itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface itemClick
    {
        void onItemClick(View view, int position);

    }
}
