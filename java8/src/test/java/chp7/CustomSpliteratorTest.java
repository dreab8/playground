/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.ArrayList;
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
		List<CustomAttribute> entityDescriptorAttributes = new ArrayList<>(  );
		entityDescriptorAttributes.add( new CustomAttribute( 0 ) );
		entityDescriptorAttributes.add( new CustomAttribute( 1 ) );
		EntityDescriptor entityDescriptor1 = new EntityDescriptor( null, entityDescriptorAttributes );

		List<CustomAttribute> entityDescriptorAttributesSecond = new ArrayList<>(  );
		entityDescriptorAttributesSecond.add( new CustomAttribute( 2 ) );
		entityDescriptorAttributesSecond.add( new CustomAttribute( 3 ) );
		EntityDescriptor entityDescriptor2 = new EntityDescriptor( entityDescriptor1, entityDescriptorAttributesSecond );

		Stream<Navigable> stream = entityDescriptor2.navigableStream();
		final List<Integer> ids = new ArrayList(  );
		stream.forEach( navigable -> ids.add(navigable.getId()) );

		for(int i = 0; i < ids.size(); i++){
			assertThat( "expected " + i  + "is not equals to " + ids.get( i ), i , is(ids.get( i )) );
		}


	}
}
