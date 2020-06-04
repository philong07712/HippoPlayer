package com.example.hippoplayer.play;

import android.util.Log;

import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.SongResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SongService {

    // This class look like SongRepo
    private static final String TAG = "SongService";
    private Retrofit retrofit;
    private SongAPI api;
    private List<SongResponse> listSong = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SongService() {
        this.retrofit = RetrofitHandler.getInstance().getSongRetrofit();
        api = this.retrofit.create(SongAPI.class);
    }

    // Lấy data từ api
    public Observable<SongResponse> getListSongObservable() {
        return api.getSongList()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<SongResponse>, ObservableSource<SongResponse>>() {
                    @Override
                    public ObservableSource<SongResponse> apply(List<SongResponse> songResponses) throws Throwable {
                        listSong = songResponses;
                        return Observable.fromIterable(songResponses)
                                .subscribeOn(Schedulers.io());
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public List<SongResponse> getSongs() {
        getListSongObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull SongResponse songResponse) {
                        Log.e("TAG", songResponse.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Log.e("TAG", listSong.toString());
        return listSong;
    }

    /*public List<Song> getListSong() {
        return listSong;
    }*/

    // Get song live data đưa ra một bài hát, chưa xử lí vấn đề bài hát đó có ID hay k.
    /*public LiveData<Song> getFirstSong() {
        MutableLiveData<Song> liveData = new MutableLiveData<>();
        api.getSongList("IwAR3wfzv2EHye3bxfRupZK0XYeCE6MjPeGnbHGmU7jWsv1V3GeWDjzyrafZA")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<SongRespone, ObservableSource<Song>>() {
                    @Override
                    public ObservableSource<Song> apply(SongRespone songRespone) throws Throwable {
                        liveData.setValue(songRespone.mSongList.get(0));
                        return Observable.fromIterable(songRespone.mSongList)
                                .subscribeOn(Schedulers.io());
                    }
                });
        return liveData;
    }*/
}
