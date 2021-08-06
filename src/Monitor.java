import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor {
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	int numberOfChopsticks;
	enum State { THINKING, HUNGRY, EATING, TALKING }
	State[] states;
	ArrayList<Integer> philosopherEatingQueue;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers) {
		numberOfChopsticks = piNumberOfPhilosophers;
		states = new State[numberOfChopsticks];
		philosopherEatingQueue = new ArrayList<>(0);
		// Instantiate all philosophers in the thinking state
		for(int i = 0; i < numberOfChopsticks; i++) {
			states[i] = State.THINKING;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */


	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) {
		// piTID - 1 since the pTID starts at 1 and the states array is 0 indexed
		states[piTID - 1] = State.HUNGRY;
		philosopherEatingQueue.add(0, piTID); // Add this hungry philosopher to the end of the hungry queue
		System.out.println("Thread id: " + piTID);
		System.out.println(Arrays.toString(states));

		while(states[(piTID - 1 + numberOfChopsticks - 1) % numberOfChopsticks] == State.EATING || states[(piTID) % numberOfChopsticks] == State.EATING ||
				philosopherEatingQueue.get(philosopherEatingQueue.size() - 1) != piTID) {
			// If both neighbors are eating, then wait.
			try {
				System.out.println("Thread " + piTID + " is waiting to eat");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		states[piTID - 1] = State.EATING;
		System.out.println(Arrays.toString(states));
	}

	/**
	 * When a given philosopher's done eating, they put the chopsticks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID) {
		System.out.println("Thread id: " + piTID);
		System.out.println(Arrays.toString(states));
		states[piTID - 1] = State.THINKING;
		System.out.println(Arrays.toString(states));
		philosopherEatingQueue.remove(philosopherEatingQueue.size() - 1); // remove the philosopher from the start of the queue
		notifyAll(); // Signaling to other threads that the lock on the monitor is available
		System.out.println("PutDown() notified - Thread" + piTID);
	}

	/**
	 * Only one philosopher at a time is allowed to talk
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID) {
		System.out.println("Thread id: " + piTID);
		System.out.println(Arrays.toString(states));
		boolean someOneIsTalking = false;
		for(int i = 0; i < numberOfChopsticks; i++) {
			if (states[i] == State.TALKING) {
				someOneIsTalking = true;
				break;
			}
		}

		if(someOneIsTalking || states[piTID - 1] == State.EATING) {
			try {
				System.out.println("Thread " + piTID + " is waiting to talk");
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			states[piTID - 1] = State.TALKING;
		}
		System.out.println(Arrays.toString(states));
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID) {
		System.out.println("Thread id: " + piTID);
		System.out.println(Arrays.toString(states));
		states[piTID - 1] = State.THINKING;
		System.out.println(Arrays.toString(states));
		notifyAll();
	}
}

// EOF
