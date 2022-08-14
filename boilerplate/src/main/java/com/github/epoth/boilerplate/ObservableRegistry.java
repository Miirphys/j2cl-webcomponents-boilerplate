package com.github.epoth.boilerplate;

import elemental2.dom.HTMLTemplateElement;

import java.util.HashMap;
import java.util.Map;

public class ObservableRegistry {

    private static Map<String, String[]> registry = new HashMap<>();

    public static void add(String key, String[] attributes) {

        registry.put(key, attributes);

    }

    public static String[] get(String key) {

        return registry.get(key);

    }

}
