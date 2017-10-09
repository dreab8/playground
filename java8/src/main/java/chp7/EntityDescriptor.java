/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrea Boriero
 */
public class EntityDescriptor<J> implements InheritanceCapable<J> {
	private final Hierarchy hierarchy;
	private final String name;
	private final InheritanceCapable<? super J> superclass;
	private final List<CustomAttribute> declaredAttributes;
	private final List declaredNavigables;
	private final List navigables;


	public EntityDescriptor(
			String name,
			InheritanceCapable superclass,
			List<CustomAttribute> declaredAttributes) {
		this.name = name;
		this.superclass = superclass;
		this.declaredAttributes = declaredAttributes;


		if ( superclass == null ) {
			this.hierarchy = new Hierarchy(
					this,
					() -> 0
			);
		}
		else {
			this.hierarchy = null;
		}
		if ( hierarchy == null ) {
			// not the root - just return the "normal" attribute list
			declaredNavigables =  declaredAttributes;
		}else {

			// otherwise, combine the id and other attributes
			declaredNavigables = new ArrayList();
			declaredNavigables.add( hierarchy.getIdentifierDescriptor() );
			declaredNavigables.addAll( declaredAttributes );
		}

		this.navigables = new ArrayList<>();
		collectHierarchicalNavigables( navigables, this );
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
	public List<CustomAttribute> getDeclaredAttributes() {
		return declaredAttributes;
	}

	@Override
	public List<Navigable<?>> getNavigables() {
//		final List<Navigable<?>> list = new ArrayList<>();
//
//		collectHierarchicalNavigables( list, this );
//
//		return list;
		return navigables;
	}

	private void collectHierarchicalNavigables(List<Navigable<?>> list, InheritanceCapable<?> container) {
		if ( container.getSuperclassType() != null ) {
			collectHierarchicalNavigables( list, container.getSuperclassType() );
		}

		list.addAll( container.getDeclaredNavigables() );
	}

	@Override
	@SuppressWarnings("unchecked")
	public List getDeclaredNavigables() {
		return declaredNavigables;
	}

	private <N extends Navigable<?>> List<N> filter(List<N> completeNavigableList, Class<N> filterType) {
		return completeNavigableList.stream()
				.filter( filterType::isInstance )
				.map( filterType::cast )
				.collect( Collectors.toList() );
	}

	@Override
	public String toString() {
		return super.toString() + "[" + getName()+ "]";
	}

}
