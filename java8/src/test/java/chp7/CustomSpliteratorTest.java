/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class CustomSpliteratorTest {

	@Test
	public void testIt(){
		List<CustomAttribute> rootEntityAttributes = new ArrayList<>(  );
		rootEntityAttributes.add( new CustomAttribute( 0 ) );
		rootEntityAttributes.add( new CustomAttribute( 1 ) );
		EntityDescriptor rootEntityDescriptor = new EntityDescriptor( "Root", null, rootEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 2 ) );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		EntityDescriptor leafEntityDescriptor = new EntityDescriptor( "Leaf", rootEntityDescriptor, leafEntityAttributes );

		testIds( leafEntityDescriptor );
	}

	@Test
	public void testMiddleMissingAttribute() {
		List<CustomAttribute> rootEntityAttributes = new ArrayList<>(  );
		rootEntityAttributes.add( new CustomAttribute( 0 ) );
		rootEntityAttributes.add( new CustomAttribute( 1 ) );
		EntityDescriptor<?> rootEntityDescriptor = new EntityDescriptor<>( "Root", null, rootEntityAttributes );

		List<CustomAttribute> middleEntityAttributes = Collections.emptyList();
		EntityDescriptor middleEntityDescriptor = new EntityDescriptor( "Middle", rootEntityDescriptor, middleEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 2 ) );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		EntityDescriptor<?> leafEntityDescriptor = new EntityDescriptor( "Leaf", middleEntityDescriptor, leafEntityAttributes );

		testIds( leafEntityDescriptor );
	}

	@Test
	public void testRootMissingAttributes() {
		List<CustomAttribute> rootEntityAttributes = Collections.emptyList();
		EntityDescriptor<?> rootEntityDescriptor = new EntityDescriptor<>( "Root", null, rootEntityAttributes );

		List<CustomAttribute> middleEntityAttributes = new ArrayList<>(  );
		middleEntityAttributes.add( new CustomAttribute( 0 ) );
		middleEntityAttributes.add( new CustomAttribute( 1 ) );
		EntityDescriptor middleEntityDescriptor = new EntityDescriptor( "Middle", rootEntityDescriptor, middleEntityAttributes );

		List<CustomAttribute> leafEntityAttributes = new ArrayList<>(  );
		leafEntityAttributes.add( new CustomAttribute( 2 ) );
		leafEntityAttributes.add( new CustomAttribute( 3 ) );
		EntityDescriptor<?> leafEntityDescriptor = new EntityDescriptor( "Leaf", middleEntityDescriptor, leafEntityAttributes );

		testIds( leafEntityDescriptor );
	}

	private void testIds(EntityDescriptor<?> entityDescriptorLeaf) {
		final List<Integer> ids = entityDescriptorLeaf.navigableStream()
				.map( Navigable::getId )
				.collect( Collectors.toList() );

		assertThat( ids.size(), is(4) );

		for (int i = 0; i < ids.size(); i++) {
			final Integer id = ids.get( i );
			System.out.printf( "Checking : i = %s, id = %s", i, id );
			assertThat( "expected " + i  + "is not equals to " + id, i , is( id ) );
		}
	}
}
