package com.m2m.yafun.view.pages.history;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2m.yafun.R;
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.view.pages.Page;

import java.util.List;

public class HistoryPage extends Page {

    HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_history, container, false);
        initHistoryView((RecyclerView) result.findViewById(R.id.translationsRecyclerView));
        return result;
    }

    private void initHistoryView(RecyclerView translationsView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        List<HistoryItem> items = getDatabaseContext().createHistoryGateway().getAllItems();
        historyAdapter = new HistoryAdapter(this, items);
        translationsView.setAdapter(historyAdapter);
        translationsView.setLayoutManager(layoutManager);
        translationsView.setItemAnimator(itemAnimator);
    }

    @Override
    public void update() {
        List<HistoryItem> items = getDatabaseContext().createHistoryGateway().getAllItems();
        historyAdapter = new HistoryAdapter(this, items);
    }
}
