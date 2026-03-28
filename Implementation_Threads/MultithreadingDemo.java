
class MultithreadingDemo extends Thread {
    @Override
    public void run() {
        System.out.println("My thread is in running state.");
        System.out.println("Thread Name: " + getName());
        System.out.println("Thread Priority: " + getPriority());
    }

    public static void main(String args[]) {
        MultithreadingDemo obj = new MultithreadingDemo();
    }
}
