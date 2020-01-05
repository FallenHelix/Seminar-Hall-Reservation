package com.example.seminarhall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class  RecyclerView_Config {
    private Context mContext;
    private HallAdapater mHallAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Hall> hall, List<String> keys) {
        mContext=context;
        mHallAdapter = new HallAdapater(hall, keys);
//        mHallAdapter = new HallAdapater(halls, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mHallAdapter);

    }
    class HallItemView extends RecyclerView.ViewHolder{


        private TextView mName;
        private TextView Capacity;

        private String key;

        public HallItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.hall_list_item, parent, false));
            mName = (TextView) itemView.findViewById(R.id.Hall_Name);
            Capacity = (TextView) itemView.findViewById(R.id.cap);
        }


        public void bind(Hall hall, String key) {
            mName.setText(hall.getName());
            Capacity.setText(hall.getSize());
            this.key=key    ;
        }

    }


    class HallAdapater extends RecyclerView.Adapter<HallItemView> {
        private List<Hall> mHallList;
        private List<String> mkeys;

        public HallAdapater(List<Hall> mHallList, List<String> keys) {
            this.mHallList = mHallList;
            this.mkeys = keys;
        }

//        public HallAdapater() {
//            super();
//        }

        @NonNull
        @Override
        public HallItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HallItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull HallItemView holder, int position) {
            holder.bind(mHallList.get(position), mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mHallList.size();
        }
    }
}
