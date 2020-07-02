package com.example.hippoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.hippoplayer.list.ListFragment;
import com.example.hippoplayer.offline.OfflineFragment;
import com.example.hippoplayer.play.PassData;
import com.example.hippoplayer.play.ViewPagerAdapter;
import com.example.hippoplayer.search.SearchFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private TabLayout tabLayoutMain;
    private ViewPager viewPagerMain;
    private ListFragment listFragment;
    OfflineFragment offlineFragment;
    public PassData mPassData;
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
        offlineFragment = new OfflineFragment();
        searchFragment = new SearchFragment();
        // add new fragment in function setup view pager
        setupViewPager();
        viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Hide input text when change page
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayoutMain.setupWithViewPager(viewPagerMain);
        // section for fragment tabLayout.getTabAt(i).setIcon(....);
    }

    public void passVal(PassData passData) {
        this.mPassData = passData;
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // add new fragment
        viewPagerAdapter.addFragment(offlineFragment, "Offline Fragment");
        viewPagerAdapter.addFragment(listFragment, "List Fragment");
        viewPagerAdapter.addFragment(searchFragment, "Search Fragment");
        viewPagerMain.setAdapter(viewPagerAdapter);
    }
}
