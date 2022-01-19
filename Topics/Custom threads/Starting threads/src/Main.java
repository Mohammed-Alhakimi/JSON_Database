public class Main {

    public static void main(String[] args) {
        System.out.print("too hard calculations...\n".repeat(3));
        /*
          (°͜ʖ°)
         Thread t1 = new Thread(new RunnableWorker(),"worker-1");
         Thread t2 = new Thread(new RunnableWorker(),"worker-2");
         Thread t3 = new Thread(new RunnableWorker(),"worker-3");
         t1.start();
         t2.start();
         t3.start();
        */
    }
}

// Don't change the code below       
class RunnableWorker implements Runnable {

    @Override
    public void run() {
        final String name = Thread.currentThread().getName();

        if (name.startsWith("worker-")) {
            System.out.println("too hard calculations...");
        } else {
            return;
        }
    }
}