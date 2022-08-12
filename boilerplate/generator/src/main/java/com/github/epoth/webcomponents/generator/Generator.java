package com.github.epoth.webcomponents.generator;

import com.github.epoth.webcomponents.annotations.WebComponent;
import com.google.common.annotations.GwtIncompatible;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@GwtIncompatible
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Generator extends AbstractProcessor {

    private static final String DEFINE_COMPONENT_PATTERN = "elemental2.dom.DomGlobal.customElements.define($S,$L.class)";
    private static final String HTML_TEMPLATE_ELEMENT_CREATION = "elemental2.dom.HTMLTemplateElement $L_template = (elemental2.dom.HTMLTemplateElement) elemental2.dom.DomGlobal.document.createElement(\"template\")";
    private static final String HTML_TEMPLATE_ELEMENT_SET_INNER = "$L_template.innerHTML=$S";
    private static final String HTML_TEMPLATE_BIND_TO_HEAD = "elemental2.dom.DomGlobal.document.head.append($L_template)";

    private static final String HTML_TEMPLATE_REGISTRY_ADD = "com.github.epoth.webcomponents.TemplateRegistry.add($S,$L_template)";

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

        CodeBlock.Builder createTemplateCodeBlockBuilder = CodeBlock.builder();

        /* */

        CodeBlock.Builder defineComponentCodeBlockBuilder = CodeBlock.builder();

        StringBuilder templatePathBuilder = new StringBuilder();

        /* */

        for (Component component : components) {

            /* */

            defineComponentCodeBlockBuilder.addStatement(DEFINE_COMPONENT_PATTERN, component.tagName, component.className);

            String simpleClassName = getSimpleClassName(component.className);
            String packagePath = getPackagePath(component.className);

            templatePathBuilder.append(packagePath).append("/").append(component.templateUrl);

            /* */

            String templateContents = null;

            try {

                templateContents = getStringContentsOfPath(processingEnv.getFiler(), templatePathBuilder.toString()).toString();

                createTemplateCodeBlockBuilder.addStatement(HTML_TEMPLATE_ELEMENT_CREATION, simpleClassName);
                createTemplateCodeBlockBuilder.addStatement(HTML_TEMPLATE_ELEMENT_SET_INNER, simpleClassName, templateContents);
                createTemplateCodeBlockBuilder.addStatement(HTML_TEMPLATE_BIND_TO_HEAD, simpleClassName);
                createTemplateCodeBlockBuilder.addStatement(HTML_TEMPLATE_REGISTRY_ADD, simpleClassName, simpleClassName);

            } catch (IOException ioException) {

                throw new RuntimeException(ioException);

            }

            /* */

        }

        classBuilder.addStaticBlock(createTemplateCodeBlockBuilder.build());

        /* */

        MethodSpec.Builder onLoadMethodBuilder = MethodSpec.methodBuilder("onLoad");

        onLoadMethodBuilder.addModifiers(Modifier.PUBLIC);

        onLoadMethodBuilder.addCode(defineComponentCodeBlockBuilder.build());

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

    public String getSimpleClassName(String className) {

        return className.substring(className.lastIndexOf('.') + 1).toLowerCase();

    }

    public String getPackagePath(String className) {

        return className.substring(0, className.lastIndexOf('.')).replaceAll("\\.", "/");

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

    private CharSequence getStringContentsOfPath(Filer filer, String path) throws IOException {
        for (JavaFileManager.Location location : Arrays.asList(
                StandardLocation.SOURCE_PATH,
                StandardLocation.SOURCE_OUTPUT,
                StandardLocation.CLASS_PATH,
                StandardLocation.CLASS_OUTPUT,
                StandardLocation.ANNOTATION_PROCESSOR_PATH
        )) {
            try {
                FileObject resource = filer.getResource(location, "", path);
                if (resource != null && new File(resource.getName()).exists()) {
                    return resource.getCharContent(false);
                }
            } catch (IOException e) {
                //ignore, look in the next entry
            }
        }
        try (InputStream inputStream = getClass().getResourceAsStream("/" + path)) {
            if (inputStream != null) {
                final char[] buffer = new char[1024];
                final StringBuilder out = new StringBuilder();
                try (Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    while (true) {
                        int rsz = in.read(buffer, 0, buffer.length);
                        if (rsz < 0)
                            break;
                        out.append(buffer, 0, rsz);
                    }
                }
                return out.toString();
            }
            throw new IllegalStateException("Failed to find resource " + path);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource " + path, e);
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
