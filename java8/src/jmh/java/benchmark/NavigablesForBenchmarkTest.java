/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import java.util.List;

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

		final List<Navigable<?>> navigables = state.leafEntityDescriptor
				.getNavigables();

		for ( int position = 0; position < navigables.size(); position++ ) {
			Navigable<?> navigable = navigables.get( position );
			if ( StateArrayElementContributor.class.isInstance( navigable ) ) {
				final StateArrayElementContributor contributor = (StateArrayElementContributor) navigable;
				hydratedState[contributor.getStateArrayPosition()] = (contributor)
						.deepCopy( hydratedState[contributor.getStateArrayPosition()] );
			}
		}
		assert hydratedState[0] == StateArrayElementContributor.NOT_NULL;
	}
}
