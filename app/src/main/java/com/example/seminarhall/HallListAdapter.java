package com.example.seminarhall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HallListAdapter extends RecyclerView.Adapter<HallListAdapter.HallListViewHolder> implements Filterable{


    private List<Hall> halls;
    private List<Hall> hallListFull;
    private LayoutInflater mInflater;


    HallListAdapter(Activity context, ArrayList<Hall> halls) {
        if (halls == null) {
            this.halls = new ArrayList<>();
        }
        else {
            this.halls = halls;
        }

        this.hallListFull = new ArrayList<>(this.halls);
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

    @Override
    public Filter getFilter() {

        return exampleFilter;
    }
    private  Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Hall> filteredList = new ArrayList<>();
            FilterResults results=new FilterResults();
            if (halls.isEmpty()) {
                results.values=filteredList;
                return results;
            }
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(hallListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Hall hall : hallListFull) {
                    int hint = Integer.parseInt(filterPattern);
                    int temp=hall.getSize();
                    if (temp>=hint) {
                        filteredList.add(hall);
                    }
                }
            }
            results.values=filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            halls.clear();
            halls.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
