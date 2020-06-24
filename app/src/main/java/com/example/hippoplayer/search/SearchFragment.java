package com.example.hippoplayer.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.hippoplayer.databinding.FragmentSearchBinding;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements SearchTitleAdapter.SearchTiltleListener {

    private SearchViewModel mViewModel;

    private ArrayList<Artist> arrayList = new ArrayList<>();
    private ArrayList<Song> mSong = new ArrayList<>();
    private ArrayList allData = new ArrayList();
    private ArrayList<String> arrayListItemSearch = new ArrayList<>();

    private FragmentSearchBinding fragmentSearchBinding;
    private RecyclerView recyclerView;
    private SearchTitleAdapter searchTitleAdapter = new SearchTitleAdapter(this);
    private  SearchAdapter searchAdapter = new SearchAdapter();
    private LinearLayoutManager layoutReyclerTitleSearch;

    private final static int INDEXSEARCH = 2;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private Subscriber<List<ArtistResponse>> responseArtist = new Subscriber<List<ArtistResponse>>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<ArtistResponse> artistResponses) {
            for (ArtistResponse artistResponse : artistResponses) {
                Artist artist = new Artist();
                artist.setSongResponse(artistResponse);
                arrayList.add(artist);
            }
            setArtist(arrayList);
        }

        @Override
        public void onError(Throwable t) {
            Log.e("SongResponse List", t.getMessage());
        }

        @Override
        public void onComplete() {
        }
    };

    private Subscriber<List<SongResponse>> responseSong = new Subscriber<List<SongResponse>>() {
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

    private void setArtist(ArrayList arrayList) {
        searchAdapter.setData(arrayList, 0);
        recyclerView = fragmentSearchBinding.recyclerViewSearch;
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        fragmentSearchBinding.setLifecycleOwner(this);
        RecyclerView recyclerViewTitlteSearch = fragmentSearchBinding.recyclerViewTitlteSearch;
        addValueTitleSearch();
        searchTitleAdapter.setArrayList(arrayListItemSearch);
        layoutReyclerTitleSearch = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTitlteSearch.setLayoutManager(layoutReyclerTitleSearch);
        recyclerViewTitlteSearch.scheduleLayoutAnimation();
        recyclerViewTitlteSearch.setAdapter(searchTitleAdapter);
        eventButtonSearch();
        return fragmentSearchBinding.getRoot();
    }

    private void addValueTitleSearch() {
        arrayListItemSearch.clear();
        arrayListItemSearch.add("Artists");
        arrayListItemSearch.add("Songs");
        arrayListItemSearch.add("Search");
    }

    private void eventButtonSearch() {
        fragmentSearchBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentTitle = fragmentSearchBinding.textContextSearch.getText().toString().trim();
                if (contentTitle != null) {
                    Log.e(getTag(), contentTitle);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        arrayList.clear();
        mViewModel.setContext(getContext());
        // Request data
        mViewModel.getmSongListArtist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseArtist);

        mViewModel.getmSongResponeFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseSong);
    }

    @Override
    public void searchTitleClicked(int position) {
        switch (position) {
            case INDEXSEARCH : {
                Log.e(getTag(), "index search");
                fragmentSearchBinding.containerContextSearch.animate()
                        .alpha(1f)
                        .translationY(0)
                        .setDuration(300)
                        .setInterpolator(new AnticipateOvershootInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                fragmentSearchBinding.containerContextSearch.setVisibility(View.VISIBLE);
                            }
                        }).start();
                showInputTextSearch(position);
                break;
            }
            case 0 : {
                hideInputTextSearch(position);
                searchAdapter.setData(arrayList, position);
                break;
            }
            case 1 :{
                hideInputTextSearch(position);
                searchAdapter.setData(mSong, position);
                break;
            }
        }

        recyclerView = fragmentSearchBinding.recyclerViewSearch;
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

    private void showInputTextSearch(int position){
        fragmentSearchBinding.recyclerViewSearch.animate()
                .translationY(80)
                .setDuration(300)
                .setInterpolator(new AnticipateOvershootInterpolator()).start();
        searchTitleAdapter.setIndex(position);
    }

    private void hideInputTextSearch(int position){
        fragmentSearchBinding.containerContextSearch.setVisibility(View.GONE);
        fragmentSearchBinding.containerContextSearch.setTranslationY(-80);
        fragmentSearchBinding.recyclerViewSearch.animate()
                .translationY(0)
                .setDuration(300)
                .setInterpolator(new AnticipateOvershootInterpolator()).start();
        searchTitleAdapter.setIndex(position);
    }
}