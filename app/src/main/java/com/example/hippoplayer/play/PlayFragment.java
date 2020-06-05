package com.example.hippoplayer.play;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hippoplayer.databinding.FragmentPlayBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;

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
    private TextView tvTitle, tvArtist;
    private ImageView imgBg, imgSong;

    // Todo: Fields
    private String mTitle, mArtist, mThumbnail, mMediaUrl;
    private List<Song> mSong = new ArrayList<>();

    private PlayViewModel mViewModel;

    private boolean isPlay = false; // variable for button play and pause
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
        fragmentPlayBinding.buttonPlayAndPause.setProgress(1.0f);
        fragmentPlayBinding.buttonPlayAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlay){ // if true // pause song
                    isPlay = !isPlay;
                    fragmentPlayBinding.buttonPlayAndPause.playAnimation();
                } else { // if false // resume play song
                    isPlay = !isPlay;
                    fragmentPlayBinding.buttonPlayAndPause.cancelAnimation();
                    fragmentPlayBinding.buttonPlayAndPause.setProgress(0.0f);
                }
            }
        });
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
    }

    /*@Override
    public void onStart() {
        super.onStart();
        mediaIntent = new Intent(getActivity(), MediaPlayerService.class);
       // getActivity().bindService(mediaIntent, mViewModel.getMusicConnection(), Context.BIND_AUTO_CREATE);
        getActivity().startService(mediaIntent);
    }*/

    private void playSong() {
        mViewModel.setMediaSong(mMediaUrl);
        mViewModel.playMediaSong();
    }
}
