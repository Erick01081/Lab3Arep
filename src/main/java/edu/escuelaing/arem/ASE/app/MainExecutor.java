package edu.escuelaing.arem.ASE.app;

import java.lang.reflect.Method;

public class MainExecutor {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, Exception {
        Class c = Class.forName(args[0]);

        Class[] mainParameterTypes = {String[].class};

        Method main = c.getDeclaredMethod("main", mainParameterTypes);

        String[] params = {args[1], args[2]};

        main.invoke(null, (Object) params);
    }
}
