package com.example.hippoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.hippoplayer.play.PlayFragment;
import com.example.hippoplayer.play.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tl_main;
    private ViewPager vp_main;
    private PlayFragment playFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl_main = findViewById(R.id.tl_main);
        vp_main = findViewById(R.id.vp_main);

        playFragment = new PlayFragment();

        setupViewPager();
        tl_main.setupWithViewPager(vp_main);
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(playFragment, "Player Fragment");
        vp_main.setAdapter(viewPagerAdapter);
    }
}
