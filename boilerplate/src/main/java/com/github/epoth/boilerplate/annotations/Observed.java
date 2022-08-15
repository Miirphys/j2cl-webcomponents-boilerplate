package com.github.epoth.boilerplate.annotations;

public @interface Observed {

    String function() default "";

    String attribute() default "";

}
