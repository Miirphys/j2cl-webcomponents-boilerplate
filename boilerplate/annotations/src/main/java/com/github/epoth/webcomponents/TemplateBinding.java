package com.github.epoth.webcomponents;

public class TemplateBinding {

    public static final int FIELD = 0;
    public static final int FUNCTION = 1;

    private String id;
    private String field;
    private String function;

    private String event;
    private int type;

    public TemplateBinding(int type) {

        this.type = type;

    }

    public int getType() {

        return type;
    }

    public void setType(int type) {

        this.type = type;

    }

    public String getId() {

        return id;

    }

    public void setId(String id) {

        this.id = id;

    }

    public String getFunction() {

        return function;

    }

    public void setFunction(String function) {

        this.function = function;

    }

    public String getField() {

        return field;

    }

    public void setField(String field) {

        this.field = field;

    }

    public String getEvent() {

        return event;

    }

    public void setEvent(String event) {

        this.event = event;

    }
}
