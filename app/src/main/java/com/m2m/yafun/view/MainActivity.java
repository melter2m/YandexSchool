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

import com.m2m.yafun.R;
import com.m2m.yafun.view.pages.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnUpdateListener, Updater {

    private SectionsPagerAdapter sectionsPagerAdapter;

    private ViewPager viewPager;
    private List<OnUpdateListener> updateListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        updateListeners = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
