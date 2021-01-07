class NumberReader {

    String convertNum(int val) {
        switch (val) {
            case 0: return "zero ";
            case 1: return "one ";
            case 2: return "two ";
            case 3: return "three ";
            case 4: return "four ";
            case 5: return "five ";
            case 6: return "six ";
            case 7: return "seven ";
            case 8: return "eight ";
            case 9: return "nine ";
            default: return " ";
        }
    }

    public static void main (String arguments[]) {
        NumberReader n = new NumberReader();
        String num = n.convertNum(4) + n.convertNum(1) + n.convertNum(3);
        System.out.println("413 converts to " + num);
    }
}
