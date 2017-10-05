package java8inaction.chp1;

import java.util.ArrayList;
import java.util.List;

import chp1.Apple;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class AppleTest {

	@Test
	public void testAppleWightFiltering(){
		Apple a = new Apple();
		a.setWeight( 100);
		Apple a1 = new Apple();
		a1.setWeight( 151);
		List<Apple> apples = new ArrayList<>();
		apples.add( a );
		apples.add( a1 );

		final List<Apple> heavyApples = Apple.filterApples( apples, Apple::isHeavyApple );
		assertThat(heavyApples.size(), is(1));
		assertThat(heavyApples.get( 0 ), is(a1));
	}
}
