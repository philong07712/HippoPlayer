package com.example.hippoplayer.play;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class PlayFragment extends Fragment {

    private static final String TAG = PlayFragment.class.getSimpleName();

    private PlayViewModel mViewModel;

    public static PlayFragment newInstance() {
        return new PlayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_play, container, false);
     }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlayViewModel.class);
        mViewModel.init();

        Observer<List<Song>> list = new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                Log.d(TAG, songs.get(0).getArtist());
                Log.d(TAG, songs.get(0).getDescription());
                Log.d(TAG, songs.get(0).getName());
                Log.d(TAG, songs.get(0).getThumbnail());
                Log.d(TAG, songs.get(0).getUrl());
            }
        };

        mViewModel.getSongsLiveData().observe(getViewLifecycleOwner(), list);

        // TODO: Use the ViewModel
    }

}
