package edu.escuelaing.arem.ASE.app.MyOwnJUnit;

import java.lang.reflect.Method;

public class JUnitECI {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, Exception {
        Class<?> c = Class.forName(args[0]);

        Method[] methods = c.getDeclaredMethods();
        int passed = 0;
        int failed = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                try {
                    method.invoke(null);
                    System.out.println("Test " + method.getName() + " passed.");
                    passed++;
                } catch (Exception e) {
                    System.out.println("Test " + method.getName() + " failed: " + e.getCause());
                    failed++;
                }
            }
        }

        System.out.println("Total passed: " + passed);
        System.out.println("Total failed: " + failed);
    }

    public static void main(int[] enteros){};
}