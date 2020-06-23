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

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements SearchTitleAdapter.SearchTiltleListener {

    private SearchViewModel mViewModel;

    private ArrayList<Artist> arrayList = new ArrayList<>();
    private ArrayList<String> arrayListItemSearch = new ArrayList<>();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private FragmentSearchBinding fragmentSearchBinding;
    private RecyclerView recyclerView;
    private SearchTitleAdapter searchTitleAdapter = new SearchTitleAdapter(this);

    private LinearLayoutManager layoutReyclerTitleSearch;
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
    private int INDEXSEARCH = 4;

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
        arrayListItemSearch.add("Artists");
        arrayListItemSearch.add("Songs");
        arrayListItemSearch.add("Feature");
        arrayListItemSearch.add("For you");
        arrayListItemSearch.add("Search");
    }

    private void eventButtonSearch() {
        fragmentSearchBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentTitle = fragmentSearchBinding.textContextSearch.getText().toString().trim();
                if (contentTitle != null) {

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
        mViewModel.getmSongListArtist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response);
        Log.d("GOI LAI", "GOI LAI");
    }

    @Override
    public void searchTitleClicked(int position) {
        if (position == INDEXSEARCH) {
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
                    });
            searchTitleAdapter.setIndex(position);
        } else {
            fragmentSearchBinding.containerContextSearch.setVisibility(View.GONE);
            fragmentSearchBinding.containerContextSearch.setTranslationY(-80);
            searchTitleAdapter.setIndex(position);
        }
    }
}