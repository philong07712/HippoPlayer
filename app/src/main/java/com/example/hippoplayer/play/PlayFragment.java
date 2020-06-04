package com.example.hippoplayer.play;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.lifecycle.ViewModelProviders;

import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongRespone;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class PlayFragment extends Fragment {
    // Todo: Constant
    private static final String TAG = PlayFragment.class.getSimpleName();

    // View
    private TextView tvTitle, tvArtist;
    private ImageView imgBg, imgSong;
    private View view;

    // Todo: Fields
    private String mTitle, mArtist, mThumbnail, mMediaUrl;
    private List<Song> mSongs = new ArrayList<>();

    private PlayViewModel mViewModel;

    // media service
    Intent mediaIntent;

    private Subscriber<SongRespone> respone = new Subscriber<SongRespone>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(SongRespone songRespone) {
            mSongs = songRespone.getSongList();
            mTitle = mSongs.get(0).getName();
            mArtist = mSongs.get(0).getArtist();
            mThumbnail = mSongs.get(0).getThumbnail();
            mThumbnail = mViewModel.getFullUrl(mSongs.get(0).getThumbnail());
            mMediaUrl = mViewModel.getFullUrl(mSongs.get(0).getUrl());
            updateUi();
            playSong();
        }

        @Override
        public void onError(Throwable t) {
            Log.d(TAG, "onError called " + t.getMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete called");
        }
    };

    // Todo: Override method
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
        mViewModel = new ViewModelProvider(this).get(PlayViewModel.class);
//        binding.setLifecycleOwner(getViewLifecycleOwner());
//        binding.setPlayViewModel(mViewModel);
        mViewModel = ViewModelProviders.of(this).get(PlayViewModel.class);
        mViewModel.init();
        mViewModel.getSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respone);
        // This will be testing mediaPlayer Service
    }

    @Override
    public void onStart() {
        super.onStart();
        mediaIntent = new Intent(getActivity(), MediaPlayerService.class);
        getActivity().bindService(mediaIntent, mViewModel.getMusicConnection(), Context.BIND_AUTO_CREATE);
        getActivity().startService(mediaIntent);
    }

    // Todo: public method

    // Todo: private method

    private void playSong() {
        mViewModel.setMediaSong(mMediaUrl);
        mViewModel.playMediaSong();
    }

    private void updateUi() {
        tvArtist.setText(mArtist);
        tvTitle.setText(mTitle);
        Glide.with(this)
                .load(mThumbnail)
                .into(imgBg)
                ;
        Glide.with(this)
                .load(mThumbnail)
                .into(imgSong)
                ;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.text_title_song);
        tvArtist = view.findViewById(R.id.text_artist_song);
        imgBg = view.findViewById(R.id.image_bg_song);
        imgSong = view.findViewById(R.id.image_song);
    }

    // Todo: inner classes + interfaces
}
