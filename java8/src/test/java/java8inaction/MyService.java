package java8inaction;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andrea Boriero
 */
public class MyService {
	Logger logger = Logger.getLogger( MyService.class.getName() );


	private int i;
	private int j;
	private Object lock = new Object();
	private Object lock1 = new Object();

	public void doWork() throws InterruptedException {
		synchronized (this) {
			logger.log( Level.SEVERE, "------------------------------------------------------------------------" );
			i = 100;
			while ( true ) {
//				j--;
			}
		}
	}

	public Integer getI() {
		logger.log( Level.WARNING, "getId" );
		synchronized (this) {
			logger.log( Level.WARNING, "Enter synchro" );
			int i = 1000;
			while ( i > 0 ) {
				i--;
			}
//			try {
//				Thread.sleep( 1000 );
//			}
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			j++;

			logger.log( Level.WARNING, "FINISHED getId " + j );
			return j;
		}
	}
}
