package com.m2m.yafun.model.db.entities;


import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class LanguagesItem extends DatedEntity {

    private final List<String> directions;

    private final Map<String, String> languages;

    public LanguagesItem(long id, List<String> directions, Map<String, String> languages, Calendar date) {
        super(id, date);
        this.directions = directions;
        this.languages = languages;
    }


    public List<String> getDirections() {
        return directions;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }
}
