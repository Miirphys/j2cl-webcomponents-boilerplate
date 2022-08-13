package com.github.epoth.webcomponents;

import java.util.HashMap;
import java.util.Map;

public class BinderRegistry {

    private static Map<String, IComponentBinder> registry = new HashMap<>();

    public static void add(String key, IComponentBinder eventBinder) {

        registry.put(key, eventBinder);

    }

    public static IComponentBinder get(String key) {

        return registry.get(key);

    }


}
