package com.github.epoth.webcomponents;

import elemental2.dom.HTMLTemplateElement;

import java.util.HashMap;
import java.util.Map;

public class TemplateRegistry {
    private static Map<String, HTMLTemplateElement> registry = new HashMap<>();

    public static void add(String key, HTMLTemplateElement htmlTemplateElement) {

        registry.put(key, htmlTemplateElement);

    }

    public static HTMLTemplateElement get(String key) {

        return registry.get(key);

    }

}
