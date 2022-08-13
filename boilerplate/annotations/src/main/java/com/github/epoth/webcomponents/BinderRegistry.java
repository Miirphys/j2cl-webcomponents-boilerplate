package com.github.epoth.webcomponents;

import java.util.HashMap;
import java.util.Map;

public class BinderRegistry {

    private static Map<String, ComponentBinder> registry = new HashMap<>();

    public static void add(String key, ComponentBinder eventBinder) {

        registry.put(key, eventBinder);

    }

    public static ComponentBinder get(String key) {

        return registry.get(key);

    }


}
