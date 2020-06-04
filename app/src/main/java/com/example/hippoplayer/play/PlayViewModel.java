package com.example.hippoplayer.play;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlayViewModel extends ViewModel {

    //

    private SongService songService = new SongService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Song song; // nhận song từ fragment
    public static Context mContext;
    List<SongResponse> songList = new ArrayList<>();

    public PlayViewModel() {

    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public SongResponse getSong(int songIndex) {
        songList = songService.getSongs();
        return songList.get(songIndex);
    }

    public void setSong(SongResponse songResponse) { // nhận song từ fragment (Nhận bài hát từ fragment)
        this.song = new Song(songResponse, songResponse.artist); // nhận song từ fragment
    }

    // Get one song
   /* public LiveData<Song> getSongLiveData() {
        return songService.getFirstSong();
    }*/

    public String getFullUrl(String endpoint) {
        return Constants.SONG_BASE_URL + endpoint;
    }

    @BindingAdapter("app:imageCenter")
    public static void setImage(ImageView image, String url) {
        String finalurl = Constants.SONG_BASE_URL + url;
        Glide.with(mContext)
                .load(finalurl).centerCrop()
                .fitCenter().into(image);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
