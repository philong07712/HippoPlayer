package com.example.hippoplayer.play;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.R;
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

    private View view;
    private FloatingActionButton btnPause;
    private SeekBar seekBarDuration;
    // Todo: Fields
    private MediaService mediaService = new MediaService();
    private List<Song> mSong = new ArrayList<>();

    private PlayViewModel mViewModel;

    // media service
    Intent mediaIntent;

    private Subscriber<List<SongResponse>> response = new Subscriber<List<SongResponse>>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<SongResponse> songResponses) {
            for(SongResponse songResponse : songResponses){
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false);
        fragmentPlayBinding.setLifecycleOwner(this);
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
//        initListener();
//        initHandler();
        // This will be testing mediaPlayer Service
    }

    @Override
    public void onStart() {
        super.onStart();
        mediaService.requestAudioFocus(getContext());
    }

    // Todo: public method

    // Todo: private method



    private void initListener() {
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaService.pauseButtonClicked();
            }
        });

        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                   mediaService.seekTo(progress * 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void initHandler() {
        Handler mHandler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaService.getMediaPlayer().isPlaying()) {
                    int currentPosition = mediaService.getMediaPlayer().getCurrentPosition();
                    int maxDuration = mediaService.getMediaPlayer().getDuration();
                    updateSeekBar(currentPosition, maxDuration);
                    updateTime(currentPosition, maxDuration);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void updateSeekBar(int currentPosition, int maxDuration) {
        seekBarDuration.setMax(maxDuration / 100);
        int mCurrentPosition = currentPosition / 100;
        seekBarDuration.setProgress(mCurrentPosition);
    }

    private void updateTime(int currentPosition, int maxDuration) {
//        tvStartSong.setText(ConvertHelper.convertToMinutes(currentPosition));
//        tvEndSong.setText(ConvertHelper.convertToMinutes(maxDuration));
    }


    // Todo: inner classes + interfaces
}
