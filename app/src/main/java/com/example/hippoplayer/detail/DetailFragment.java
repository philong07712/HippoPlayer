package com.example.hippoplayer.detail;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.hippoplayer.utils.PathHelper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailFragment extends Fragment {

    private DetailViewModel mViewModel;
    private FragmentDetailBinding fragmentDetailBinding;
    private String idArtist;
    private Artist artist = new Artist();

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    public Subscriber<ArtistResponse> responseArtist = new Subscriber<ArtistResponse>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(ArtistResponse artistResponse) {
            artist.setArtistResponse(artistResponse);
            setImageHeader();
        }

        @Override
        public void onError(Throwable t) {
            Log.e("Detail Fragment", t.getMessage());
        }

        @Override
        public void onComplete() {
            Log.e("Detail Fragment", "onComplete");
        }
    };

    private void setImageHeader() {
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
        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        mViewModel.setIdArtist(idArtist);
        mViewModel.setContext(getContext());
        mViewModel.getmArtistResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseArtist);
    }
}