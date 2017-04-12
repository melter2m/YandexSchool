package com.m2m.yafun.model.db.gateway;


import com.m2m.yafun.model.db.entities.HistoryItem;

import java.util.List;

public interface IHistoryGateway extends ITableGateway<HistoryItem> {

    HistoryItem setFavorite(HistoryItem item, boolean value);

    List<HistoryItem> getOnlyFavorite();

    HistoryItem getItem(String originalText, String direction);

}
