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

import org.hibernate.orm.model.CustomAttribute;
import org.hibernate.orm.model.EntityDescriptor;
import org.hibernate.orm.model.IdentifierDescriptor;
import org.hibernate.orm.model.Navigable;
import org.hibernate.orm.model.StateArrayElementContributor;

import org.junit.Test;

import org.hamcrest.core.Is;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Andrea Boriero
 */
public class BenchmarkTestBaseSetUp {
	@SuppressWarnings({"unchecked", "WeakerAccess"})
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
			this.rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

			List<CustomAttribute<?>> middleEntityAttributes = new ArrayList<>();
			middleEntityAttributes.add( new CustomAttribute( "middle - 1" ) );
			middleEntityAttributes.add( new CustomAttribute( "middle - 2" ) );
			this.middleEntityDescriptor = new EntityDescriptor(
					"Middle",
					rootEntityDescriptor,
					middleEntityAttributes
			);

			List<CustomAttribute> leafEntityAttributes = new ArrayList<>();
			leafEntityAttributes.add( new CustomAttribute( "leaf - 3" ) );
			leafEntityAttributes.add( new CustomAttribute( "leaf - 4" ) );
			leafEntityDescriptor = new EntityDescriptor(
					"Leaf",
					middleEntityDescriptor,
					leafEntityAttributes
			);

			rootEntityDescriptor.complete();
			middleEntityDescriptor.complete();
			leafEntityDescriptor.complete();
		}

		@TearDown
		public void tearDown(){
			leafEntityDescriptor = null;
		}
	}

	@Test
	public void testProperModel() {
		TestState state = new TestState();
		state.setUp();

		assertNavPosition( state.rootEntityDescriptor.getHierarchy().getIdentifierDescriptor(), 0 );

		assertNavPosition( state.middleEntityDescriptor.getDeclaredAttributes().get( 0 ), 1 );
		assertStateArrayPosition( state.middleEntityDescriptor.getDeclaredAttributes().get( 0 ), 0 );

		assertNavPosition( state.leafEntityDescriptor.getDeclaredAttributes().get( 0 ), 3 );
		assertStateArrayPosition( state.leafEntityDescriptor.getDeclaredAttributes().get( 0 ), 2 );
	}

	private void assertNavPosition(Navigable navigable, int expectedPosition) {
		assertThat( navigable.getNavigablePosition(), is( expectedPosition ) );
	}

	private void assertStateArrayPosition(StateArrayElementContributor co, int expectedPosition) {
		assertThat( co.getStateArrayPosition(), is( expectedPosition ) );
	}
}
