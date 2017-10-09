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
	public void testIt(TestState state) {
		final Object[] hydratedState = new Object[ state.totalAttributeCount ];

		state.leafEntityDescriptor.navigableStream( StateArrayElementContributor.class )
				.parallel()
				.forEach(
						attribute -> {
							int position = attribute.getStateArrayPosition();
							hydratedState[ position ] = attribute.deepCopy( hydratedState[ position ] );
						}
				);

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}

}
