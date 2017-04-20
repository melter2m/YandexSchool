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

    private final static int TranslatePageIndex = 0;
    private final static int HistoryPageIndex = TranslatePageIndex + 1;
    private final static int FavoritesPageIndex = HistoryPageIndex + 1;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TranslatePageIndex:
                return new TranslatePage();
            case HistoryPageIndex:
                return new HistoryPage();
            case FavoritesPageIndex:
                return new FavoritesPage();
        }

        return null;
    }

    public int getPageIconId(int position) {
        switch (position) {
            case TranslatePageIndex:
                return R.drawable.exchange;
            case HistoryPageIndex:
                return R.drawable.history;
            case FavoritesPageIndex:
                return android.R.drawable.btn_star_big_off;
            default:
                return 0;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TranslatePageIndex:
                return context.getString(R.string.translate_page);
            case HistoryPageIndex:
                return context.getString(R.string.history_page);
            case FavoritesPageIndex:
                return context.getString(R.string.favorites_page);
        }
        return null;
    }
}
