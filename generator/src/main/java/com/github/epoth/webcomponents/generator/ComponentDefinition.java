package com.github.epoth.webcomponents.generator;

import com.google.common.annotations.GwtIncompatible;

import javax.lang.model.element.Element;

/**
 * Copyright 2022 Eric Ponthiaux -/- ponthiaux.eric@gmail.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@GwtIncompatible
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
