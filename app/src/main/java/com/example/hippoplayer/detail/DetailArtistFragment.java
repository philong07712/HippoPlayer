package com.example.hippoplayer.detail;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hippoplayer.R;

public class DetailArtistFragment extends Fragment {

    private DetailArtistViewModel mViewModel;

    public static DetailArtistFragment newInstance() {
        return new DetailArtistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_artist_fragment, container, false);;
        Bundle bundle = this.getArguments();
        String idArtist = bundle.getString("idArtist");
        TextView textView = view.findViewById(R.id.textView_detail);
        textView.setText("hello " +  idArtist);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DetailArtistViewModel.class);
        // TODO: Use the ViewModel
    }

}