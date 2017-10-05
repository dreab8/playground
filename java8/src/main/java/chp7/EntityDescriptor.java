/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.List;
import java.util.Set;

/**
 * @author Andrea Boriero
 */
public class EntityDescriptor<J> implements InheritanceCapable<J> {

	private final String name;
	private final InheritanceCapable superclass;
	private final List<CustomAttribute> declaredAttributes;

	public EntityDescriptor(
			String name,
			InheritanceCapable superclass,
			List<CustomAttribute> declaredAttributes) {
		this.name = name;
		this.superclass = superclass;
		this.declaredAttributes = declaredAttributes;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public InheritanceCapable getSuperclassType() {
		return superclass;
	}

	@Override
	public List<CustomAttribute> getDeclaredAttributes() {
		return declaredAttributes;
	}

	@Override
	public String toString() {
		return super.toString() + "[" + getName()+ "]";
	}
}
