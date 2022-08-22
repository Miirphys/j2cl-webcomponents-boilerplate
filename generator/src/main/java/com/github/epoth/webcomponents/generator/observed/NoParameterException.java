package com.github.epoth.webcomponents.generator.observed;

import java.text.MessageFormat;

public class NoParameterException extends RuntimeException {

    private static final String MESSAGE = "Method {0} from {1} has no parameter , one parameter required .";

    public NoParameterException(String methodName, String className) {

        super(MessageFormat.format(MESSAGE, methodName, className));

    }


}
