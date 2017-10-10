/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

/**
 * @author Andrea Boriero
 */
public class EntityDescriptor<J> implements InheritanceCapable<J> {
	private final Hierarchy hierarchy;
	private final String name;
	private final InheritanceCapable<? super J> superclass;
	private final List<CustomAttribute<?>> declaredAttributes;

	private final List<Navigable<?>> declaredNavigables;
	private final List<Navigable<?>> navigables;

	public EntityDescriptor(
			String name,
			InheritanceCapable<? super J> superclass,
			List<CustomAttribute<?>> declaredAttributes) {
		this.name = name;
		this.superclass = superclass;
		this.declaredAttributes = declaredAttributes;

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

		declaredNavigables.addAll( declaredAttributes );
		collectHierarchicalNavigables( this, navigables );
	}

	private void collectHierarchicalNavigables(InheritanceCapable<?> container, List<Navigable<?>> list) {
		collectHierarchicalNavigables( container, list, null );
	}

	private void collectHierarchicalNavigables(
			InheritanceCapable<?> container,
			List<Navigable<?>> list,
			Class navTypeFilter) {
		if ( container.getSuperclassType() != null ) {
			// supers first
			collectHierarchicalNavigables( container.getSuperclassType(), list, navTypeFilter );
		}

		if ( indicatesNoFiltering( navTypeFilter ) ) {
			list.addAll( container.getDeclaredNavigables() );
		}
		else {
			for ( Navigable<?> navigable : container.getDeclaredNavigables() ) {
				if ( navTypeFilter.isInstance( navigable ) ) {
					list.add( navigable );
				}
			}
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
	public List<CustomAttribute<?>> getDeclaredAttributes() {
		return declaredAttributes;
	}

	@Override
	public List<Navigable<?>> getNavigables() {
		return navigables;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Navigable<?>> List<N> getNavigables(Class<N> filterType) {
		if ( indicatesNoFiltering( filterType ) ) {
			return (List<N>) navigables;
		}

		List<N> filtered = new ArrayList<>();
		collectHierarchicalNavigables( this, (List<Navigable<?>>) filtered, filterType );
		return filtered;
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
	public <N extends Navigable<?>> Spliterator<N> declaredNavigableSource(Class<N> filterType) {
		if ( filterType == null || Navigable.class.equals( filterType ) ) {
			return (Spliterator<N>) getDeclaredNavigables().spliterator();
		}

		return new ImprovedFilterableNavigableSpliterator( declaredNavigables, filterType );
	}

	@Override
	public String toString() {
		return super.toString() + "[" + getName()+ "]";
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Navigable<?>> Spliterator<N> navigableSource(Class<N> filterType) {
		if ( filterType == null || Navigable.class.equals( filterType ) ) {
			return (Spliterator<N>) navigables.spliterator();
		}

		return new ImprovedFilterableNavigableSpliterator( navigables, filterType );
	}
}
