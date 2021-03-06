import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subroutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread {
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat() {
		System.out.println("Philosopher " + getTID() + " has started eating!");
		try {
			this.randomYield();
			Thread.sleep((long)(Math.random() * TIME_TO_WASTE));
			this.randomYield();
		}
		catch(InterruptedException e) {
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		System.out.println("Philosopher " + getTID() + " has finished eating!");
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think() {
		System.out.println("Philosopher " + getTID() + " has started thinking!");
		try {
			this.randomYield();
			Thread.sleep((long)(Math.random() * TIME_TO_WASTE));
			this.randomYield();
		}
		catch(InterruptedException e) {
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		System.out.println("Philosopher " + getTID() + " has finished thinking!");
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk() {
		System.out.println("Philosopher " + getTID() + " has started talking!");
		this.randomYield();
		saySomething();
		this.randomYield();
		System.out.println("Philosopher " + getTID() + " has finished talking!");
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run() {

		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++) {
			DiningPhilosophers.soMonitor.pickUp(getTID());
			eat();
			DiningPhilosophers.soMonitor.putDown(getTID());
			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */

			// The philosopher will talk if the random double is greater than 0.5
			double j = Math.random();
			if(j > 0.5) {
				// Some monitor ops down here...
				DiningPhilosophers.soMonitor.requestTalk(getTID());
				talk();
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}

			Thread.yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething() {
		String[] astrPhrases = {
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println("Philosopher " + getTID() + " says: " + astrPhrases[(int)(Math.random() * astrPhrases.length)]);
	}
}

// EOF
