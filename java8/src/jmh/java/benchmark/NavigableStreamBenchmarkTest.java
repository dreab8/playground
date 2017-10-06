package benchmark;

import chp7.Navigable;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
public class NavigableStreamBenchmarkTest extends BenchmarkTestBaseSetUp {

	@Benchmark
	public void testIt(TestState state) {
		final CountKeeper countKeeper = new CountKeeper();

		state.leafEntityDescriptor.navigableStream()
				.map( Navigable::getNavigablePosition )
				.forEach( integer -> countKeeper.count++ );
		assert countKeeper.count == 5;
	}

	private static class CountKeeper {
		private int count;
	}
}
