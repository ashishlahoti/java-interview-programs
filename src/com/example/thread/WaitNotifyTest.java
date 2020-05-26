package com.example.thread;

import java.util.LinkedList;
import java.util.Queue;

public class WaitNotifyTest {

	public static void main(String[] args) throws InterruptedException{
		Queue<Integer> queue = new LinkedList<Integer>();
		Thread producer = new Thread(new Producer(queue));
		producer.setName("Producer");
		producer.start();
		Thread consumer = new Thread(new Consumer(queue));
		consumer.setName("Consumer");
		consumer.start();
	}
	
}

class Producer implements Runnable{

	private Queue<Integer> queue = null;
	public Producer(Queue<Integer> queue){
		this.queue = queue;
	}
	@Override
	public void run() {
		for(int i=0; i<100; i++){
			synchronized (queue) {
				while(queue.size() > 0){
					System.out.println("Queue is full, producer is waiting.");
					try {
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("producing: "  + i);
				queue.add(i);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				queue.notify();
			}
		}
	}
}

class Consumer implements Runnable{

	private Queue<Integer> queue = null;
	public Consumer(Queue<Integer> queue){
		this.queue = queue;
	}
	@Override
	public void run() {
		while(true){
			synchronized (queue) {
				while(queue.size() == 0){
					try {
						System.out.println("Queue is empty, consumer is waiting.");
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("consuming: " + queue.poll());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				queue.notify();
			}
		}
	}
}
