package com.m2m.yafun.view.pages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.m2m.yafun.R;
import com.m2m.yafun.view.MainActivity;
import com.m2m.yafun.view.pages.history.FavoritesPage;
import com.m2m.yafun.view.pages.history.HistoryPage;
import com.m2m.yafun.view.pages.translate.TranslatePage;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TranslatePage();
            case 1:
                return new HistoryPage();
            case 2:
                return new FavoritesPage();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.translate_page);
            case 1:
                return context.getString(R.string.history_page);
            case 2:
                return context.getString(R.string.favorites_page);
        }
        return null;
    }
}
