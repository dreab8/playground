package benchmark;

import org.junit.Test;

import org.hibernate.orm.model.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigableStreamApproach extends BenchmarkTestBaseSetUp {

	@Benchmark
	@SuppressWarnings({"unchecked", "WeakerAccess"})
	public void benchmarkIt(TestState state) {
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

}
