package main.java.concurrency;

import java.util.ArrayList;
import java.util.List;

//Learn how to use wait and notify on shared objects.
public class ProducerConsumer {
    public static void main(String[] args) {
        List<Integer> tasks = new ArrayList<>();
        new Thread(new Producer(tasks, 10), "Producer").start();
        new Thread(new Consumer(tasks), "Consumer").start();
    }
}

class Producer implements Runnable{

    private final List<Integer> tasks;
    private final int capacity;

    public Producer(List<Integer> tasks, int capacity){
        this.tasks = tasks;
        this.capacity = capacity;
    }

    @Override
    public void run() {
        int counter = 1;
        while (true){
            try {
                produce(counter++);
            } catch (InterruptedException e){
                System.out.println("Producer thread got interrupted "+e.getMessage());
            }
        }
    }

    private void produce(int value) throws InterruptedException{
        synchronized (tasks){
            while (tasks.size() == capacity){
                System.out.println("Queue is full.");
                tasks.wait();
            }
            tasks.add(value);
            tasks.notify();
        }
    }
}

class Consumer implements Runnable{

    private final List<Integer> tasks;

    public Consumer(List<Integer> tasks){
        this.tasks = tasks;
    }

    @Override
    public void run() {
        while (true){
            try {
                consume();
            } catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void consume() throws InterruptedException{
        synchronized (tasks) {
            while (tasks.size() == 0) {
                System.out.println("Queue is empty");
                tasks.wait();
            }
            System.out.println(tasks.remove(tasks.size() - 1) + " is consumed.");
            tasks.notify();
        }
    }
}
