package com.example.core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

public class MyLift {

	public static void main(String[] args) {
		System.out.println("Welcome to MyLift");
		// RequestListenerThread to read requested floor and add to Set
		Thread requestListenerThread = new Thread(new RequestListener(),
				"RequestListenerThread");
		// RequestProcessorThread to read Set and process requested floor
		Thread requestProcessorThread = new Thread(new RequestProcessor(),
				"RequestProcessorThread");
		
		Elevator.getInstance().setRequestProcessorThread(requestProcessorThread);
		
		requestListenerThread.start();
		requestProcessorThread.start();
		
		
	}
}

class Elevator {
	
	private static Elevator elevator = null;
	
	private TreeSet<Integer> upRequestSet = new TreeSet<Integer>();
	
	private TreeSet<Integer> downRequestSet = new TreeSet<Integer>();
	
	private int currentFloor = 0;

	private Direction direction = Direction.UP;

	private Elevator() {};
	
	private Thread requestProcessorThread;

	/**
	 * @return singleton instance
	 */
	static Elevator getInstance() {
		if (elevator == null) {
			elevator = new Elevator();
		}
		return elevator;
	}

	/**
	 * Add request to Set
	 * 
	 * @param floor
	 */
	public synchronized void addFloor(int f) {
		if(f > currentFloor){
			upRequestSet.add(f);
		}else if(f < currentFloor){
			downRequestSet.add(f);
		}else{
			System.out.println("Lift already at :" + f);
		}
		System.out.println("UPRequest : " + upRequestSet);
		System.out.println("DownRequest : " + downRequestSet);
		if(f != currentFloor){
			if(requestProcessorThread.getState() == Thread.State.WAITING){
				// Notify processor thread that a new request has come if it is waiting
				notify();
			}else{
				// Interrupt Processor thread to check if new request should be processed before current request or not. 
				requestProcessorThread.interrupt();
			}
		}
	}

	/**
	 * @return next request to process based on elevator current floor and direction
	 */
	public synchronized int nextFloor() {

		Integer floor = null;

		if (direction == Direction.UP) {
			floor = upRequestSet.pollFirst();
			if(floor == null){
				floor = downRequestSet.pollLast();
			}
		}else if(direction == Direction.DOWN){
			floor = downRequestSet.pollLast();
			if(floor == null){
				floor = upRequestSet.pollFirst();
			}
		}
		if (floor == null) {
			try {
				System.out.println("Waiting at Floor :" + getCurrentFloor());
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
		return (floor == null) ? -1 : floor;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
	
	/**
	 * Set current floor and direction based on requested floor
	 * 
	 * @param currentFloor
	 * @throws InterruptedException 
	 */
	public void setCurrentFloor(int currentFloor) throws InterruptedException {
		if (this.currentFloor > currentFloor) {
			setDirection(Direction.DOWN);
		} else {
			setDirection(Direction.UP);
		}
		this.currentFloor = currentFloor;
		
		System.out.println("Floor : " + currentFloor);
		
		Thread.sleep(3000);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Thread getRequestProcessorThread() {
		return requestProcessorThread;
	}

	public void setRequestProcessorThread(Thread requestProcessorThread) {
		this.requestProcessorThread = requestProcessorThread;
	}

	public TreeSet<Integer> getUpRequestSet() {
		return upRequestSet;
	}

	public void setUpRequestSet(TreeSet<Integer> upRequestSet) {
		this.upRequestSet = upRequestSet;
	}

	public TreeSet<Integer> getDownRequestSet() {
		return downRequestSet;
	}

	public void setDownRequestSet(TreeSet<Integer> downRequestSet) {
		this.downRequestSet = downRequestSet;
	}
	
}

class RequestProcessor implements Runnable {

	@Override
	public void run() {
		while (true) {
			Elevator elevator = Elevator.getInstance();
			int floor = elevator.nextFloor();
			int currentFloor = elevator.getCurrentFloor();
			try{
				if (floor >= 0) {
					if (currentFloor > floor) {
						while (currentFloor > floor) {
							elevator.setCurrentFloor(--currentFloor);
						}
					} else {
						while (currentFloor < floor) {
							elevator.setCurrentFloor(++currentFloor);
						}
					}
					System.out.println("Welcome to Floor : " + elevator.getCurrentFloor());
				}
				
			}catch(InterruptedException e){
				// If a new request has interrupted a current request processing then check -
				// -if the current request is already processed
				// -otherwise add it back in request Set
				if (floor > currentFloor) {
					elevator.getUpRequestSet().add(floor);
				} else if(floor < currentFloor){
					elevator.getDownRequestSet().add(floor);
				}
			}
		}
	}
}

class RequestListener implements Runnable {

	@Override
	public void run() {

		while (true) {
			String floorNumberStr = null;
			try {
				// Read input from console
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				floorNumberStr = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (isValidFloorNumber(floorNumberStr)) {
				System.out.println("User Pressed : " + floorNumberStr);
				Elevator elevator = Elevator.getInstance();
				elevator.addFloor(Integer.parseInt(floorNumberStr));
			} else {
				System.out.println("Floor Request Invalid : " + floorNumberStr);
			}
		}
	}

	/**
	 * This method is used to define maximum floors this elevator can process.
	 * @param s - requested floor
	 * @return true if requested floor is integer and upto two digits. (max floor = 99)
	 */
	private boolean isValidFloorNumber(String s) {
		return (s != null) && s.matches("\\d{1,2}");
	}

}

enum Direction {
	UP, DOWN
}
