package edu.escuelaing.arem.ASE.app;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GreetingController {

    public String value();


}
