package com.github.epoth.webcomponents.generator;

import com.github.epoth.boilerplate.Component;
import com.github.epoth.boilerplate.WebComponentBinder;
import com.google.common.annotations.GwtIncompatible;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.io.IOException;
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
public class WebComponentBinderGenerator {

    void generate(

            ProcessingEnvironment processingEnvironment,

            Element classElement,

            String className,

            List<TemplateBinding> templateBindingList,

            CodeBlock.Builder codeBuilder

    ) throws IOException {


        String simpleClassName = ClassNameUtils.simpleClassName(className);

        /* */

        StringBuilder binderClassNameBuilder = new StringBuilder();

        binderClassNameBuilder.append(simpleClassName).append("Binder$");

        /* */

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(binderClassNameBuilder.toString()).addModifiers(Modifier.PUBLIC);

        classBuilder.addSuperinterface(WebComponentBinder.class);

        MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind").addModifiers(Modifier.PUBLIC);

        ParameterSpec.Builder componentParameterBuilder = ParameterSpec.builder(Component.class, "component", Modifier.FINAL);

        bindMethodBuilder.addParameter(componentParameterBuilder.build());

        CodeBlock.Builder bindMethodContentBuilder = CodeBlock.builder();

        // generate cast to component type

        bindMethodBuilder.addStatement(

                "$L cp = ($L)component",

                className,

                className
        );

        for (TemplateBinding binding : templateBindingList) {

            switch (binding.getType()) {

                case TemplateBinding.FIELD:

                    for (Element element : classElement.getEnclosedElements()) {

                        if (element.toString().equals(binding.getField())) {

                            bindMethodContentBuilder.addStatement(

                                    "cp.$L = ($L) cp.getElementById($S)",

                                    binding.getField(),

                                    "" + element.asType(),

                                    binding.getId()

                            );

                            break;

                        }

                    }

                    break;

                case TemplateBinding.FUNCTION:

                    // generate binding from template specific element to component function

                    bindMethodContentBuilder.addStatement(

                            "cp.getElementById($S).addEventListener($S,cp::$L)",

                            binding.getId(),

                            binding.getEvent(),

                            binding.getFunction()

                    );

            }

        }

        bindMethodBuilder.addCode(bindMethodContentBuilder.build());

        classBuilder.addMethod(bindMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder("com.github.epoth.boilerplate", classBuilder.build()).build();

        javaFile.writeTo(processingEnvironment.getFiler());

        // statically set the generated binder class

        codeBuilder.addStatement(

                "$L.__Binder = new com.github.epoth.boilerplate.$L()",

                className,

                binderClassNameBuilder.toString()

        );

    }

}
