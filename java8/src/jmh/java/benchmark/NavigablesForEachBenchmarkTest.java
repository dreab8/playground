/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import chp7.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigablesForEachBenchmarkTest extends BenchmarkTestBaseSetUp {

	@SuppressWarnings("unchecked")
	@Benchmark
	public void testIt(TestState state) {
		final Object[] hydratedState = new Object[state.totalAttributeCount];

		final int count = state.totalAttributeCount;


		state.leafEntityDescriptor.getNavigables()
				.forEach(
						navigable -> {
							if ( StateArrayElementContributor.class.isInstance( navigable ) ) {
								final StateArrayElementContributor contributor = (StateArrayElementContributor) navigable;
								final int index = contributor.getStateArrayPosition();
								hydratedState[index] = contributor.deepCopy( hydratedState[index] );
							}
						}
				);

		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}
}
