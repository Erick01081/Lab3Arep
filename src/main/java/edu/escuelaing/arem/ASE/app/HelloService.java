package edu.escuelaing.arem.ASE.app;

@RestController
public class HelloService {
    @GetMapping("/hello")
    public static String hello() {
        return "Hello World!";
    }
    @GetMapping("/pi")
    public static String pi() {
        return "PI: " + Math.PI;
    }

    @GetMapping("/Capitalized")
    public static String Capitalized() {
        return "Capitalized: " + "Hello World!".toUpperCase();
    }

    @GetMapping("/horaydia")
    public static String horaydia() {
        return "Hora y dia: " + java.time.LocalDateTime.now();
    }
}
