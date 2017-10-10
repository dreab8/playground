/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import chp7.Navigable;
import chp7.StateArrayElementContributor;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
@SuppressWarnings("unused")
public class NavigablesForBenchmarkTest extends BenchmarkTestBaseSetUp {
	@SuppressWarnings("unchecked")
	@Benchmark
	public void testIt(TestState state) {
		final Object[] hydratedState = new Object[state.totalAttributeCount];

		final int count = state.totalAttributeCount;

		for ( int position = 0; position < count; position++ ) {
			Navigable<?> navigable = state.leafEntityDescriptor
					.getNavigables()
					.get( position );
			if ( StateArrayElementContributor.class.isInstance( navigable ) ) {
				hydratedState[position] = ( (StateArrayElementContributor) navigable )
						.deepCopy( hydratedState[position] );
			}
		}
		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}
}
