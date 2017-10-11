/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Andrea Boriero
 */
public class EntityDescriptor<J> implements InheritanceCapable<J> {
	private final Hierarchy hierarchy;
	private final String name;
	private final InheritanceCapable<? super J> superclass;

	private final List<PersistentAttribute<?>> declaredAttributes;
	private final List<PersistentAttribute<?>> attributes;

	private final List<Navigable<?>> declaredNavigables;
	private final List<Navigable<?>> navigables;

	public EntityDescriptor(
			String name,
			InheritanceCapable<? super J> superclass,
			List<PersistentAttribute<?>> declaredAttributes) {
		this.name = name;
		this.superclass = superclass;

		this.declaredAttributes = new ArrayList<>( declaredAttributes );
		this.attributes = new ArrayList<>();

		this.declaredNavigables = new ArrayList<>();
		this.navigables = new ArrayList<>();

		if ( superclass == null ) {
			this.hierarchy = new Hierarchy(
					this,
					new SimpleIdentifierDescriptor()
			);
		}
		else {
			this.hierarchy = null;
		}
	}

	private boolean indicatesNoFiltering(Class navTypeFilter) {
		return navTypeFilter == null || Navigable.class.equals( navTypeFilter );
	}

	@Override
	public Hierarchy getHierarchy() {
		if ( hierarchy != null ) {
			return hierarchy;
		}

		assert superclass != null;
		return superclass.getHierarchy();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public InheritanceCapable<? super J> getSuperclassType() {
		return superclass;
	}

	@Override
	public List<PersistentAttribute<?>> getAttributes() {
		return attributes;
	}

	@Override
	public List<PersistentAttribute<?>> getDeclaredAttributes() {
		return declaredAttributes;
	}

	@Override
	public List<Navigable<?>> getNavigables() {
		return navigables;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Navigable<?>> Stream<N> navigableStream(Class<N> filterType) {
		if ( indicatesNoFiltering( filterType ) ) {
			return (Stream<N>) navigables.stream();
		}

		Spliterator<N> spliterator = new FilteredNavigableSpliterator(
				navigables,
				filterType
		);

		return StreamSupport.stream( spliterator, false );
	}

	@Override
	@SuppressWarnings("unchecked")
	public List getDeclaredNavigables() {
		if ( hierarchy == null ) {
			// not the root - just return the "normal" attribute list
			return declaredAttributes;
		}

		// otherwise, combine the id and other attributes
		final List completeList = new ArrayList();
		completeList.add( hierarchy.getIdentifierDescriptor() );
		completeList.addAll( declaredAttributes );

		return completeList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Navigable<?>> Stream<N> declaredNavigableStream(Class<N> filterType) {
		if ( indicatesNoFiltering( filterType ) ) {
			return (Stream<N>) declaredNavigables.stream();
		}

		return StreamSupport.stream(
				new FilteredNavigableSpliterator( declaredNavigables, filterType ),
				false
		);
	}

	@Override
	public String toString() {
		return super.toString() + "[" + getName()+ "]";
	}

	public void complete() {
		if ( hierarchy != null ) {
			// we are the root of the hierarchy
			declaredNavigables.add( hierarchy.getIdentifierDescriptor() );
		}

		navigables.add( getHierarchy().getIdentifierDescriptor() );

		declaredNavigables.addAll( declaredAttributes );

		addNavigables( navigables, attributes );
	}

	@Override
	public void addNavigables(
			List<Navigable<?>> navigables,
			List<PersistentAttribute<?>> attributes) {
		if ( getSuperclassType() != null ) {
			getSuperclassType().addNavigables( navigables, attributes );
		}

		for ( PersistentAttribute<?> declaredAttribute : declaredAttributes ) {
			navigables.add( declaredAttribute );
			attributes.add( declaredAttribute );
		}
	}
}
