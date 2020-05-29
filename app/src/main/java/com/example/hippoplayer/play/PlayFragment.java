package com.example.hippoplayer.play;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;

import java.util.List;

public class PlayFragment extends Fragment {

    private static final String TAG = PlayFragment.class.getSimpleName();

    // View
    private TextView tvTitle, tvArtist;
    private ImageView imgBg, imgSong;
    private String title, artist, thumbnail;
    private View view;

    private PlayViewModel mViewModel;

    public static PlayFragment newInstance() {
        return new PlayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_play, container, false);
        initView(view);
        return view;
     }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlayViewModel.class);

        mViewModel.init();

        Observer<List<Song>> list = new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                title = songs.get(0).getName();
                artist = songs.get(0).getArtist();
                thumbnail = songs.get(0).getThumbnail();
                // This will get the full url for the song

                thumbnail = mViewModel.getFullUrl(thumbnail);
                updateUi();
            }
        };
        mViewModel.getSongsLiveData().observe(getViewLifecycleOwner(), list);
    }

    private void updateUi() {
        tvArtist.setText(artist);
        tvTitle.setText(title);
        Glide.with(this)
                .load(thumbnail)
                .into(imgBg)
                ;
        Glide.with(this)
                .load(thumbnail)
                .into(imgSong)
                ;

    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.text_title_song);
        tvArtist = view.findViewById(R.id.text_artist_song);
        imgBg = view.findViewById(R.id.image_bg_play);
        imgSong = view.findViewById(R.id.image_song);
    }
}
