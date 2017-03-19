package com.m2m.yafun.model.api.service.result;

import java.util.List;
import java.util.Map;

public class Languages {

    private final List<String> dirs;

    private final Map<String, String> langs;

    public Languages(List<String> directions, Map<String, String> languages) {
        this.dirs = directions;
        this.langs = languages;
    }

    public List<String> getDirections() {
        return dirs;
    }

    public Map<String, String> getLanguages() {
        return langs;
    }

}
