package com.m2m.yafun.model.db.entities;


import java.util.Calendar;
import java.util.List;

public class HistoryItem extends DatedEntity {

    private final String text;
    private final String direction;
    private final List<String> translation;
    private final boolean isFavorite;

    public HistoryItem(Calendar date, String text, String direction, List<String> translation, boolean isFavorite) {
        this(-1, date, text, direction, translation, isFavorite);
    }

    public HistoryItem(long id, Calendar date, String text, String direction, List<String> translation, boolean isFavorite) {
        super(id, date);
        this.text = text;
        this.direction = direction;
        this.translation = translation;
        this.isFavorite = isFavorite;
    }

    public String getText() {
        return text;
    }

    public String getDirection() {
        return direction;
    }

    public boolean translationEqual(List<String> otherTranslation) {
        if (otherTranslation == null)
            return false;
        for (String translation: otherTranslation) {
            if (!this.translation.contains(translation))
                return false;
        }
        return true;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getTranslationTotalString() {
        String result = "";
        for (String item: translation) {
            result += item;
            result += "\n";
        }
        return result;
    }
}
