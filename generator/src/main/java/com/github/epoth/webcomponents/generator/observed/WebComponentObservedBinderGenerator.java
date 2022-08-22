package com.github.epoth.webcomponents.generator.observed;


import com.github.epoth.boilerplate.Component;
import com.github.epoth.boilerplate.WebComponentObservedBinder;
import com.github.epoth.boilerplate.annotations.Observed;
import com.github.epoth.webcomponents.generator.ClassNameUtils;
import com.github.epoth.webcomponents.generator.ComponentDefinition;
import com.google.common.annotations.GwtIncompatible;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
public class WebComponentObservedBinderGenerator {

    private HashMap<String, List<ObservedBinding>> bindingsMap = new HashMap<>();

    public void generate(

            ProcessingEnvironment processingEnvironment,

            ComponentDefinition componentDefinition,

            CodeBlock.Builder codeBuilder

    ) throws IOException {

        for (Element element : componentDefinition.getClassElement().getEnclosedElements()) {

            Observed observed = element.getAnnotation(Observed.class);

            if (observed != null) {

                processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "+ extracting - " + observed.toString());

                ObservedBinding observedBinding = new ObservedBinding();

                if (element.getKind() == ElementKind.METHOD) {

                    ExecutableElement method = (ExecutableElement) element;

                    int parametersCount = method.getParameters().size();

                    if (parametersCount == 0) {

                        throw new NoParameterException(method.getSimpleName().toString(), componentDefinition.getClassName());

                    }

                    if (parametersCount > 1) {

                        throw new OnlyOneParameterRequiredException(method.getSimpleName().toString(), componentDefinition.getClassName());

                    }

                    observedBinding.targetType = method.getParameters().get(0).asType().toString();
                    observedBinding.type = ObservedBinding.METHOD;
                    observedBinding.attribute = observed.attribute();
                    observedBinding.method = element.getSimpleName().toString();

                    List<ObservedBinding> bindingsList = null;

                    if (bindingsMap.containsKey(observedBinding.attribute)) {

                        bindingsList = bindingsMap.get(observedBinding.attribute);

                    } else {

                        bindingsList = new ArrayList<>();

                        bindingsMap.put(observedBinding.attribute, bindingsList);

                    }

                    bindingsList.add(observedBinding);

                }

            }

        }

        String simpleClassName = ClassNameUtils.simpleClassName(componentDefinition.getClassName());

        StringBuilder binderClassNameBuilder = new StringBuilder();

        binderClassNameBuilder.append(simpleClassName).append("ObservedBinder$");

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(binderClassNameBuilder.toString()).addModifiers(Modifier.PUBLIC);

        classBuilder.addSuperinterface(WebComponentObservedBinder.class);

        /* */

        StringBuilder attributesArrayBuilder = new StringBuilder();

        attributesArrayBuilder.append("new String[]{");

        Iterator<String> attributeNameIterator = bindingsMap.keySet().iterator();

        while (attributeNameIterator.hasNext()) {

            String attributeName = attributeNameIterator.next();

            attributesArrayBuilder.append("\"").append(attributeName).append("\"");

            if (attributeNameIterator.hasNext()) attributesArrayBuilder.append(",");

        }

        attributesArrayBuilder.append("}");

        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "+ generating observed attributes - " + attributesArrayBuilder.toString());

        MethodSpec.Builder getObservedAttributesMethodBuilder = MethodSpec
                .methodBuilder("getObservedAttributes")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);

        getObservedAttributesMethodBuilder.addStatement("return $L", attributesArrayBuilder.toString());

        classBuilder.addMethod(getObservedAttributesMethodBuilder.build());

        /* */

        MethodSpec.Builder onAttributeChangeMethodBuilder = MethodSpec.methodBuilder("onAttributeChange").addModifiers(Modifier.PUBLIC);

        ParameterSpec.Builder nameParameterBuilder = ParameterSpec.builder(String.class, "name", Modifier.FINAL);
        ParameterSpec.Builder valueParameterBuilder = ParameterSpec.builder(Object.class, "value", Modifier.FINAL);
        ParameterSpec.Builder componentParameterBuilder = ParameterSpec.builder(Component.class, "component", Modifier.FINAL);

        onAttributeChangeMethodBuilder.addParameter(nameParameterBuilder.build());
        onAttributeChangeMethodBuilder.addParameter(valueParameterBuilder.build());
        onAttributeChangeMethodBuilder.addParameter(componentParameterBuilder.build());

        CodeBlock.Builder onAttributeChangeMethodContentBuilder = CodeBlock.builder();

        onAttributeChangeMethodBuilder.addStatement(

                "$L cp = ($L)component",

                componentDefinition.getClassName(),

                componentDefinition.getClassName()

        );

        attributeNameIterator = bindingsMap.keySet().iterator();

        while (attributeNameIterator.hasNext()) {

            String attributeName = attributeNameIterator.next();

            List<ObservedBinding> bindings = bindingsMap.get(attributeName);

            onAttributeChangeMethodBuilder.beginControlFlow(

                    "if(name.equals($S))",

                    attributeName
            );


            for (ObservedBinding binding : bindings) {

                switch (binding.type) {

                    case ObservedBinding.METHOD:

                        onAttributeChangeMethodBuilder.addStatement(

                                "cp.$L(($L)value)",

                                binding.method,

                                binding.targetType
                        );

                        break;

                    case ObservedBinding.FIELD:

                        onAttributeChangeMethodBuilder.addStatement(

                                "cp.$L = value",

                                binding.field
                        );

                }

            }

            onAttributeChangeMethodBuilder.addStatement("return", "");

            onAttributeChangeMethodBuilder.endControlFlow();

        }

        onAttributeChangeMethodBuilder.addCode(onAttributeChangeMethodContentBuilder.build());

        /* */

        classBuilder.addMethod(onAttributeChangeMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder("com.github.epoth.boilerplate", classBuilder.build()).build();

        javaFile.writeTo(processingEnvironment.getFiler());

        codeBuilder.addStatement(

                "$L.__ObservedBinder = new com.github.epoth.boilerplate.$L()",

                componentDefinition.getClassName(),

                binderClassNameBuilder.toString()

        );

    }
}
