/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chp7.CustomAttribute;
import chp7.EntityDescriptor;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 * @author Andrea Boriero
 */
public class BenchmarkTestBaseSetUp {
	@State(Scope.Thread)
	public static class TestState {

		public EntityDescriptor<?> rootEntityDescriptor;
		public EntityDescriptor<?> middleEntityDescriptor;
		public EntityDescriptor<?> leafEntityDescriptor;

		public int totalAttributeCount = 5;
		public int totalStateArrayContributorCount = 4;

		@Setup
		public void setUp() {
			List<CustomAttribute<?>> rootEntityAttributes = Collections.emptyList();
			this.rootEntityDescriptor = new EntityDescriptor<>( "Root", null, rootEntityAttributes );

			List<CustomAttribute<?>> middleEntityAttributes = new ArrayList<>();
			middleEntityAttributes.add( new CustomAttribute( 1 ) );
			middleEntityAttributes.add( new CustomAttribute( 2 ) );
			this.middleEntityDescriptor = new EntityDescriptor(
					"Middle",
					rootEntityDescriptor,
					middleEntityAttributes
			);

			List<CustomAttribute> leafEntityAttributes = new ArrayList<>();
			leafEntityAttributes.add( new CustomAttribute( 3 ) );
			leafEntityAttributes.add( new CustomAttribute( 4 ) );
			leafEntityDescriptor = new EntityDescriptor(
					"Leaf",
					middleEntityDescriptor,
					leafEntityAttributes
			);
		}

		@TearDown
		public void tearDown(){
			leafEntityDescriptor = null;
		}
	}
}
