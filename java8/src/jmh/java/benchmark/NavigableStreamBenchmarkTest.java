package benchmark;

import org.junit.Test;

import chp7.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigableStreamBenchmarkTest extends BenchmarkTestBaseSetUp {

	@SuppressWarnings({"unchecked", "WeakerAccess"})
	@Benchmark
	public void testStreamWithCustomSpliterator(TestState state) {
		final Object[] hydratedState = new Object[ state.totalStateArrayContributorCount ];

		state.leafEntityDescriptor.navigableStream( StateArrayElementContributor.class )
				.forEach(
						attribute -> {
							final int position = attribute.getStateArrayPosition();
							hydratedState[ position ] = attribute.deepCopy( hydratedState[ position ] );
						}
				);

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}

	@Test
	public void testStreamWithCustomSpliterator() {
		TestState state = new TestState();
		state.setUp();

		try {
			testStreamWithCustomSpliterator( state );
		}
		finally {
			state.tearDown();
		}
	}

}
