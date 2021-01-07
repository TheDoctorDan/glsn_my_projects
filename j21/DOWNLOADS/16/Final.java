class Final {
    int[] num1 = { 12, 15, 10, 8, -1, 7 };
    int[] num2 = { 1, 5, 20, 8, 1, 13 };

    public static void main(String[] arguments) {
        Final fin = new Final();
        System.out.println("First array: ");
        fin.readNumbers(fin.num1);
        System.out.println("Second array: ");
        fin.readNumbers(fin.num2);
    }

    void readNumbers(int[] numArray) {
        int count = 0;
        int lastNum = 0;
        try {
            while (count < numArray.length) {
                lastNum = numArray[count++];
                if (lastNum == -1)
                    return;
            }
        } finally {
            System.out.println("Last number read: " + lastNum);
        }
        return;
    }
}
