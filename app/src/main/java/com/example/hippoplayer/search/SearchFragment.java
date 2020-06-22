package com.example.hippoplayer.search;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.databinding.FragmentSearchBinding;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.ArtistResponse;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;

    private ArrayList<Artist> arrayList = new ArrayList<>();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private FragmentSearchBinding fragmentSearchBinding;
    private RecyclerView recyclerView;
    private Subscriber<List<ArtistResponse>> response = new Subscriber<List<ArtistResponse>>() {
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

    private void setArtist(ArrayList arrayList) {
        SearchAdapter searchAdapter = new SearchAdapter();
        searchAdapter.setArtists(arrayList);
        recyclerView = fragmentSearchBinding.recyclerViewSearch;
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        fragmentSearchBinding.setLifecycleOwner(this);
        fragmentSearchBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getTag(), fragmentSearchBinding.textContextSearch.getText().toString());
            }
        });
        return fragmentSearchBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mViewModel.setContext(getContext());
        mViewModel.getmSongListArtist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response);
        Log.d("GOI LAI", "GOI LAI");
    }
}