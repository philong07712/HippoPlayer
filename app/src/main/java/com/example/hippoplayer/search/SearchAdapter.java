package com.example.hippoplayer.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.ItemLayoutSearchBinding;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.Song;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList arrayList = new ArrayList<>();
    private int index = 0;
    private final static int ARTIST = 0;
    private final static int SONG = 1;

    public void setData(ArrayList arrayList, int index) {
        this.index = index;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemLayoutSearchBinding itemLayoutSearchBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_layout_search, parent, false);
        return new ViewHolder(itemLayoutSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (index){
            case ARTIST:{
                Artist artist = (Artist) arrayList.get(position);
                holder.bind(artist);
                break;
            }
            case SONG: {
                Song song = (Song) arrayList.get(position);
                holder.bind(song);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutSearchBinding itemLayoutSearchBinding;
        public ViewHolder(@NonNull ItemLayoutSearchBinding itemLayoutSearchBinding) {
            super(itemLayoutSearchBinding.getRoot());
            this.itemLayoutSearchBinding = itemLayoutSearchBinding;
        }

        public void bind(Object object){
            switch (index){
                case 0:{
                    itemLayoutSearchBinding.setArtist((Artist) object);
                    break;
                }
                case 1: {
                    itemLayoutSearchBinding.setSong((Song) object);
                    break;
                }
            }
            itemLayoutSearchBinding.executePendingBindings();
        }
    }
}
