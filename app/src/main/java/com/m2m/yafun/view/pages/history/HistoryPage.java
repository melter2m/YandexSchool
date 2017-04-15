package com.m2m.yafun.view.pages.history;

import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.view.pages.history.base.HistoryBasePage;

import java.util.List;

public class HistoryPage extends HistoryBasePage {

    @Override
    protected void initAdapter() {
        List<HistoryItem> items = getDatabaseContext().createHistoryGateway().getAllItems();
        historyAdapter = new HistoryAdapter(this, items);
    }

    @Override
    protected void updateAdapter() {
        if (historyAdapter == null)
            return;
        List<HistoryItem> items = getDatabaseContext().createHistoryGateway().getAllItems();
        historyAdapter.update(items);
    }
}
