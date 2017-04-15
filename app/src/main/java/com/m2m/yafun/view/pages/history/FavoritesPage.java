package com.m2m.yafun.view.pages.history;

import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.view.pages.Page;
import com.m2m.yafun.view.pages.history.base.HistoryBasePage;

import java.util.List;

public class FavoritesPage extends HistoryBasePage {

    @Override
    protected void initAdapter() {
        List<HistoryItem> items = getDatabaseContext().createHistoryGateway().getOnlyFavorite();
        historyAdapter = new FavoritesAdapter(this, items);
    }

    @Override
    protected void updateAdapter() {
        if (historyAdapter == null)
            return;
        List<HistoryItem> items = getDatabaseContext().createHistoryGateway().getOnlyFavorite();
        historyAdapter.update(items);
    }
}
