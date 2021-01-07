public class CountInstances {
    private static int numInstances = 0;

    protected static int getNumInstances() {
        return numInstances;
    }

    private static void addInstance() {
        numInstances++;
    }

    CountInstances() {
        CountInstances.addInstance();
    }

    public static void main(String arguments[]) {
        System.out.println("Starting with " +
            CountInstances.getNumInstances() + " instances");
        for (int  i = 0; i < 10; ++i)
            new CountInstances();
        System.out.println("Created " +
            CountInstances.getNumInstances() + " instances");
    }
}
