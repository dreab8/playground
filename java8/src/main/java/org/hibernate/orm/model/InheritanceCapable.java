/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.model;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Andrea Boriero
 */
public interface InheritanceCapable<J> extends NavigableContainer<J> {
	Hierarchy getHierarchy();

	String getName();

	InheritanceCapable<? super J> getSuperclassType();

	/**
	 * @todo (6.0) : this (or similar) will likely be needed for JPA
	 * 		would be awesome to remove this and use a filtered Navigable Stream, but ^^
	 *
	 * 		although an alternative solution is to use the filtered Stream for those
	 * 		JPA metamodel method impls and still be able to delete this one
	 */
	List<PersistentAttribute<?>> getAttributes();
	List<PersistentAttribute<?>> getDeclaredAttributes();

	/**
	 * Obtain a List of all the Navigables declared by this container.
	 *
	 * @apiNote When the NavigableContainer is also a {@link InheritanceCapable}
	 * the Navigables returned here are all Navigables for this type
	 * as well as its super types.
	 *
	 * @see #getNavigables()
	 */
	List<Navigable<?>> getDeclaredNavigables();

	/**
	 * Obtain a Stream over all of the Navigables declared by this container.
	 *
	 * @apiNote The returned Stream contains just the Navigables declared on
	 * this type, exclusive of the the supertype's Navigables.
	 *
	 * @see InheritanceCapable#navigableStream()
	 */
	default Stream<Navigable> declaredNavigableStream() {
		return declaredNavigableStream( null );
	}

	/**
	 * Obtain a filtered Stream over all of the Navigables declared by this
	 * container.
	 *
	 * @param filterType The specific sub-type of Navigable to filter the
	 * Streamed Navigables by.  `null` is a special filter indicating to
	 * not filter.
	 *
	 * @apiNote The returned Stream contains just the Navigables (of the
	 * matching `filterType`) Navigables declared on this type, exclusive
	 * of the the supertype's Navigables.
	 *
	 * @see InheritanceCapable#navigableStream(Class)
	 */
	<N extends Navigable<?>> Stream<N> declaredNavigableStream(Class<N> filterType);
}

