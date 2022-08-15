package com.github.epoth.boilerplate;

import elemental2.core.JsMap;
import elemental2.dom.HTMLTemplateElement;

import java.util.HashMap;
import java.util.Map;

public class ObservableRegistry {

    private static JsMap<String, String[]> registry = new JsMap<>();

    public static void add(String key, String[] attributes) {

        registry.set(key, attributes);

    }

    public static String[] get(String key) {

        return registry.get(key);

    }

}
