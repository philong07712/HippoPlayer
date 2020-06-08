package com.example.hippoplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.session.MediaSession;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.utils.PathHelper;

public class CreateNotification {
    public static final String TAG = CreateNotification.class.getSimpleName();

    public static final String ACTION_ID = "Create Notification 1"  ;
    public static final String ACTION_PREVIOUS = TAG + "action previous";
    public static final String ACTION_PLAY = TAG + "action play";
    public static final String ACTION_NEXT = TAG + "action next";

    public static Notification notification;

    Context mContext;
    Song mSong;

    public CreateNotification(Context context, Song song, int pos, int size) {
        mContext = context;
        mSong = song;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // convert url image to bitmap

            Glide.with(mContext)
                    .asBitmap()
                    .load(PathHelper.getFullUrl(song.getThumbnail()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            create(resource);
                        }
                    });
            // create notification

        }
    }

    private void create(Bitmap largeImage) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(mContext, TAG);
        notification = new NotificationCompat.Builder(mContext, ACTION_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note)
                .setContentTitle(mSong.getNameSong())
                .setContentText(mSong.getNameArtist())
                .setOnlyAlertOnce(true)
                .setLargeIcon(largeImage)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(1, notification);

    }
}
