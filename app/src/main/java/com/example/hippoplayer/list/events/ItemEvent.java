package com.example.hippoplayer.list.events;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class ItemEvent {
    private Context context;

    public void buttonPlayItemList(View view, String idSong){
        Log.e("Button", "Click" + idSong);
    }
}
