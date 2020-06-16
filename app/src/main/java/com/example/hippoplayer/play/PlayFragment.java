package com.example.hippoplayer.play;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hippoplayer.play.notification.OnClearFromRecentService;
import com.example.hippoplayer.play.notification.SongNotificationManager;
import com.example.hippoplayer.databinding.FragmentPlayBinding;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.utils.Constants;
import com.example.hippoplayer.utils.ConvertHelper;
import com.example.hippoplayer.utils.PathHelper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.nio.file.Path;
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
    private MediaManager mMediaManager;
    private List<Song> mSong = new ArrayList<>();
    private PlayViewModel mViewModel;

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
            // create manager
            mMediaManager.setSongs(mSong);
            setSong();
            // this will init the singleton class notification manager
            SongNotificationManager.getInstance().init(getContext(), mSong);
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
        mMediaManager = new MediaManager(getContext());
        mViewModel = new ViewModelProvider(this).get(PlayViewModel.class);
        mViewModel.setContext(getContext());
        mViewModel.getmSongResponeFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response);
        initListener();
        initHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().registerReceiver(mMediaManager.broadcastReceiver, new IntentFilter(Constants.TRACK_CODE));
            Intent clearService = new Intent(getActivity().getBaseContext(), OnClearFromRecentService.class);
            getActivity().startService(clearService);
        }
    }

    private void initListener() {
        fragmentPlayBinding.buttonPlayAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaManager.pauseButtonClicked();
            }
        });

        fragmentPlayBinding.sbDurationSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                   mMediaManager.getPlayer().seekTo(progress * 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMediaManager.posLiveData.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                fragmentPlayBinding.vpPlay.setCurrentItem(integer);
            }
        });

        fragmentPlayBinding.vpPlay.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                playCurrentSong(position);
                updateTime(0, 0);
                updateSeekBar(0, 0);
            }
        });
    }

    private void playCurrentSong(int position) {
        mMediaManager.play(position);
    }

    private void initHandler() {
        Handler mHandler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaManager.getService().getMediaPlayer().isPlaying()) {
                    int currentPosition = mMediaManager.getPlayer().getCurrentPosition();
                    int maxDuration =  mMediaManager.getPlayer().getDuration();
                    updateSeekBar(currentPosition, maxDuration);
                    updateTime(currentPosition, maxDuration);
                }
                // if the song is loaded
                // and the current position is lower than duration with 1s
                if (mMediaManager.isSongCompleted()) {
                    playNextSong();
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }


    private void playNextSong() {
        mMediaManager.onNext();
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

    // Todo: inner classes + interfaces

    @Override
    public void onStart() {
        super.onStart();
        mMediaManager.getService().requestAudioFocus(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_ID);
        getActivity().unregisterReceiver(mMediaManager.broadcastReceiver);
    }

}
