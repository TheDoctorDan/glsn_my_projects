class EvenSteven {

    void evenCheck(int val) {
        System.out.println("Value is "
            + val + ". ");
        if (val % 2 == 0)
        System.out.println("Steven!");
    }

    public static void main (String arguments[]) {
        EvenSteven e = new EvenSteven();

        e.evenCheck(1);
        e.evenCheck(2);
        e.evenCheck(54);
        e.evenCheck(77);
        e.evenCheck(1346);
    }
}
