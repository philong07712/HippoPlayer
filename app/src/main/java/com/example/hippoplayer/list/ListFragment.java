package com.example.hippoplayer.list;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.FragmentListBinding;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.play.ItemPlayAdapter;
import com.example.hippoplayer.play.PlayViewModel;
import com.example.hippoplayer.search.SearchFragment;

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