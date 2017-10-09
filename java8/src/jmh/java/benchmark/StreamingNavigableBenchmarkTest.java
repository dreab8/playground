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
public class StreamingNavigableBenchmarkTest extends BenchmarkTestBaseSetUp {
	@Benchmark
	public void testIt(BenchmarkTestBaseSetUp.TestState state) {
		final CountKeeper countKeeper = new CountKeeper();

		final List<Integer> positions = new ArrayList<>();
		state.leafEntityDescriptor.getNavigables().stream().map( Navigable::getNavigablePosition )
				.forEach( integer -> countKeeper.count++ );
		assert countKeeper.count == 5;

	}

	private static class CountKeeper {
		private int count;
	}
}

