import java.lang.reflect.*;
import java.util.Random;

class SeeMethods {
    public static void main(String[] arguments)  {
        Random rd = new Random();
        Class className = rd.getClass();
        Method[] methods = className.getMethods();
        for (int i = 0; i < methods.length; i++) {
            System.out.println("Method: " + methods[i]);
        }
    }
}
