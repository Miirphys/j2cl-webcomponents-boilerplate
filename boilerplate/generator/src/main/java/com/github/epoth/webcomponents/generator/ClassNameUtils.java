package com.github.epoth.webcomponents.generator;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public class ClassNameUtils {

    public static String getSimpleLowerClassName(String className) {

        return className.substring(className.lastIndexOf('.') + 1).toLowerCase();

    }


    public static String getSimpleClassName(String className) {

        return className.substring(className.lastIndexOf('.') + 1);

    }


    public static String getPackagePath(String className) {

        return className.substring(0, className.lastIndexOf('.')).replaceAll("\\.", "/");

    }


}



