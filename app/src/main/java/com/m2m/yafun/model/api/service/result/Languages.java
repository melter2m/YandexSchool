package com.m2m.yafun.model.api.service.result;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Languages {

    private class Direction {
        private final String from;
        private final String to;

        private Direction(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

    private final List<String> dirs;
    private final LinkedHashMap<String, String> langs;

    private List<Direction> parsedDirections;
    private List<String> languagesNames;

    public Languages(List<String> directions, LinkedHashMap<String, String> languages) {
        this.dirs = directions;
        this.langs = languages;
    }

    public List<String> getDirections() {
        return dirs;
    }

    public LinkedHashMap<String, String> getLanguages() {
        return langs;
    }

    public List<String> getLanguagesNames() {
        initLanguagesNames();
        return languagesNames;
    }

    private void initLanguagesNames() {
        if (languagesNames == null || languagesNames.size() == 0) {
            this.languagesNames = new ArrayList<>();
            languagesNames.addAll(langs.values());
        }
    }

    public String getLanguageDisplayName(String lang) {
        return langs.get(lang);
    }

    public List<String> getAvailableLanguages(String selected) {
        List<String> result = new ArrayList<>();
        parseDirections();

        for (Direction direction: parsedDirections) {
            if (direction.from.equals(selected))
                result.add(direction.to);
        }
        return result;
    }

    private void parseDirections() {
        if (parsedDirections != null && parsedDirections.size() > 0)
            return;
        parsedDirections = new ArrayList<>();
        for (String direction: dirs) {
            int delimiter = direction.indexOf('-');
            if (delimiter == -1)
                return;
            String from = direction.substring(0, delimiter);
            String to = direction.substring(delimiter + 1);
            parsedDirections.add(new Direction(from, to));
        }
    }

}
