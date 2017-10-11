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
		return declaredNavigables;
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
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// handle the identifier
		//		- ultimately would need handling for version, discriminator, etc

		if ( hierarchy != null ) {
			// we are the root of the hierarchy
			declaredNavigables.add( hierarchy.getIdentifierDescriptor() );
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// order the declared attributes by name
		//		- and add to declaredNavigables

		declaredAttributes.sort( (o1, o2) ->  o1.getAttributeInfo().compareTo( o2.getName() ) );
		declaredNavigables.addAll( declaredAttributes );


		int navCount = 0;
		int stateContributorCount = 0;

		if ( getSuperclassType() != null ) {
			final List<Navigable<?>> superNavigables = getSuperclassType().getNavigables();
			navigables.addAll( superNavigables );
			navCount = superNavigables.size();

			final List<PersistentAttribute<?>> superAttributes = getSuperclassType().getAttributes();
			this.attributes.addAll( superAttributes );
			stateContributorCount = superAttributes.size();
		}

		for ( Navigable<?> declaredNavigable : declaredNavigables ) {
			declaredNavigable.setNavPosition( navCount++ );

			if ( StateArrayElementContributor.class.isInstance( declaredNavigable ) ) {
				StateArrayElementContributor.class.cast( declaredNavigable ).setStateArrayPosition( stateContributorCount++ );
			}
		}

		attributes.addAll( declaredAttributes );
		navigables.addAll( declaredNavigables );
	}
}
