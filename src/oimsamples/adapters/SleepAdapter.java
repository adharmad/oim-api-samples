package oimsamples.adapters;

public class SleepAdapter {
	public void sleep(long sec) {
		try {
			System.out.println("Thread: " + Thread.currentThread().getName() + " sleeping");
			Thread.sleep(sec * 1000);
			System.out.println("Thread: " + Thread.currentThread().getName() + " awoke");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
