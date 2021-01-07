class Jabberwock {
    String color;
    String sex;
    boolean hungry;

    void feedJabberwock() {
        if (hungry == true) {
            System.out.println("Yum -- a peasant!");
            hungry = false;
        } else
            System.out.println("No, thanks -- already ate.");
    }

    void showAttributes() {
        System.out.println("This is a " + sex + " " + color +
            " jabberwock.");
        if (hungry == true)
            System.out.println("The jabberwock is hungry.");
        else
            System.out.println("The jabberwock is full.");
    }

    public static void main (String arguments[]) {
        Jabberwock j = new Jabberwock();
        j.color = "orange";
        j.sex = "male";
        j.hungry = true;
        System.out.println("Calling showAttributes ...");
        j.showAttributes();
        System.out.println("-----");
        System.out.println("Feeding the jabberwock ...");
        j.feedJabberwock();
        System.out.println("-----");
        System.out.println("Calling showAttributes ...");
        j.showAttributes();
        System.out.println("-----");
        System.out.println("Feeding the jabberwock ...");
        j.feedJabberwock();
    }
}

