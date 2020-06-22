package com.example.hippoplayer.search.event;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class ItemEvent {

    public void buttonPlayItemList(View view, String idSong){
        Log.e("Button", "Click" + idSong);
    }
}
