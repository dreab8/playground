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
	private final String name;

	private int navPosition;
	private int stateArrayPosition;

	public CustomAttribute(String name) {
		this.name = name;
	}

	@Override
	public void setStateArrayPosition(int stateArrayPosition) {
		this.stateArrayPosition = stateArrayPosition;
	}

	@Override
	public void setNavPosition(int navPosition) {
		this.navPosition = navPosition;
	}

	@Override
	public String getName() {
		return name;
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
	public String getAttributeInfo() {
		return "simple state field [" + navPosition + "] - " + name;
	}

	@Override
	public String toString() {
		return super.toString() + "[nav-pos=" + navPosition + "]";
	}
}
