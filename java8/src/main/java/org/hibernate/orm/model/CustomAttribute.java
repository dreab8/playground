/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.model;

/**
 * @author Andrea Boriero
 */
public class CustomAttribute<J> implements NonIdAttribute<J> {
	private final int navPosition;
	private final int stateArrayPosition;

	public CustomAttribute(int navPosition) {
		this.navPosition = navPosition;
		this.stateArrayPosition = navPosition-1;
	}

	@Override
	public int getNavigablePosition() {
		return navPosition;
	}

	@Override
	public int getStateArrayPosition() {
		return stateArrayPosition;
	}

	@Override
	public String toString() {
		return super.toString() + "[id=" + navPosition + "]";
	}

	@Override
	public String getAttributeInfo() {
		return "simple state field : " + navPosition;
	}
}
