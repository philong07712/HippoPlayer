package com.example.hippoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.hippoplayer.list.ListFragment;
import com.example.hippoplayer.play.PlayFragment;
import com.example.hippoplayer.play.ViewPagerAdapter;
import com.example.hippoplayer.search.SearchFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private TabLayout tabLayoutMain;
    private ViewPager viewPagerMain;
    private PlayFragment playFragment;
    private ListFragment listFragment;
    private SearchFragment searchFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_main);
        // find view by id in activity_main
        tabLayoutMain = findViewById(R.id.tl_main);
        viewPagerMain = findViewById(R.id.vp_main);

        // create new fragment
        listFragment = new ListFragment();
        searchFragment = new SearchFragment();
        // add new fragment in function setup view pager
        setupViewPager();
        tabLayoutMain.setupWithViewPager(viewPagerMain);
        // seticon for fragment tabLayout.getTabAt(i).setIcon(....);
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // add new fragment
        viewPagerAdapter.addFragment(listFragment, "List Fragment");
        viewPagerAdapter.addFragment(searchFragment, "Search Fragment");
        viewPagerMain.setAdapter(viewPagerAdapter);
    }
}
