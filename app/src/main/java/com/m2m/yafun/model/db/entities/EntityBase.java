package com.m2m.yafun.model.db.entities;

public class EntityBase {

    protected long id;

    protected EntityBase(long id) {
        this.id = id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
