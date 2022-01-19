
class ClassCountingInstances {

    private static long numberOfInstances;

    public static synchronized void inc() {
        numberOfInstances++;
    }

    public ClassCountingInstances() {
        inc();
    }

    public static synchronized long getNumberOfInstances() {
        return numberOfInstances;
    }
}