package com.example.hippoplayer.list;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.FragmentListBinding;
import com.example.hippoplayer.list.events.ItemEvent;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListFragment extends Fragment {

    private ListViewModel mViewModel;
    private FragmentListBinding fragmentListBinding;
    private List<Song> mSong = new ArrayList<>();
    private RecyclerView recyclerView;

    private ItemEvent buttonEvent;

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
            Log.e("SongResponse List", t.getMessage());
        }

        @Override
        public void onComplete() {
            Log.e("onComplete List", "Complete List");
        }
    };

    private void setSong() {
        Log.d("TAG", Integer.toString(mSong.size()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ListSongAdapter listSongAdapter = new ListSongAdapter();
        listSongAdapter.setmSongList(mSong);
        recyclerView = fragmentListBinding.rvList;
        recyclerView.setAdapter(listSongAdapter);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                CardView cardView = viewHolder.itemView.findViewById(R.id.cardview_item_list);
                cardView.animate().setDuration(200).scaleX(1f).scaleY(1f).setInterpolator(new OvershootInterpolator()).start();
            }
        }, 100);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(layoutManager);
                int pos = layoutManager.getPosition(view);

                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(pos);
                CardView cardView = viewHolder.itemView.findViewById(R.id.cardview_item_list);

                if(newState == RecyclerView.SCROLL_STATE_IDLE){

                    cardView.animate().setDuration(200).scaleX(1f).scaleY(1f).setInterpolator(new OvershootInterpolator()).start();

                }else {

                    cardView.animate().setDuration(200).scaleX(0.95f).scaleY(0.95f).setInterpolator(new OvershootInterpolator()).start();

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentListBinding = FragmentListBinding.inflate(inflater, container, false);
        fragmentListBinding.setLifecycleOwner(this);
        return fragmentListBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        mViewModel.setContext(getContext());
        mViewModel.getmSongResponeFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response);
    }

}