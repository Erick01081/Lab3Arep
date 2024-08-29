package edu.escuelaing.arem.ASE.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpringECI {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, MalformedURLException {
        Class c = Class.forName(args[0]);
        Map<String, Method> services = new HashMap();

        // Cargar los componentes
        if(c.isAnnotationPresent(RestController.class)){
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String key = method.getAnnotation(GetMapping.class).value();
                    services.put(key, method);
                }
            }
        }

        // Codigo quemado para ejemplo

        URL serviceurl = new URL("http://localhost:8080/App/hello");
        String path = serviceurl.getPath();
        System.out.println("Path: " + path);
        String serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        Method ms = services.get(serviceName);
        System.out.println("Respuesta servicio: " + ms.invoke(null));

        URL serviceUrlPi = new URL("http://localhost:8080/App/pi");
        String pathPi = serviceUrlPi.getPath();
        System.out.println("Path 2: " + pathPi);
        String serviceNamePi = pathPi.substring(4); // Eliminar "/App"
        System.out.println("Service name 2: " + serviceNamePi);

        // Invocar el método correspondiente para el segundo URL
        Method msPi = services.get(serviceNamePi);
        System.out.println("Respuesta servicio 2: " + msPi.invoke(null));

        URL serviceUrlCapitalized = new URL("http://localhost:8080/App/Capitalized");
        String pathCapitalized = serviceUrlCapitalized.getPath();
        System.out.println("Path 3: " + pathCapitalized);
        String serviceNameCapitalized = pathCapitalized.substring(4); // Eliminar "/App"
        System.out.println("Service name 3: " + serviceNameCapitalized);

        // Invocar el método correspondiente para el tercer URL
        Method msCapitalized = services.get(serviceNameCapitalized);
        System.out.println("Respuesta servicio 3: " + msCapitalized.invoke(null));

        URL serviceUrlHoraydia = new URL("http://localhost:8080/App/horaydia");
        String pathHoraydia = serviceUrlHoraydia.getPath();
        System.out.println("Path 4: " + pathHoraydia);
        String serviceNameHoraydia = pathHoraydia.substring(4); // Eliminar "/App"
        System.out.println("Service name 4: " + serviceNameHoraydia);

        // Invocar el método correspondiente para el cuarto URL
        Method msHoraydia = services.get(serviceNameHoraydia);
        System.out.println("Respuesta servicio 4: " + msHoraydia.invoke(null));
    }
}
