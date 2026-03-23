// /this class demonstrates creating a thread by implementing the Runnable interface.
//  Using Runnable is generally preferred as it allows the class to inherit from 
//  another class if needed.

class RunnableDemo implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable implementation: My thread is in running state.");
        System.out.println("Current Thread: " + Thread.currentThread().getName());
    }

    public static void main(String args[]) {
        RunnableDemo demo = new RunnableDemo();
        Thread thread = new Thread(demo); // Pass the Runnable object to the Thread constructor
        thread.start();
    }
}
