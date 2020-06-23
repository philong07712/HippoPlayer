package com.example.hippoplayer.offline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.ItemLayoutOfflineBinding;
import com.example.hippoplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

public class OfflineSongAdapter extends RecyclerView.Adapter<OfflineSongAdapter.OfflineViewHolder> {

    private List<Song> mSongList = new ArrayList<>();

    public OfflineSongAdapter(List<Song> mSongList) {
        this.mSongList = mSongList;
    }

    @NonNull
    @Override
    public OfflineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemLayoutOfflineBinding itemLayoutOfflineBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_layout_offline, parent, false);
        return new OfflineViewHolder(itemLayoutOfflineBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineViewHolder holder, int position) {
        Song song = mSongList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    class OfflineViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutOfflineBinding itemLayoutOfflineBinding;
        public OfflineViewHolder(@NonNull ItemLayoutOfflineBinding itemLayoutOfflineBinding) {
            super(itemLayoutOfflineBinding.getRoot());
            this.itemLayoutOfflineBinding = itemLayoutOfflineBinding;
        }

        public void bind(Song item) {
            itemLayoutOfflineBinding.setSong(item);
            itemLayoutOfflineBinding.executePendingBindings();
        }
    }
}
