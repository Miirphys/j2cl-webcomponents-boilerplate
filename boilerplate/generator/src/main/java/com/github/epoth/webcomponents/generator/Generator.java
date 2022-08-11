package com.github.epoth.webcomponents.generator;

import com.github.epoth.webcomponents.annotations.WebComponent;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Generator extends AbstractProcessor {

    private Logger logger = Logger.getLogger(Generator.class.getName());

    private ArrayList<Component> components = new ArrayList<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        return Collections.singleton("com.github.epoth.webcomponents.annotations.WebComponent");

    }

    @Override
    public boolean process(

            Set<? extends TypeElement> annotations,

            RoundEnvironment roundEnv

    ) {

        Set<? extends Element> classes = roundEnv.getElementsAnnotatedWith(WebComponent.class);

        for (Element element : classes) {

            processElement(element);

        }

        return false;
    }

    private void processElement(Element element) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "processing " + element.toString());

        WebComponent component = element.getAnnotation(WebComponent.class);

        if (component.tagName() != null) {

            Component comp = new Component();

            comp.setTagName(component.tagName());
            comp.setTemplateUrl(component.templateUrl());
            comp.setClassName(element.toString());

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "extracting " + comp.toString());

            components.add(comp);

        }

    }

    private static class Component {

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

        @Override
        public String toString() {

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("{");

            stringBuilder.append("className=").append(className).append(",");

            stringBuilder.append("tagName=").append(tagName).append(",");

            stringBuilder.append("templateUrl=").append(templateUrl);

            stringBuilder.append("}");

            return stringBuilder.toString();
        }
    }


}
