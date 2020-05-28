package com.example.hippoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.PlayFragment;
import com.example.hippoplayer.play.SongService;
import com.example.hippoplayer.play.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private TabLayout tl_main;
    private ViewPager vp_main;
    private PlayFragment playFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_main);
        // find view by id in activity_main
        tl_main = findViewById(R.id.tl_main);
        vp_main = findViewById(R.id.vp_main);

        // create new fragment
        playFragment = new PlayFragment();

        // add new fragment in function setup view pager
        setupViewPager();
        tl_main.setupWithViewPager(vp_main);
        // seticon for fragment tabLayout.getTabAt(i).setIcon(....);
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // add new fragment
        viewPagerAdapter.addFragment(playFragment, "Player Fragment");
        vp_main.setAdapter(viewPagerAdapter);
    }
}
