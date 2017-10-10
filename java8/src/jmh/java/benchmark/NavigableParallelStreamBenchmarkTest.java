package benchmark;

import org.junit.Test;

import chp7.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigableParallelStreamBenchmarkTest extends BenchmarkTestBaseSetUp {

	@Test
	public void testStreamParallelWithCustomSpliterator() {
		TestState state = new TestState();
		state.setUp();

		try {
			testStreamParallelWithCustomSpliterator( state );
		}
		finally {
			state.tearDown();
		}
	}

	@Test
	public void testStreamParallelWithoutCuastomSpliterator() {
		TestState state = new TestState();
		state.setUp();

		try {
			testStreamParallelWithoutCuastomSpliterator( state );
		}
		finally {
			state.tearDown();
		}
	}

	@SuppressWarnings({"unchecked", "WeakerAccess"})
	@Benchmark
	public void testStreamParallelWithCustomSpliterator(TestState state) {
		final Object[] hydratedState = new Object[ state.totalStateArrayContributorCount ];

		state.leafEntityDescriptor.navigableStream( StateArrayElementContributor.class )
				.parallel()
				.forEach(
						attribute -> {
							final int position = attribute.getStateArrayPosition();
							hydratedState[ position ] = attribute.deepCopy( hydratedState[ position ] );
						}
				);

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}

	@SuppressWarnings("unchecked")
	@Benchmark
	public void testStreamParallelWithoutCuastomSpliterator(TestState state) {
		final Object[] hydratedState = new Object[state.totalAttributeCount];

		state.leafEntityDescriptor.getNavigables()
				.stream().parallel()
				.filter( StateArrayElementContributor.class::isInstance )
				.map( StateArrayElementContributor.class::cast )
				.forEach(
						attribute -> {
							final int position = attribute.getStateArrayPosition();
							hydratedState[position] = attribute.deepCopy( hydratedState[position] );
						}
				);

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}
}
