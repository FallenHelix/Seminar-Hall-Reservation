package com.example.seminarhall.homePage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {
    private static final String TAG = "ReceiptAdapter";

    private List<ReservedHall> reserveList;
    public   ItemClickListener itemClickListener;

    public ReceiptAdapter(List<ReservedHall> halls) {
        reserveList=halls;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_list, parent, false);
        return new ReceiptAdapter.ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        holder.Hallid.setText(getHallName(reserveList.get(position).getHallId()));
        holder.sDate.append(reserveList.get(position).getStartDate().toString());
        holder.eDate.append(reserveList.get(position).getEndDate().toString());
        holder.purpose.append(reserveList.get(position).getPurpose());

    }

    private String getHallName(String hallId) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Halls");
        databaseReference.equalTo(hallId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Log.d(TAG, "onDataChange: "+dataSnapshot.getValue().toString());

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return "1";
    }

    @Override
    public int getItemCount() {
        return reserveList.size();
    }

    public void setListener(ItemClickListener itemClickListener) {
        this.itemClickListener=itemClickListener;
    }


    public  class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView Hallid,sDate,eDate,purpose;



        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            Hallid = (TextView) itemView.findViewById(R.id.BDate);
            sDate = (TextView) itemView.findViewById(R.id.startDate);
            eDate    = (TextView) itemView.findViewById(R.id.endDate);
            purpose = (TextView) itemView.findViewById(R.id.TextViewPurpose);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
