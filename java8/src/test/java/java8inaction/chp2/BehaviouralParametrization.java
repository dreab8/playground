package java8inaction.chp2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 * <p>
 * This is what behavior parameterization means: the ability to tell a method to take multiple behaviors (or strategies)
 * as parameters and use them 	internally to accomplish different behaviors.
 * <p>
 * With Lambda expressions it is possible to achive this with less verbosity.
 */
public class BehaviouralParametrization {

	@Test
	public void testFilteringIntegers() {
//		List<Integer> integers = new ArrayList<>();
//		for ( int i = 0; i < 10; i++ ) {
//			integers.add( i );
//		}

		final List<Integer> evenNumbers = filter(
				Arrays.asList( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ),
				(Integer i) -> i % 2 == 0
		);
		assertThat( evenNumbers.size(), is( 5 ) );

		evenNumbers.forEach( number -> assertThat( number % 2, is( 0 ) ) );
	}

	@Test
	public void testFilteringApples() {
		List<Apple> apples = new ArrayList<>();
		for ( int i = 0; i < 10; i++ ) {
			if ( i % 2 == 0 ) {
				apples.add( new Apple( Apple.Color.GREEN ) );
			}
			else {
				apples.add( new Apple( Apple.Color.RED ) );
			}
		}
		final List<Apple> greenApples = filter( apples, (Apple apple) -> apple.color == Apple.Color.GREEN );


		assertThat( greenApples.size(), is( 5 ) );
		greenApples.forEach( apple -> assertThat( apple.color, is( Apple.Color.GREEN ) ) );
	}

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		List<T> result = new ArrayList<>();
		for ( T element : list ) {
			if ( predicate.test( element ) ) {
				result.add( element );
			}
		}
		return result;
	}

	public static class Apple {
		enum Color {
			RED,
			GREEN;
		}

		Color color;

		public Apple(Color color) {
			this.color = color;
		}
	}
}
