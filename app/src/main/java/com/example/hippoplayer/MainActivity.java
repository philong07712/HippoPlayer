package com.example.hippoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.hippoplayer.list.ListFragment;
import com.example.hippoplayer.offline.OfflineFragment;
import com.example.hippoplayer.play.PassData;
import com.example.hippoplayer.play.ViewPagerAdapter;
import com.example.hippoplayer.search.SearchFragment;
import com.example.hippoplayer.utils.Constants;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private TabLayout tabLayoutMain;
    private ViewPager viewPagerMain;
    private ListFragment listFragment;
    OfflineFragment offlineFragment;
    public PassData mPassData;
    private SearchFragment searchFragment;

    private int[] tabIcons = {
            R.drawable.ic_baseline_wifi_off_24_black,
            R.drawable.ic_baseline_view_list_24,
            R.drawable.ic_baseline_search_24
    };
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
        offlineFragment = new OfflineFragment();
        searchFragment = new SearchFragment();
        // add new fragment in function setup view pager
        setupViewPager();
        tabLayoutMain.setupWithViewPager(viewPagerMain);
        setupTabLayout();
        // section for fragment tabLayout.getTabAt(i).setIcon(....);
    }

    public void passVal(PassData passData) {
        this.mPassData = passData;
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // add new fragment
        viewPagerAdapter.addFragment(offlineFragment, null);
        viewPagerAdapter.addFragment(listFragment, null);
        viewPagerAdapter.addFragment(searchFragment, null);
        viewPagerMain.setOffscreenPageLimit(Constants.MAX_TAB_VIEWPAGER);
        viewPagerMain.setAdapter(viewPagerAdapter);
    }

    private void setupTabLayout() {
        tabLayoutMain.getTabAt(0).setIcon(tabIcons[0]);
        tabLayoutMain.getTabAt(1).setIcon(tabIcons[1]);
        tabLayoutMain.getTabAt(2).setIcon(tabIcons[2]);
    }
}
