package com.github.epoth.boilerplate;

public interface WebComponentObservedBinder {

    String[] getObservedAttributes();

    void onAttributeChange(String name, Object value, Component component);

}
