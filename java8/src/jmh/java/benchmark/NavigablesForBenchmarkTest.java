/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import java.util.List;

import org.junit.Test;

import chp7.Navigable;
import chp7.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigablesForBenchmarkTest extends BenchmarkTestBaseSetUp {

	@Test
	public void testIt() {
		TestState state = new TestState();
		state.setUp();

		try {
			testIt( state );
		}
		finally {
			state.tearDown();
		}
	}

	@SuppressWarnings({"unchecked", "WeakerAccess"})
	@Benchmark
	public void testIt(TestState state) {
		final Object[] hydratedState = new Object[state.totalStateArrayContributorCount];

		final List<Navigable<?>> navigables = state.leafEntityDescriptor.getNavigables();

		for ( int i = 0, count = state.totalAttributeCount; i < count; i++ ) {
			final Navigable<?> navigable = navigables.get( i );

			if ( !StateArrayElementContributor.class.isInstance( navigable ) ) {
				continue;
			}

			final StateArrayElementContributor contributor = (StateArrayElementContributor) navigable;
			final int index = contributor.getStateArrayPosition();
			hydratedState[index] = contributor.deepCopy( hydratedState[index] );
		}

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}
}
