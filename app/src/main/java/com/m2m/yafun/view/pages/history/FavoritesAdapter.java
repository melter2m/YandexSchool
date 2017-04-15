package com.m2m.yafun.view.pages.history;

import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.view.pages.history.base.HistoryBaseAdapter;
import com.m2m.yafun.view.pages.history.base.HistoryBasePage;

import java.util.List;

public class FavoritesAdapter extends HistoryBaseAdapter {

    protected FavoritesAdapter(HistoryBasePage historyPage, List<HistoryItem> historyItems) {
        super(historyPage, historyItems);
    }

    @Override
    protected void changeFavoriteState(int position) {
        HistoryItem item = historyItems.get(position);
        IHistoryGateway gateway = historyPage.getDatabaseContext().createHistoryGateway();
        gateway.setFavorite(item, !item.isFavorite());
        historyItems.remove(position);
        notifyItemRemoved(position);
    }
}
