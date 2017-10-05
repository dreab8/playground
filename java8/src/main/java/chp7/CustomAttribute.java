/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.Locale;

/**
 * @author Andrea Boriero
 */
public class CustomAttribute implements Navigable {

	private int id;

	public CustomAttribute(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return super.toString() + "[id=" + id + "]";
	}
}
