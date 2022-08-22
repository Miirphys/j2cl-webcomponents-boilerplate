package com.github.epoth.webcomponents.generator.observed;

public class ObservedBinding {

    public static final int METHOD = 0;

    public static final int FIELD = 1;

    public String attribute;

    public String method;

    public String field;

    public int type;

    public String targetType;

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("type=").append(type).append(",");

        stringBuilder.append("attribute=").append(attribute).append(",");

        stringBuilder.append("method=").append(method).append(",");

        stringBuilder.append("field=").append(field);

        stringBuilder.append("targetType=").append(targetType);

        return stringBuilder.toString();

    }
}
