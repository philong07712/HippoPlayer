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
import com.example.hippoplayer.search.event.ItemEvent;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<Artist> artists = new ArrayList<>();

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
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
        Artist artist = artists.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutSearchBinding itemLayoutSearchBinding;
        public ViewHolder(@NonNull ItemLayoutSearchBinding itemLayoutSearchBinding) {
            super(itemLayoutSearchBinding.getRoot());
            this.itemLayoutSearchBinding = itemLayoutSearchBinding;
        }
        public void bind(Artist artist){
            itemLayoutSearchBinding.setArtist(artist);
            itemLayoutSearchBinding.executePendingBindings();
            ItemEvent itemEvent = new ItemEvent();
            itemLayoutSearchBinding.setItemEvents(itemEvent);
        }
    }
}
