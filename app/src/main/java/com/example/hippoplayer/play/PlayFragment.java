package com.example.hippoplayer.play;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.databinding.FragmentPlayBinding;


public class PlayFragment extends Fragment {

    private static final String TAG = PlayFragment.class.getSimpleName();

    // View

    private FragmentPlayBinding fragmentPlayBinding;
    private PlayViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false);
        fragmentPlayBinding.setLifecycleOwner(this);
        return fragmentPlayBinding.getRoot();
     }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlayViewModel.class);
        fragmentPlayBinding.setPlayViewModel(mViewModel);
        mViewModel.setContext(getContext());
        mViewModel.setSong(mViewModel.getSong(0));
    }
}
