package com.example.hippoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hippoplayer.list.ListFragment;
import com.example.hippoplayer.offline.OfflineFragment;
import com.example.hippoplayer.play.PassData;
import com.example.hippoplayer.play.ViewPagerAdapter;
import com.example.hippoplayer.search.SearchFragment;
import com.example.hippoplayer.utils.Constants;
import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private TabLayout tabLayoutMain;
    private ViewPager viewPagerMain;
    private ListFragment listFragment;
    OfflineFragment offlineFragment;
    public PassData mPassData;
    private SearchFragment searchFragment;
    boolean doubleBackToExitPressedOnce = false;

    SlidingUpPanelLayout.PanelState playPanelState;
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
        // set default tab to list
        tabLayoutMain.selectTab(tabLayoutMain.getTabAt(1));
    }

    @Override
    public void onBackPressed() {
        // to determine if the back stack is empty or not
        if (doubleBackToExitPressedOnce || isPlayPanelExpaned()) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private boolean isPlayPanelExpaned() {
        return playPanelState == SlidingUpPanelLayout.PanelState.EXPANDED;
    }


    public void setPanelState(SlidingUpPanelLayout.PanelState panelState) {
        this.playPanelState = panelState;
    }
}
