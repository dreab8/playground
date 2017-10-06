/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import java.util.ArrayList;
import java.util.List;

import chp7.Navigable;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Andrea Boriero
 */
public class NavigablesForEachBenchmarkTest extends BenchmarkTestBaseSetUp {

	@Benchmark
	public void testIt(TestState state) {
		final List<Integer> positions = new ArrayList<>(  );
		List<Navigable<?>> navigables = state.leafEntityDescriptor.getNavigables();
		navigables.forEach( navigable -> positions.add( navigable.getNavigablePosition() ) );
		assert positions.size() == 5;
	}
}
