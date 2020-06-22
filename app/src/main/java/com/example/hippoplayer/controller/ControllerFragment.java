package com.example.hippoplayer.controller;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.hippoplayer.R;
import com.example.hippoplayer.play.PlayFragment;

public class ControllerFragment extends Fragment {

    private static final String TAG = ControllerFragment.class.getSimpleName();
    private ControllerViewModel mViewModel;
    private ConstraintLayout mContainer;

    public static ControllerFragment newInstance() {
        return new ControllerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_fragment, container, false);
        mContainer = view.findViewById(R.id.mini_container_controller);

        setListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ControllerViewModel.class);
        // TODO: Use the ViewModel
    }

    private void setListener() {
        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
//                fr.replace(R.id.container_controller, new PlayFragment());
//                fr.commit();
            }
        });
    }

}