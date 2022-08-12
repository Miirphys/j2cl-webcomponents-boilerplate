package com.github.epoth.webcomponents.generator;

import com.github.epoth.webcomponents.annotations.WebComponent;
import com.google.common.annotations.GwtIncompatible;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

@GwtIncompatible
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Generator extends AbstractProcessor {

    private static final String DEFINE_COMPONENT_PATTERN = "elemental2.dom.DomGlobal.customElements.define($S,$L.class)";

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

        if (classes.size() == 0) {

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "nothing to do ...");

            return false;

        }

        /* */

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "generating declarations for " + classes.size() + " components ");

        /* */

        StringBuilder nativeBootstrapBuilder = new StringBuilder();

        nativeBootstrapBuilder.append(" setTimeout( function() { ");
        nativeBootstrapBuilder.append(" var ep = Module.$create__();");
        nativeBootstrapBuilder.append(" ep.m_onLoad__();");
        nativeBootstrapBuilder.append(" }, 0); ");

        /* */

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("Module").addModifiers(Modifier.PUBLIC);

        /* */

        CodeBlock.Builder codeBuilder = CodeBlock.builder();

        for (Component component : components) {

            codeBuilder.addStatement(DEFINE_COMPONENT_PATTERN, component.tagName, component.className);

        }

        /* */

        MethodSpec.Builder onLoadMethodBuilder = MethodSpec.methodBuilder("onLoad");

        onLoadMethodBuilder.addModifiers(Modifier.PUBLIC);

        onLoadMethodBuilder.addCode(codeBuilder.build());


        /* */

        classBuilder.addMethod(onLoadMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder("com.boilerplate.boot", classBuilder.build()).build();

        try {

            javaFile.writeTo(processingEnv.getFiler());

            FileObject jsBoostrap = processingEnv.getFiler().createResource(

                    StandardLocation.SOURCE_OUTPUT,

                    "", "com/boilerplate/boot/Module.native.js"

            );

            Writer writer = jsBoostrap.openWriter();

            writer.write(nativeBootstrapBuilder.toString());

            writer.flush();

            writer.close();

        } catch (IOException ioException) {

            throw new UncheckedIOException(ioException);

        }

        /* */

        return true;

    }

    private void processElement(Element element) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "+ processing - " + element.toString());

        WebComponent component = element.getAnnotation(WebComponent.class);

        if (component.tagName() != null) {

            Component comp = new Component();

            comp.setTagName(component.tagName());
            comp.setTemplateUrl(component.templateUrl());
            comp.setClassName(element.toString());

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "+ extracting - " + comp.toString());

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
