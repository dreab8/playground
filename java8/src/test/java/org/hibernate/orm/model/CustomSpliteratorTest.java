/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.Test;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class CustomSpliteratorTest {

	@Test
	public void testIt(){
		List<CustomAttribute> rootEntityAttributes = new ArrayList<>(  );
		rootEntityAttributes.add( new CustomAttribute( 1 ) );
		rootEntityAttributes.add( new CustomAttribute( 2 ) );
		EntityDescriptor rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		leafEntityAttributes.add( new CustomAttribute( 4 ) );
		EntityDescriptor leafEntityDescriptor = new EntityDescriptor( "Leaf", rootEntityDescriptor, leafEntityAttributes );

		rootEntityDescriptor.complete();
		leafEntityDescriptor.complete();

		testIds( leafEntityDescriptor );
	}

	@Test
	public void testMiddleMissingAttribute() {
		List<CustomAttribute> rootEntityAttributes = new ArrayList<>(  );
		rootEntityAttributes.add( new CustomAttribute( 1 ) );
		rootEntityAttributes.add( new CustomAttribute( 2 ) );
		EntityDescriptor<?> rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

		List<CustomAttribute> middleEntityAttributes = Collections.emptyList();
		EntityDescriptor middleEntityDescriptor = new EntityDescriptor( "Middle", rootEntityDescriptor, middleEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		leafEntityAttributes.add( new CustomAttribute( 4 ) );
		EntityDescriptor<?> leafEntityDescriptor = new EntityDescriptor( "Leaf", middleEntityDescriptor, leafEntityAttributes );

		rootEntityDescriptor.complete();
		middleEntityDescriptor.complete();
		leafEntityDescriptor.complete();

		testIds( leafEntityDescriptor );
	}

	@Test
	public void testRootMissingAttributes() {
		List<CustomAttribute> rootEntityAttributes = Collections.emptyList();
		EntityDescriptor<?> rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

		List<CustomAttribute> middleEntityAttributes = new ArrayList<>(  );
		middleEntityAttributes.add( new CustomAttribute( 1 ) );
		middleEntityAttributes.add( new CustomAttribute( 2 ) );
		EntityDescriptor middleEntityDescriptor = new EntityDescriptor( "Middle", rootEntityDescriptor, middleEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		leafEntityAttributes.add( new CustomAttribute( 4 ) );
		EntityDescriptor<?> leafEntityDescriptor = new EntityDescriptor( "Leaf", middleEntityDescriptor, leafEntityAttributes );

		rootEntityDescriptor.complete();
		middleEntityDescriptor.complete();
		leafEntityDescriptor.complete();

		testIds( leafEntityDescriptor );
	}

	private void testIds(EntityDescriptor<?> entityDescriptor) {
		testStreams(
				entityDescriptor.navigableStream().map( Navigable::getNavigablePosition ).collect( toList() ),
				5
		);

		testStreams(
				entityDescriptor.navigableStream( StateArrayElementContributor.class )
						.map( StateArrayElementContributor::getStateArrayPosition )
						.collect( toList() ),
				4
		);

		testStreams(
				entityDescriptor.navigableStream( NonIdAttribute.class )
						.map( StateArrayElementContributor::getStateArrayPosition )
						.collect( toList() ),
				4
		);

		testStreams(
				entityDescriptor.navigableStream( PersistentAttribute.class )
						.map( StateArrayElementContributor::getStateArrayPosition )
						.collect( toList() ),
				4
		);

		final ImprovedFilterableNavigableSpliteratorSecondVersion<StateArrayElementContributor<?>> spliterator = new ImprovedFilterableNavigableSpliteratorSecondVersion(
				entityDescriptor.getNavigables(),
				StateArrayElementContributor.class
		);

		testStreams( StreamSupport.stream( spliterator, false )
							 .map( StateArrayElementContributor::getStateArrayPosition )
							 .collect( toList() ),
					 4 );
	}

	private void testStreams(List<Integer> values, int expectedCount) {
		assertThat( values.size(), is(expectedCount) );

		for (int i = 0; i < values.size(); i++) {
			final Integer id = values.get( i );
			System.out.printf( "Checking : i = %s, id = %s", i, id );
			assertThat( "expected " + i  + "is not equals to " + id, i , is( id ) );
		}
	}

	@Test
	public void testParallel() {
		List<CustomAttribute> rootEntityAttributes = new ArrayList<>(  );
		rootEntityAttributes.add( new CustomAttribute( 1 ) );
		rootEntityAttributes.add( new CustomAttribute( 2 ) );
		EntityDescriptor<?> rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		leafEntityAttributes.add( new CustomAttribute( 4 ) );
		EntityDescriptor<?> leafEntityDescriptor = new EntityDescriptor( "Leaf", rootEntityDescriptor, leafEntityAttributes );

		rootEntityDescriptor.complete();
		leafEntityDescriptor.complete();

		leafEntityDescriptor.navigableStream().parallel().forEach(
				navigable -> System.out.println( "Streaming : " + navigable.getNavigablePosition() )
		);
	}

	@Test
	public void testFiltered() {
		List<CustomAttribute> rootEntityAttributes = new ArrayList<>(  );
		rootEntityAttributes.add( new CustomAttribute( 1 ) );
		rootEntityAttributes.add( new CustomAttribute( 2 ) );
		EntityDescriptor<?> rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		leafEntityAttributes.add( new CustomAttribute( 4 ) );
		EntityDescriptor<?> leafEntityDescriptor = new EntityDescriptor( "Leaf", rootEntityDescriptor, leafEntityAttributes );

		rootEntityDescriptor.complete();
		leafEntityDescriptor.complete();

		leafEntityDescriptor.navigableStream( ).forEach(
				navigable -> System.out.println( "Streaming : " + navigable.getNavigablePosition() )
		);
	}
}
