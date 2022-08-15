package com.github.epoth.webcomponents.generator;

import javax.lang.model.element.Element;

public class ComponentDefinition {

    private int mode;

    private Element classElement;

    private String className;

    private String tagName;

    private String templateUrl;

    public String getTagName() {

        return tagName;

    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {

        this.className = className;

    }

    public Element getClassElement() {
        return classElement;
    }

    public void setClassElement(Element classElement) {
        this.classElement = classElement;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        stringBuilder.append("mode=").append(mode).append(",");

        stringBuilder.append("className=").append(className).append(",");

        stringBuilder.append("tagName=").append(tagName).append(",");

        stringBuilder.append("templateUrl=").append(templateUrl);

        stringBuilder.append("}");

        return stringBuilder.toString();
    }


}
