package com.m2m.yafun.model.db.entities;


import java.util.Calendar;

public class DatedEntity extends EntityBase {

    private final Calendar date;

    public DatedEntity(long id, Calendar date) {
        super(id);
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

}
