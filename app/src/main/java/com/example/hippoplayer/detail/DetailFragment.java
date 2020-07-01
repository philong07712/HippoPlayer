package com.example.hippoplayer.detail;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.FragmentDetailBinding;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.utils.PathHelper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailFragment extends Fragment {

    private DetailViewModel mViewModel;
    private FragmentDetailBinding fragmentDetailBinding;
    private String idArtist;
    private List<Song> mSong = new ArrayList<>();
    private List<Artist> mArtists = new ArrayList<>();

    private void setListSongs(List<Song> mSong, List<Artist> mArtists, String idArtist) {
        List<String> idSongs = new ArrayList<>();
        List<Song> dataSong = new ArrayList<>();
        for (Artist artist : mArtists){
            if(artist.getId().equals(idArtist)){
                for (String idSong : artist.getSongsList()){
                    idSongs.add(idSong);
                }
            }
        }

        DetailAdapter detailAdapter = new DetailAdapter();
        for (String idSong : idSongs) {
            for (Song song : mSong) {
                if (song.getIdSong().equals(idSong)){
                    dataSong.add(song);
                }
            }
        }
        detailAdapter.setSongArrayList(dataSong);
        fragmentDetailBinding.recyclerViewDetail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentDetailBinding.recyclerViewDetail.setHasFixedSize(true);
        fragmentDetailBinding.recyclerViewDetail.setAdapter(detailAdapter);
    }

    private void setImageHeader(String idArtist) {
        String finalurl = PathHelper.getFullUrl(idArtist, PathHelper.TYPE_ARTIST);
        Glide.with(getContext())
                .load(finalurl)
                .centerCrop()
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_music_note_orange)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(fragmentDetailBinding.imageBackgroundHeaderDetail);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);
        Bundle bundle = this.getArguments();
        idArtist = bundle.getString("idArtist");
        mSong = (List<Song>) bundle.getSerializable("songs");
        mArtists = (List<Artist>) bundle.getSerializable("artists");
        setImageHeader(idArtist);
        setListSongs(mSong, mArtists, idArtist);
        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        mViewModel.setContext(getContext());
    }
}