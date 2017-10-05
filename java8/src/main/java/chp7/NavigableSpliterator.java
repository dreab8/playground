/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.Spliterator;

/**
 * @author Andrea Boriero
 */
public class NavigableSpliterator extends AbstractNavigableSpliterator<Navigable> {
	public <T> NavigableSpliterator(InheritanceCapable<T> container, boolean includeSuperNavigables) {
		super( container, includeSuperNavigables );
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Spliterator<Navigable> nextSpliterator(InheritanceCapable container) {
		return container.getDeclaredAttributes().spliterator();
	}
}
