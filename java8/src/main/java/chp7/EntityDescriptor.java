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
public class EntityDescriptor implements InheritanceCapable {

	private InheritanceCapable superclass;
	private List<CustomAttribute> declaredAttributes;

	public EntityDescriptor(InheritanceCapable superclass, List<CustomAttribute> declaredAttributes) {
		this.superclass = superclass;
		this.declaredAttributes = declaredAttributes;
	}

	@Override
	public InheritanceCapable getSuperclassType() {
		return superclass;
	}

	@Override
	public List<CustomAttribute> getDeclaredAttributes() {
		return declaredAttributes;
	}
}
