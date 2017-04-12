package com.m2m.yafun.model.api.service.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private boolean isSorted = false;

    private List<Direction> parsedDirections;
    private List<String> languagesNames;

    public Languages(List<String> directions, LinkedHashMap<String, String> languages) {
        this.dirs = directions;
        this.langs = languages;
        sortIfNeeded();
    }

    public List<String> getDirections() {
        return dirs;
    }

    public LinkedHashMap<String, String> getLanguages() {
        sortIfNeeded();
        return langs;
    }

    private void sortIfNeeded() {
        if (isSorted)
            return;
        List<Map.Entry<String, String>> entries = new ArrayList<>(langs.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return lhs.getValue().compareTo(rhs.getValue());
            }
        });

        langs.clear();
        for(Map.Entry<String, String> e : entries) {
            langs.put(e.getKey(), e.getValue());
        }
        isSorted = true;
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
