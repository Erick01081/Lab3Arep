package edu.escuelaing.arem.ASE.app.MyOwnSpringboot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GreetingController {

    public String value();


}
