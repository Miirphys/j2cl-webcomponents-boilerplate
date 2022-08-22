package com.github.epoth.webcomponents.generator;

import com.github.epoth.boilerplate.Component;
import com.github.epoth.boilerplate.WebComponentInitializer;
import com.google.common.annotations.GwtIncompatible;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import elemental2.dom.ShadowRoot;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;

import static com.github.epoth.boilerplate.annotations.WebComponent.CLOSED;
import static com.github.epoth.boilerplate.annotations.WebComponent.FREE;
import static com.github.epoth.boilerplate.annotations.WebComponent.OPEN;

@GwtIncompatible
public class WebComponentInitializerGenerator {

    void generate(

            ProcessingEnvironment processingEnvironment,

            ComponentDefinition component,

            CodeBlock.Builder codeBuilder

    ) throws IOException {


        String simpleClassName = ClassNameUtils.simpleClassName(component.getClassName());

        /* */

        StringBuilder initializerClassNameBuilder = new StringBuilder();

        initializerClassNameBuilder.append(simpleClassName).append("Initializer$");

        /* */

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(initializerClassNameBuilder.toString()).addModifiers(Modifier.PUBLIC);

        classBuilder.addSuperinterface(WebComponentInitializer.class);

        MethodSpec.Builder initializeMethodBuilder = MethodSpec.methodBuilder("initialize").addModifiers(Modifier.PUBLIC);

        ParameterSpec.Builder componentParameterBuilder = ParameterSpec.builder(Component.class, "component", Modifier.FINAL);

        initializeMethodBuilder.addParameter(componentParameterBuilder.build());

        CodeBlock.Builder initializeMethodContentBuilder = CodeBlock.builder();

        initializeMethodBuilder.returns(ShadowRoot.class);

        // generate cast to component type

        initializeMethodBuilder.addStatement(

                "$L cp = ($L)component",

                component.getClassName(),

                component.getClassName()
        );

        switch (component.getMode()) {

            case OPEN:

                initializeMethodBuilder.addStatement("elemental2.dom.HTMLElement.AttachShadowOptionsType openOptions = elemental2.dom.HTMLElement.AttachShadowOptionsType.create()","");

                initializeMethodBuilder.addStatement("openOptions.setMode(\"open\")","");

                initializeMethodBuilder.addStatement("return cp.attachShadow(openOptions)","");

                break;

            case CLOSED:

                initializeMethodBuilder.addStatement("elemental2.dom.HTMLElement.AttachShadowOptionsType openOptions = elemental2.dom.HTMLElement.AttachShadowOptionsType.create()","");

                initializeMethodBuilder.addStatement("openOptions.setMode(\"closed\")","");

                initializeMethodBuilder.addStatement("return cp.attachShadow(openOptions)","");

                break;

            case FREE:

                initializeMethodBuilder.addStatement("return null","");

        }


        initializeMethodBuilder.addCode(initializeMethodContentBuilder.build());

        classBuilder.addMethod(initializeMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder("com.github.epoth.boilerplate", classBuilder.build()).build();

        javaFile.writeTo(processingEnvironment.getFiler());

        // statically set the generated binder class

        codeBuilder.addStatement(

                "$L.__Initializer = new com.github.epoth.boilerplate.$L()",

                component.getClassName(),

                initializerClassNameBuilder.toString()

        );

    }
}
