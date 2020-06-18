package com.example.hippoplayer.search;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.ArtistResponse;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private BubblePicker bubblePicker;

    private List<Artist> artistList = new ArrayList<>();
    private List<Artist> artistList_fake = new ArrayList<>();

    private int maxValueRequest = 1;
    private String colorPicker = "#F8B739";

    private List<String> strings = new ArrayList<>();


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

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
                artistList.add(artist);
            }
        }

        @Override
        public void onError(Throwable t) {
            Log.e("SongResponse List", t.getMessage());
        }

        @Override
        public void onComplete() {
            createBubbles(artistList);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        bubblePicker = view.findViewById(R.id.bubble_picker);
        artistList_fake.add(new Artist());
        artistList_fake.add(new Artist());
        return view;
    }

    private void createBubbles(List<Artist> artists) {
        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                Log.d("Size", String.valueOf(artists.size()));
                return artists.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                PickerItem item = new PickerItem();
                item.setTitle(artists.get(i).getName());
                item.setTextColor(Color.WHITE);
                item.setColor(Color.parseColor(colorPicker));
                return item;
            }
        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                Log.e("BubblePicker", "Seselected");
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {
                Log.e("BubblePicker", "Deselected");
            }
        });

        bubblePicker.setBubbleSize(10);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mViewModel.getmSongListArtist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response);
        createBubbles(artistList_fake);
        Log.d("GOI LAI", "GOI LAI");
    }

    @Override
    public void onResume() {
        super.onResume();
        bubblePicker.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bubblePicker.onPause();
    }
}