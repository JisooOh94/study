import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class Test2 {
	private class Foo implements Runnable{
		@Override
		public void run() {
			System.out.println("Thread run");
		}
	}
	@Test
	public void modernMultiThreading() {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new Foo());

		ExecutorService exec2 = Executors.newFixedThreadPool(100);
		exec2.execute(new Foo());

		ExecutorService exec3 = Executors.newScheduledThreadPool(100);
		((ScheduledExecutorService) exec3).schedule(new Foo(), 1000L, TimeUnit.MILLISECONDS);
	}

	@Test
	public void modernSynchronizing() {
		ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
		ConcurrentLinkedDeque<String> queue = new ConcurrentLinkedDeque<>();
	}


	@Test
	public void lock() throws InterruptedException {
		synchronized(this) {
			int num = 0;
			while(num < 100) {
				num++;
				this.wait();
			}

			this.notify();
		}
	}
}
