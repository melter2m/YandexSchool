package com.m2m.yafun.view.pages.history.base;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2m.yafun.R;
import com.m2m.yafun.view.pages.Page;

public abstract class HistoryBasePage extends Page {

    protected HistoryBaseAdapter historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_history, container, false);
        initHistoryView((RecyclerView) result.findViewById(R.id.translationsRecyclerView));
        return result;
    }

    private void initHistoryView(RecyclerView translationsView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        initAdapter();
        translationsView.setAdapter(historyAdapter);
        translationsView.setLayoutManager(layoutManager);
        translationsView.setItemAnimator(itemAnimator);
        ItemTouchHelper.Callback callback = new HistoryItemTouchCallback(historyAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(translationsView);
    }

    protected abstract void initAdapter();

    @Override
    public void update() {
        updateAdapter();
    }

    protected abstract void updateAdapter();
}
