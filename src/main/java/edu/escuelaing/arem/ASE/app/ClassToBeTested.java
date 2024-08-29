package edu.escuelaing.arem.ASE.app;

public class ClassToBeTested {
    @Test
    public static void m1(){
        System.out.println("Ok.");
    }
    @Test
    public static void m2(){
        System.out.println("Ok.");
    }
    @Test
    public static void m3(){
        System.out.println("Ok.");
    }
    @Test
    public static void m4() throws Exception{
        throw new Exception("Error from m4");
    }
    @Test
    public static void m5() throws Exception{
        throw new Exception("Error from m5");
    }
}
