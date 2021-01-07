class CheckString {

    public static void main(String arguments[]) {
        String str = "In my next life, I will believe in reincarnation";
        System.out.println("The string is: " + str);
        System.out.println("Length of this string: "
            + str.length());
        System.out.println("The character at position 7: "
            + str.charAt(7));
        System.out.println("The substring from 24 to 31: "
            + str.substring(24, 31));
        System.out.println("The index of the character x: "
            + str.indexOf('x'));
        System.out.println("The index of the beginning of the "
            + "substring \"will\": " + str.indexOf("will"));
        System.out.println("The string in upper case: "
            + str.toUpperCase());
    }
}
