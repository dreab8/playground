package java8inaction;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author Andrea Boriero
 */
public class SynchronizationTest {
Logger logger = Logger.getLogger( SynchronizationTest.class.getName() );

	private int NUMBER_OF_THREADS = 100;

	@Test
	public void test() throws InterruptedException, ExecutionException {
		MyService service = new MyService();

		logger.log( Level.WARNING, "start " );
		Future<Integer>[] serviceIdentities = execute(service);

		int i = 0;
		for(Future<Integer> future: serviceIdentities){
			future.get();
			i++;
		}


	}

	private FutureTask<Integer>[] execute(MyService service)
			throws InterruptedException, ExecutionException {
		FutureTask<Integer>[] results = new FutureTask[NUMBER_OF_THREADS];
		ExecutorService executor = Executors.newFixedThreadPool( NUMBER_OF_THREADS );
		for ( int i = 0; i < NUMBER_OF_THREADS; i++ ) {
			results[i] = new FutureTask<Integer>( new ServiceCallable( service , i));
			executor.execute( results[i] );
		}
		return results;
	}

	public class ServiceCallable implements Callable<Integer> {
		private final MyService registry;
		private int i;

		public ServiceCallable(MyService registry, int i) {
			this.registry = registry;
			this.i = i;
		}

		@Override
		public Integer call() throws Exception {
			if(i == 10){
				registry.doWork();
			}else{
				return registry.getI();
			}

			return 10;
		}
	}
}
