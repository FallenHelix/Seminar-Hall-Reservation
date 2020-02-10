package com.example.seminarhall;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private Context mContext;
    private ItemClickListener mClickListener;
    private int selected;

    public HorizontalAdapter(Context context, ArrayList<String> mDays, ArrayList<String> mDates) {
        this.mDays = mDays;
        this.mDates = mDates;
        this.mContext = context;
        selected=-1;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list , parent, false);
        return new ViewHolder(view);

    }
    void setSelected(int position)
    {
        this.selected=position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.day.setText(mDates.get(position));
        holder.date.setText(mDays.get(position));

        int color;
        if (selected == position) {
            holder.date.setTextColor(Color.parseColor("#FFFFFF"));
            holder.day.setTextColor(Color.parseColor("#FFFFFF"));
            color = ContextCompat.getColor(mContext, R.color.main_red);
            holder.relativeLayout.setBackgroundColor(color);
            holder.date.setBackgroundColor(color);
            holder.day.setBackgroundColor(color);
        }
        else
        {
            color = ContextCompat.getColor(mContext, R.color.main_red);
            holder.day.setTextColor(color);
//            holder.day.setTextColor(Color.parseColor("#000000"));
            color = ContextCompat.getColor(mContext, R.color.gray);
            holder.date.setTextColor(color);
            holder.date.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.day.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView day,date;
        RelativeLayout relativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.TextDay);
            date = itemView.findViewById(R.id.TextDate);
            relativeLayout = new RelativeLayout(itemView.getContext());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
                Log.d("Onclick", "Onclick view view");
        }
    }
    void setClickListener(HorizontalAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
