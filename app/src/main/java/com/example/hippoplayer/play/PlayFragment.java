package com.example.hippoplayer.play;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.FragmentPlayBinding;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.play.notification.OnClearFromRecentService;
import com.example.hippoplayer.play.notification.SongNotificationManager;
import com.example.hippoplayer.utils.Constants;
import com.example.hippoplayer.utils.ConvertHelper;
import com.example.hippoplayer.utils.PathHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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
    private static final float PAUSE_LOTTIE_SPEED = 3.0f;
    Handler mHandler;
    // View
    private FragmentPlayBinding fragmentPlayBinding;
    private SlidingUpPanelLayout panelLayout;

    // Todo: Fields
    private MediaManager mMediaManager;
    private List<Song> mSong = new ArrayList<>();
    private PlayViewModel mViewModel;
    private int FLAG_PAGE = -1;
    private ViewPager2PageChangeCallBack pager2PageChangeCallBack;
    private Subscriber<List<SongResponse>> response = new Subscriber<List<SongResponse>>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<SongResponse> songResponses) {
            mSong = new ArrayList<>();
            for (SongResponse songResponse : songResponses) {
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
        panelLayout = getActivity().findViewById(R.id.sliding_up_panel_main);
        panelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d(TAG, "onPanelSlide: " + slideOffset);
                fragmentPlayBinding.miniContainerController.setAlpha(1f - slideOffset);
                // set alpha of the fullscreen
                fragmentPlayBinding.vpPlay.setAlpha(slideOffset);
                fragmentPlayBinding.containerController.setAlpha(slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.d(TAG, "onPanelStateChanged: Previous " + previousState.name());
                Log.d(TAG, "onPanelStateChanged: New " + newState.name());
            }
        });

        fragmentPlayBinding.buttonPlayAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaManager.pauseButtonClicked();
            }
        });

        fragmentPlayBinding.btnPlayAndPauseController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaManager.pauseButtonClicked();
            }
        });

        mMediaManager.stateLiveData.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // if the song isPlaying, then we will display pause
                if (aBoolean) {
                    // play->pause
                    fragmentPlayBinding.buttonPlayAndPause.setSpeed(-PAUSE_LOTTIE_SPEED);
                    fragmentPlayBinding.buttonPlayAndPause.playAnimation();
                    fragmentPlayBinding.btnPlayAndPauseController.setBackgroundResource(R.drawable.ic_baseline_pause_24_orange);
                } else {
                    // pause->play
                    fragmentPlayBinding.buttonPlayAndPause.setSpeed(PAUSE_LOTTIE_SPEED);
                    fragmentPlayBinding.buttonPlayAndPause.playAnimation();
                    fragmentPlayBinding.btnPlayAndPauseController.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24_orange);
                }
            }
        });

        // set next and previous button
        fragmentPlayBinding.buttonBackwardSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaManager.onPrevious();
            }
        });

        fragmentPlayBinding.buttonNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaManager.onNext();
            }
        });

        fragmentPlayBinding.btnSkipNextController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaManager.onNext();
            }
        });
        // set repeat button
        fragmentPlayBinding.buttonRepeatSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update flag
                mMediaManager.updateStateFlag();
                switch (mMediaManager.getStateFlag()) {
                    // update the status that the app is currently not repeat the song
                    case MediaManager.STATE_NON_REPEAT:
                        fragmentPlayBinding.buttonRepeatSong.setImageResource(R.drawable.ic_icon_feather_repeat_white);
                        break;
                    // update the status that the app is currently repeat the current song
                    case MediaManager.STATE_REPEAT_ONE:
                        fragmentPlayBinding.buttonRepeatSong.setImageResource(R.drawable.ic_baseline_repeat_one_24_orange);
                        break;
                    // update the status that the app is currently shuffle the song
                    case MediaManager.STATE_SHUFFLE:
                        fragmentPlayBinding.buttonRepeatSong.setImageResource(R.drawable.ic_icon_open_random_orange);
                        break;
                    // update the status that the app is currently repeat the song list
                    case MediaManager.STATE_REPEAT:
                        fragmentPlayBinding.buttonRepeatSong.setImageResource(R.drawable.ic_icon_feather_repeat_orange);
                        break;
                }
                // if the repeat do not choose
                // if the repeat has been choosen
            }
        });
        fragmentPlayBinding.sbDurationSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaManager.seekTo(progress * 100);
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
//                fragmentPlayBinding.vpPlay.setCurrentItem(integer);
                pager2PageChangeCallBack.onPageSelected(integer);
            }
        });
        // create pager 2 listener
        pager2PageChangeCallBack = new ViewPager2PageChangeCallBack();
        fragmentPlayBinding.vpPlay.registerOnPageChangeCallback(pager2PageChangeCallBack);

    }

    private void playCurrentSong(int position) {
        mMediaManager.play(position);
    }

    private void initHandler() {
        mHandler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaManager.isPlaying()) {
                    long currentPosition = mMediaManager.getCurrentPosition();
                    long maxDuration = mMediaManager.getDuration();
                    updateSeekBar(currentPosition, maxDuration);
                    updateTime(currentPosition, maxDuration);
                }
                // if the song is loaded
                // and the current position is lower than duration with 1s
                if (mMediaManager.isSongCompleted()) {
                    mMediaManager.playNextSong();
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void updateSeekBar(long currentPosition, long maxDuration) {
        fragmentPlayBinding.sbDurationSong.setMax((int) maxDuration / 100);
        int mCurrentPosition = (int) currentPosition / 100;
        fragmentPlayBinding.sbDurationSong.setProgress(mCurrentPosition);
    }

    private void updateTime(long currentPosition, long maxDuration) {
        fragmentPlayBinding.textTimeStartSong.setText(ConvertHelper.convertToMinutes(currentPosition));
        if (maxDuration >= 0)
            fragmentPlayBinding.textTimeEndSong.setText(ConvertHelper.convertToMinutes(maxDuration));
    }

    // Todo: inner classes + interfaces

    private void setSong() {
        Log.d("TAG", Integer.toString(mSong.size()));
        ItemPlayAdapter itemPlayAdapter = new ItemPlayAdapter();
        itemPlayAdapter.setmSongList(mSong);
        fragmentPlayBinding.vpPlay.setAdapter(itemPlayAdapter);
        fragmentPlayBinding.vpPlay.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaManager.getService().requestAudioFocus(getContext());
        // load current position song without scroll style
        fragmentPlayBinding.vpPlay.setCurrentItem(FLAG_PAGE, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the loop to update times and seekbar
        mHandler.removeCallbacksAndMessages(null);
        fragmentPlayBinding.vpPlay.unregisterOnPageChangeCallback(pager2PageChangeCallBack);
        mMediaManager.deleteNotification();
        getActivity().unregisterReceiver(mMediaManager.broadcastReceiver);
        mMediaManager.getService().releasePlayer();
        mMediaManager.getService().removeAudioFocus();
    }

    class ViewPager2PageChangeCallBack extends ViewPager2.OnPageChangeCallback {

        public ViewPager2PageChangeCallBack() {
            super();
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (position == FLAG_PAGE) {
                return;
            }
            playCurrentSong(position);
            FLAG_PAGE = position;

            fragmentPlayBinding.vpPlay.setCurrentItem(FLAG_PAGE, false);
            // we will set the tittle and artist name to current song
            fragmentPlayBinding.tvTitleController.setText(mSong.get(position).getNameSong());
            fragmentPlayBinding.tvArtistController.setText(mSong.get(position).getNameArtist());
            Log.d(TAG, "onPageSelected: " + position);
            updateController(mSong.get(position));
            updateTime(0, 0);
            updateSeekBar(0, 0);
        }

        private void updateController(Song song) {
            fragmentPlayBinding.tvTitleController.setText(song.getNameSong());
            fragmentPlayBinding.tvArtistController.setText(song.getNameArtist());
            String url = song.getThumbnail();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(fragmentPlayBinding.imgThumbnailController);
        }
    }
}
