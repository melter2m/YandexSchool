package com.m2m.yafun.view.pages.history;

import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.view.pages.history.base.HistoryBaseAdapter;

import java.util.List;

class HistoryAdapter extends HistoryBaseAdapter {

    HistoryAdapter(HistoryPage historyPage, List<HistoryItem> historyItems) {
        super(historyPage, historyItems);
    }

    @Override
    protected void changeFavoriteState(int position) {
        HistoryItem item = historyItems.get(position);
        IHistoryGateway gateway = historyPage.getDatabaseContext().createHistoryGateway();
        item = gateway.setFavorite(item, !item.isFavorite());
        historyItems.remove(position);
        historyItems.add(position, item);
        notifyItemChanged(position);
    }

}
