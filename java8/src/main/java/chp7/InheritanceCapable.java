/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Andrea Boriero
 */
public interface InheritanceCapable<T> {
	InheritanceCapable<? super T> getSuperclassType();

	List<CustomAttribute> getDeclaredAttributes();

	default Stream<Navigable> navigableStream() {
		return StreamSupport.stream( declaredNavigableSource(), false );
	}

	@SuppressWarnings("unchecked")
	default Spliterator declaredNavigableSource() {
		return new NavigableSpliterator( this, true );
	}

}

