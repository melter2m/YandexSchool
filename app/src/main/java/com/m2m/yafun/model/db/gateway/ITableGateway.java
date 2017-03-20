package com.m2m.yafun.model.db.gateway;

import com.m2m.yafun.model.db.entities.EntityBase;
import java.util.List;

public interface ITableGateway<T extends EntityBase> {

    long insertItem(T item);

    void deleteItem(long id);

    T getItemById(long id);

    List<T> getAllItems();

    List<Long> getIds();

    void updateItem(long id, T newItemValue);

    boolean isItemExist(long id);
}
