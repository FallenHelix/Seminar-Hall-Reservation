package com.example.seminarhall;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class artistList extends ArrayAdapter<Artist> {
    private Activity context;
    private List<Artist> artistList;

    public artistList(Activity context, List<Artist> artistList) {
        super(context,R.layout.list_layout,artistList);
        this.context=context;
        this.artistList=artistList;
    }

    @NonNull
    @Override
    public View getView(int position, View ConverView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView Name = (TextView) listViewItem.findViewById(R.id.view1);
        TextView Genre= (TextView) listViewItem.findViewById(R.id.view2);
        Artist artist = artistList.get(position);
        Name.setText(artist.getArtistName());
        Genre.setText(artist.getArtistGenre());
        return  listViewItem;
    }

}
