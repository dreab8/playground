package benchmark;

import chp7.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigableStreamBenchmarkTest extends BenchmarkTestBaseSetUp {

	@SuppressWarnings("unchecked")
	@Benchmark
	public void testStreamWithCustomSpliterator(TestState state) {
		final Object[] hydratedState = new Object[ state.totalAttributeCount ];

		state.leafEntityDescriptor.navigableStream( StateArrayElementContributor.class )
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
	public void testStreamWithoutCustomSpliterator(TestState state) {
		final Object[] hydratedState = new Object[state.totalAttributeCount];

		state.leafEntityDescriptor.getNavigables()
				.stream()
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
