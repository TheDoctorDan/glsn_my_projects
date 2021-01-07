class Person {
    String name;
    int age;

    Person(String n, int a) {
        name = n;
        age = a;
    }

    void printPerson() {
        System.out.print("Hi, my name is " + name);
        System.out.println(". I am " + age + " years old.");
    }

    public static void main (String arguments[]) {
        Person p;
        p = new Person("Luke", 50);
        p.printPerson();
        System.out.println("----");
        p = new Person("Laura", 35);
        p.printPerson();
        System.out.println("----");
    }
}
