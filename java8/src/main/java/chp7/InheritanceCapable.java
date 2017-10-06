/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Andrea Boriero
 */
public interface InheritanceCapable<J> extends NavigableContainer<J> {
	Hierarchy getHierarchy();

	String getName();

	InheritanceCapable<? super J> getSuperclassType();

	List<CustomAttribute> getDeclaredAttributes();
	List<Navigable<?>> getDeclaredNavigables();

	@SuppressWarnings("unchecked")
	default <N extends Navigable<?>> Spliterator<N> navigableSource(Class<N> filterType) {
		return new FilterableNavigableSpliterator( this, filterType, true );
	}

	@SuppressWarnings("unchecked")
	default <N extends Navigable<?>> Spliterator<N> declaredNavigableSource(Class<N> filterType) {
		return new FilterableNavigableSpliterator( this, filterType, false );
	}

	default  <N extends Navigable<?>> Stream<N> declaredNavigableStream(Class<N> filterType) {
		return StreamSupport.stream( declaredNavigableSource( filterType ), false );
	}

}

