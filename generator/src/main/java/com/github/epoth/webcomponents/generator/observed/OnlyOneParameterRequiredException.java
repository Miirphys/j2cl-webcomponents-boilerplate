package com.github.epoth.webcomponents.generator.observed;

import java.text.MessageFormat;

public class OnlyOneParameterRequiredException extends RuntimeException {

    private static final String MESSAGE = "Method {0} from {1} has multiple parameters , only one parameter required .";

    public OnlyOneParameterRequiredException(String methodName, String className) {

        super(MessageFormat.format(MESSAGE, methodName, className));

    }
}
