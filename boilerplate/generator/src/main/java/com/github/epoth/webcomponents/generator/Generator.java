package com.github.epoth.webcomponents.generator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Generator extends AbstractProcessor {

    private Logger logger = Logger.getLogger(Generator.class.getName());

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        return Collections.singleton("com.github.epoth.webcomponents.annotations.WebComponent");

    }

    @Override
    public boolean process(

            Set<? extends TypeElement> annotations,

            RoundEnvironment roundEnv

    ) {

        System.out.println("PROCESS *********");

        return false;
    }

}
