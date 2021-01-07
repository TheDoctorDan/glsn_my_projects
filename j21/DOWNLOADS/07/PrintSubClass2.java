class PrintSubClass2 extends PrintClass {
    int z = 3;

    void printMe() {
        System.out.println("x is " + x + ", y is " + y +
               ", z is " + z);
        System.out.println("I am an instance of the class " +
               this.getClass().getName());
    }

    public static void main(String arguments[]) {
        PrintSubClass2 obj = new PrintSubClass2();
        obj.printMe();
    }
}
