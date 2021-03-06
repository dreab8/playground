package benchmark;

import org.junit.Test;

import org.hibernate.orm.model.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Steve Ebersole
 */
public class StreamOperationApproach extends BenchmarkTestBaseSetUp {

	@Test
	public void testIt() {
		TestState state = new TestState();
		state.setUp();

		try {
			benchmarkIt( state );
		}
		finally {
			state.tearDown();
		}
	}

	@Benchmark
	@SuppressWarnings({"unchecked", "WeakerAccess"})
	public void benchmarkIt(TestState state) {
		final Object[] hydratedState = new Object[state.totalStateArrayContributorCount];

		state.leafEntityDescriptor.navigableStream()
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
