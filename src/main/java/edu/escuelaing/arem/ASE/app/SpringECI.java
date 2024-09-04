package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.MyOwnSpringboot.GetMapping;
import edu.escuelaing.arem.ASE.app.MyOwnSpringboot.RequestParam;
import edu.escuelaing.arem.ASE.app.MyOwnSpringboot.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Utility class to invoke methods in a service class based on URL mapping.
 * Demonstrates method invocation with parameters extracted from query strings.
 */
public class SpringECI {

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("No class provided. Please provide a fully qualified class name.");
            }

            // Load the class dynamically using reflection
            Class<?> serviceClass = Class.forName(args[0]);
            Map<String, Method> services = new HashMap<>();

            if (serviceClass.isAnnotationPresent(RestController.class)) {
                // Populate service methods map based on @GetMapping annotation
                for (Method method : serviceClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        String endpoint = method.getAnnotation(GetMapping.class).value();
                        services.put(endpoint, method);
                    }
                }
            }

            // Example URL for testing
            URL serviceUrl = new URL("http://localhost:8080/App/greeting?name=Erick");
            String path = serviceUrl.getPath();
            String query = serviceUrl.getQuery();

            System.out.println("Path: " + path);
            System.out.println("Query: " + query);

            // Extract service name from path
            String serviceName = "/" + extractServiceName(path);
            System.out.println("Service Name: " + serviceName);

            Method targetMethod = services.get(serviceName);

            if (targetMethod != null) {
                Object[] methodArguments = extractArguments(targetMethod, query);
                // Invoke the target method
                System.out.println("Service Response: " + targetMethod.invoke(null, methodArguments));
            } else {
                System.out.println("Service not found: " + serviceName);
            }

            URL serviceUrl1 = new URL("http://localhost:8080/App/horaydia");
            String path1 = serviceUrl1.getPath();
            String query1 = serviceUrl1.getQuery();

            System.out.println("Path: " + path1);
            System.out.println("Query: " + query1);

            // Extract service name from path
            String serviceName1 = "/" + extractServiceName(path1);
            System.out.println("Service Name: " + serviceName1);

            Method targetMethod1 = services.get(serviceName1);

            if (targetMethod != null) {
                Object[] methodArguments1 = extractArguments(targetMethod1, query);
                // Invoke the target method
                System.out.println("Service Response: " + targetMethod1.invoke(null, methodArguments1));
            } else {
                System.out.println("Service not found: " + serviceName1);
            }



        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format: " + e.getMessage());
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.err.println("Error invoking method: " + e.getCause());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Extracts method arguments from the query string based on the method parameters and annotations.
     *
     * @param method the method for which to extract arguments
     * @param query  the query string from the URL
     * @return an array of arguments to be passed to the method
     */
    private static Object[] extractArguments(Method method, String query) {
        Map<String, String> queryParams = parseQuery(query);
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameters[i].getAnnotation(RequestParam.class);
                String paramName = annotation.value();
                String value = queryParams.getOrDefault(paramName, annotation.defaultValue());
                args[i] = value;
            }
        }
        return args;
    }

    /**
     * Parses a query string into a map of query parameters.
     *
     * @param query the query string
     * @return a map of query parameters
     */
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    /**
     * Extracts the service name from the URL path.
     *
     * @param path the path of the URL
     * @return the service name
     */
    private static String extractServiceName(String path) {
        if (path != null && path.startsWith("/App/")) {
            return path.substring(5);  // Removes "/App/" to get the service name
        }
        return "";
    }
}
