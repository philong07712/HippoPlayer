package com.example.hippoplayer.play;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hippoplayer.R;
import com.example.hippoplayer.databinding.FragmentPlayBinding;

public class PlayFragment extends Fragment {

    private FragmentPlayBinding binding;
    private PlayViewModel mViewModel;

    public static PlayFragment newInstance() {
        return new PlayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlayViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setPlayViewModel(mViewModel);
    }
}
