class AmoebaMath {
    public static void main (String arguments[]) {
        int x = 6;
        short y = 4;
        float a = .12f;

        System.out.println("You start with " + x + " pet amoebas.");
        System.out.println("\tTwo get married and their spouses move in.");
        x = x + 2;
        System.out.println("You now have " + x);

        System.out.println("\tMitosis occurs, doubling the number of amoebas.");
        x = x * 2;
        System.out.println("You now have " + x);

        System.out.println("\tThere's a fight. " + y + " amoebas move out.");
        x = x - y;
        System.out.println("You now have " + x);

        System.out.println("\tParamecia attack! You lose one-third of the colony.");
        x = x - (x / 3);
        System.out.println("You end up with " + x + " pet amoebas.");
        System.out.println("Daily upkeep cost per amoeba: $" + a);
        System.out.println("Total daily cost: $" + (a * x));
    }
}
