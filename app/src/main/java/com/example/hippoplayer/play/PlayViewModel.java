package com.example.hippoplayer.play;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlayViewModel extends ViewModel {

    private String name;
    private String description;
    private String url;
    private String thumbnail;
    private String artist;
    private String id;

    @BindingAdapter("bind:button_pause")
    public void buttonPause(FloatingActionButton floatingActionButton){
    }

}
