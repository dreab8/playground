/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import java.util.stream.StreamSupport;

import org.junit.Test;

import org.hibernate.orm.model.ImprovedFilterableNavigableSpliteratorSecondVersion;
import org.hibernate.orm.model.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
public class StreamSpliteratorSecondVersionApproach {

	@Test
	public void testIt() {
		BenchmarkTestBaseSetUp.TestState state = new BenchmarkTestBaseSetUp.TestState();
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
	public void benchmarkIt(BenchmarkTestBaseSetUp.TestState state) {
		final Object[] hydratedState = new Object[ state.totalStateArrayContributorCount ];

		final ImprovedFilterableNavigableSpliteratorSecondVersion spliterator = new ImprovedFilterableNavigableSpliteratorSecondVersion(
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
