import java.util.Random;

class RandomNumbers {

    public static void main(String arguments[]) {
        Random r1, r2;

        r1 = new Random();
        System.out.println("Random value 1: " + r1.nextDouble());

        r2 = new Random(8675309);
        System.out.println("Random value 2: " + r2.nextDouble());
    }
}
