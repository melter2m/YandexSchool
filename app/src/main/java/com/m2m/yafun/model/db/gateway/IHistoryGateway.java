package com.m2m.yafun.model.db.gateway;


import com.m2m.yafun.model.db.entities.HistoryItem;

public interface IHistoryGateway {

    HistoryItem setFavorite(HistoryItem item, boolean value);

}
