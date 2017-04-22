package com.m2m.yafun.model.db.entities;


import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class LanguagesItem extends DatedEntity {

    private final List<String> directions;

    private final LinkedHashMap<String, String> languages;

    public LanguagesItem(long id, List<String> directions, LinkedHashMap<String, String> languages, Calendar date) {
        super(id, date);
        this.directions = directions;
        this.languages = languages;
    }

    public List<String> getDirections() {
        return directions;
    }

    public LinkedHashMap<String, String> getLanguages() {
        return languages;
    }
}
