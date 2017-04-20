package com.m2m.yafun.view;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.m2m.yafun.R;
import com.m2m.yafun.view.pages.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnUpdateListener, Updater {

    private List<OnUpdateListener> updateListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initTabs();
        updateListeners = new ArrayList<>();
        checkNetworkState();
    }

    private void checkNetworkState() {
        if (NetworkUtils.isNetworkAvailable(this))
            return;
        Toast.makeText(this, R.string.network_is_not_available, Toast.LENGTH_LONG).show();
    }

    private void initTabs() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setIcon(sectionsPagerAdapter.getPageIconId(i));
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void notifyOthersToUpdate() {
        initUpdateListenersIfNeeded();
        for (OnUpdateListener listener : updateListeners) {
            listener.update();
        }
    }

    @Override
    public void registerUpdateListener(OnUpdateListener listener) {
        initUpdateListenersIfNeeded();
        updateListeners.add(listener);
    }

    @Override
    public void unRegisterUpdateListener(OnUpdateListener listener) {
        initUpdateListenersIfNeeded();
        updateListeners.remove(listener);
    }

    private void initUpdateListenersIfNeeded() {
        if (updateListeners == null)
            updateListeners = new ArrayList<>();
    }

}
