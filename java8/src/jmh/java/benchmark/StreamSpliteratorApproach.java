package benchmark;

import java.util.stream.StreamSupport;

import org.junit.Test;

import org.hibernate.orm.model.ImprovedFilterableNavigableSpliterator;
import org.hibernate.orm.model.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Steve Ebersole
 */
public class StreamSpliteratorApproach extends BenchmarkTestBaseSetUp {

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

	@SuppressWarnings({"unchecked", "WeakerAccess"})
	@Benchmark
	public void benchmarkIt(TestState state) {
		final Object[] hydratedState = new Object[ state.totalStateArrayContributorCount ];

		final ImprovedFilterableNavigableSpliterator spliterator = new ImprovedFilterableNavigableSpliterator(
				state.leafEntityDescriptor.getNavigables(),
				StateArrayElementContributor.class
		);

		StreamSupport.stream( spliterator, false ).forEach(
				attribute -> {
					final StateArrayElementContributor contributor = (StateArrayElementContributor) attribute;
					final int position = contributor.getStateArrayPosition();
					hydratedState[ position ] = contributor.deepCopy( hydratedState[ position ] );
				}
		);

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}

}
