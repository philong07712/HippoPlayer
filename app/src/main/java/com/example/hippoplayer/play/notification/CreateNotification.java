package com.example.hippoplayer.play.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.utils.PathHelper;

public class CreateNotification {
    public static final String TAG = CreateNotification.class.getSimpleName();

    public static final String ACTION_ID = "Create Notification 1";
    public static final String ACTION_NAME = "action name";

    public static final String ACTION_PREVIOUS = "action previous";
    public static final String ACTION_PLAY = "action play";
    public static final String ACTION_NEXT = "action next";

    // Fields
    Context mContext;
    Song mSong;
    int mPosition;
    int mSize;

    // Previous
    PendingIntent pendingIntentPrevious;
    int drw_previous;
    // Play
    PendingIntent pendingIntentPlay;
    int drw_play;
    // Next
    PendingIntent pendingIntentNext;
    int drw_next;

    public static Notification notification;


    public CreateNotification(Context context, Song song, int drawable, int pos, int size) {
        mContext = context;
        mSong = song;
        mPosition = pos;
        mSize = size;
        drw_play = drawable;

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
        // This will check what intent user want to do

        createIntentPrevious();
        createIntentPlay();
        createIntentNext();

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(mContext, TAG);
        notification = new NotificationCompat.Builder(mContext, ACTION_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note)
                .setContentTitle(mSong.getNameSong())
                .setContentText(mSong.getNameArtist())
                .setOnlyAlertOnce(true)
                .setLargeIcon(largeImage)
                .setShowWhen(false)
                .addAction(drw_previous, "Previous", pendingIntentPrevious)
                .addAction(drw_play, "Play", pendingIntentPlay)
                .addAction(drw_next, "Next", pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionCompat.getSessionToken())
                    .setShowActionsInCompactView(0, 1, 2))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(1, notification);

    }

    private void createIntentPrevious() {

        if (mPosition == 0) {
            pendingIntentPrevious = null;
            drw_previous = 0;
        } else {
            Intent intentPrevious = new Intent(mContext, NotificationActionService.class)
                    .setAction(ACTION_PREVIOUS);
            pendingIntentPrevious = PendingIntent.getBroadcast(mContext, 0,
                    intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
            drw_previous = R.drawable.ic_baseline_skip_previous_24;
        }
    }

    private void createIntentPlay() {
        // Intent play
        Intent intentPlay = new Intent(mContext, NotificationActionService.class)
                .setAction(ACTION_PLAY);
        pendingIntentPlay = PendingIntent.getBroadcast(mContext, 0,
                intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private void createIntentNext() {
        if (mPosition == mSize - 1) {
            pendingIntentNext = null;
            drw_next = 0;
        } else {
            Intent intentNext = new Intent(mContext, NotificationActionService.class)
                    .setAction(ACTION_NEXT);
            pendingIntentNext = PendingIntent.getBroadcast(mContext, 0,
                    intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
            drw_next = R.drawable.ic_baseline_skip_next_24;
        }
    }
}
