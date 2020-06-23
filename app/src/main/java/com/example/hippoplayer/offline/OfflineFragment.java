package com.example.hippoplayer.offline;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.utils.ConvertHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineFragment extends Fragment {

    private static final int READ_STORAGE_REQUEST_CODE = 111;
    private static final String TAG = OfflineFragment.class.getSimpleName();
    private OfflineViewModel mViewModel;
    private List<Song> songList;
    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OfflineViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkPermissionForReadExternalStorage()) {
            loadAudio();
        }
        else {
            requestPermissionForReadExternalStorage();
        }
    }

    private void loadAudio() {
        songList = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selector = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selector, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String idSong = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String idArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String thumbnail = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                byte[] rawArt;
                Bitmap art;
                BitmapFactory.Options bfo=new BitmapFactory.Options();

                mmr.setDataSource(getContext(), Uri.parse(data));
                rawArt = mmr.getEmbeddedPicture();

                // if rawArt is null then no cover art is embedded in the file or is not
                // recognized as such.
                if (null != rawArt) {
                    art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
                }
                else thumbnail = null;

                songList.add(new Song(data, title, idSong, idArtist, artistName, thumbnail));
            }
        }

        for (int i = 0; i < songList.size(); i++) {
            logSong(songList.get(i), i);
        }
        cursor.close();

    }

    private void logSong(Song song, int position) {
        Log.d(TAG, "logSong: -----------------------------");
        Log.d(TAG, "logSong: position " + position);
        Log.d(TAG, "loadAudio: song " + song.getSong());
        Log.d(TAG, "loadAudio: title " + song.getNameSong());
        Log.d(TAG, "logSong: idSong " + song.getIdSong());
        Log.d(TAG, "logSong: idArtist " + song.getIdArtist());
        Log.d(TAG, "logSong: artistName " + song.getNameArtist());
        Log.d(TAG, "logSong: thumbnail " + song.getThumbnail());
        Log.d(TAG, "logSong: -----------------------------");
    }

    private boolean checkPermissionForReadExternalStorage() {
        // this will check if user granted permission for the app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermissionForReadExternalStorage() {
        // request user to give permission to app
        try {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}