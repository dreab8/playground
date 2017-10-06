package benchmark;

import java.util.List;

import chp7.Navigable;
import org.openjdk.jmh.annotations.Benchmark;

import static java.util.stream.Collectors.toList;

/**
 * @author Andrea Boriero
 */
public class NavigableStreamBenchmarkTest extends BenchmarkTestBaseSetUp {

	@Benchmark
	public void testIt(TestState state) {
		List<Integer> positions = state.leafEntityDescriptor.navigableStream()
				.map( Navigable::getNavigablePosition )
				.collect( toList() );
		assert positions.size() == 5;
	}

}
