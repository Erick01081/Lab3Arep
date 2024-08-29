package edu.escuelaing.arem.ASE.app;

@RestController
public class PiService {
    @GetMapping("/pi")
    public static String pi() {
        return "PI: " + Math.PI;
    }

}
