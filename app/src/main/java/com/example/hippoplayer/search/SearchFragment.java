package com.example.hippoplayer.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.hippoplayer.MainActivity;
import com.example.hippoplayer.databinding.FragmentSearchBinding;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.play.PassData;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements SearchTitleAdapter.SearchTiltleListener {

    private SearchViewModel mViewModel;

    public static ArrayList<Artist> artists = new ArrayList<>();
    public static ArrayList<Song> songs = new ArrayList<>();
    public static ArrayList<Song> songOfflineList = new ArrayList<>();
    private ArrayList allData = new ArrayList();
    private ArrayList<String> arrayListItemSearch = new ArrayList<>();

    private FragmentSearchBinding fragmentSearchBinding;
    private RecyclerView recyclerView;
    private SearchTitleAdapter searchTitleAdapter = new SearchTitleAdapter(this);
    private  SearchAdapter searchAdapter = new SearchAdapter();
    private LinearLayoutManager layoutReyclerTitleSearch;

    private final static int ARTIST = 0;
    private final static int SONG = 1;
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
                artists.add(artist);
            }
            setArrayList(artists);
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
                songs.add(song);
            }
            songs.addAll(songOfflineList);
        }

        @Override
        public void onError(Throwable t) {
            Log.e("SongResponse List", t.getMessage());
        }

        @Override
        public void onComplete() {
        }
    };

    private void setArrayList(ArrayList arrayList) {
        searchAdapter.setData(arrayList, 0);
        recyclerView = fragmentSearchBinding.recyclerViewSearch;
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

    private void addValueTitleSearch() {
        arrayListItemSearch.clear();
        arrayListItemSearch.add("Artists");
        arrayListItemSearch.add("Songs");
        arrayListItemSearch.add("Search");
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
        ((MainActivity) getActivity()).passOfflineList(new PassData() {
            @Override
            public void onChange(List<Song> songOffline, int position) {
                songOfflineList.addAll(songOffline);
            }
        });
        fragmentSearchBinding.textContextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                filter(text);
            }
        });
        return fragmentSearchBinding.getRoot();
    }

    private void filter(String text) {
        ArrayList<Artist> artistsFilter = new ArrayList<>();
        ArrayList<Song> songsFilter = new ArrayList<>();
        for (Artist artist : artists){
            if(artist.getName().toLowerCase().contains(text.toLowerCase())){
                artistsFilter.add(artist);
            }
        }
        for (Song song : songs){
            if (song.getNameSong().toLowerCase().contains(text.toLowerCase())){
                songsFilter.add(song);
            }
        }

        if(artistsFilter.size() == 1 || songsFilter.size() == 1){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        }

        if(artistsFilter.size() != 0){
            searchAdapter.setData(artistsFilter, ARTIST);
        }
        if(songsFilter.size() != 0){
            searchAdapter.setData(songsFilter, SONG);
        }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        artists.clear();
        songs.clear();
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

        mViewModel.setActivity(getActivity());
    }

    @Override
    public void searchTitleClicked(int position) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
        switch (position) {
            case INDEXSEARCH : {
                fragmentSearchBinding.textContextSearch.setText("");
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
            case ARTIST : {
                hideInputTextSearch(position);
                searchAdapter.setData(artists, position);
                break;
            }
            case SONG:{
                hideInputTextSearch(position);
                searchAdapter.setData(songs, position);
                break;
            }
        }

        recyclerView = fragmentSearchBinding.recyclerViewSearch;
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }
}