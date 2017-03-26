package com.m2m.yafun.model.db.gateway;


import com.m2m.yafun.model.db.entities.HistoryItem;

import java.util.List;

public interface IHistoryGateway {

    HistoryItem setFavorite(HistoryItem item, boolean value);

    List<HistoryItem> getOnlyFavorite();

}
