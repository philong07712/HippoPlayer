package com.example.hippoplayer.play;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hippoplayer.databinding.FragmentPlayBinding;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.utils.ConvertHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private FragmentPlayBinding fragmentPlayBinding;
    // Todo: Fields
    private MediaService mMediaService = new MediaService();
    public static final List<Song> mSong = new ArrayList<>();

    private PlayViewModel mViewModel;

    public List<Song> getmSong() {
        return mSong;
    }

    private Subscriber<List<SongResponse>> response = new Subscriber<List<SongResponse>>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<SongResponse> songResponses) {
            for (SongResponse songResponse : songResponses) {
                Song song = new Song();
                song.setSongResponse(songResponse);
                mSong.add(song);
            }
            setSong();
        }

        @Override
        public void onError(Throwable t) {
            Log.e("SongResponse Fragment", t.getMessage());
        }

        @Override
        public void onComplete() {
            Log.e("onComplete", "Complete");
        }
    };

    private void setSong() {
        Log.d("TAG", Integer.toString(mSong.size()));
        ItemPlayAdapter itemPlayAdapter = new ItemPlayAdapter();
        itemPlayAdapter.setmSongList(mSong);
        fragmentPlayBinding.vpPlay.setAdapter(itemPlayAdapter);
        fragmentPlayBinding.vpPlay.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    // Todo: public method

    // Todo: private method

    //
    private void initListener() {
        fragmentPlayBinding.sbDurationSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaService.seekTo(progress * 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        fragmentPlayBinding.vpPlay.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "current viewpager position is: " + position);
                playCurrentSong(position);
            }
        });
    }

    private void playCurrentSong(int position) {
        String fullFileUrl = mViewModel.getFullUrl(mSong.get(position).getUrl());
        mMediaService.setMediaFile(fullFileUrl);
        mMediaService.loadMediaSource();
        mMediaService.playMedia();
        setButtonToPlayStatus();
    }

    private void initHandler() {
        Handler mHandler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaService.getMediaPlayer().isPlaying()) {
                    int currentPosition = mMediaService.getMediaPlayer().getCurrentPosition();
                    int maxDuration = mMediaService.getMediaPlayer().getDuration();
                    updateSeekBar(currentPosition, maxDuration);
                    updateTime(currentPosition, maxDuration);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void updateSeekBar(int currentPosition, int maxDuration) {
        fragmentPlayBinding.sbDurationSong.setMax(maxDuration / 100);
        int mCurrentPosition = currentPosition / 100;
        fragmentPlayBinding.sbDurationSong.setProgress(mCurrentPosition);
    }

    private void updateTime(int currentPosition, int maxDuration) {
        fragmentPlayBinding.textTimeStartSong.setText(ConvertHelper.convertToMinutes(currentPosition));
        fragmentPlayBinding.textTimeEndSong.setText(ConvertHelper.convertToMinutes(maxDuration));
    }

    private void setButtonToPlayStatus(){
        fragmentPlayBinding.buttonPlayAndPause.cancelAnimation();
        fragmentPlayBinding.buttonPlayAndPause.setProgress(0.0f);
    }

    private void setButtonToPauseStatus(){
        fragmentPlayBinding.buttonPlayAndPause.playAnimation();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false);
        fragmentPlayBinding.setLifecycleOwner(this);
        // Set up button play and pause
        fragmentPlayBinding.buttonPlayAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaService.getMediaPlayer().isPlaying()) { // if true // pause song
                    mMediaService.pauseButtonClicked();
                    setButtonToPauseStatus();
                } else { // if false // resume play song
                    mMediaService.resumeMedia();
                    setButtonToPlayStatus();
                }
            }
        });

        initListener();
        initHandler();
        return fragmentPlayBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlayViewModel.class);
        mViewModel.setContext(getContext());
        mViewModel.getmSongResponeFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response);
        // This will be testing mediaPlayer Service
    }


    @Override
    public void onStart() {
        super.onStart();
        mMediaService.requestAudioFocus(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG PLAY", "PAUSE");
    }

    // Todo: inner classes + interfaces
}
