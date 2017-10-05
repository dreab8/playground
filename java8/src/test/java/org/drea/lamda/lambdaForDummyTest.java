package org.drea.lamda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class lambdaForDummyTest {

	@Test
	public void testFirstLamba() {
		String[] words = new String[] {"aaa", "aa", "a"};

		/*
			(String first, String second) -> {Integer.compare( first.length(), second.length() )}
			(String first, String second) -> Integer.compare( first.length(), second.length() )
			(first, second) -> Integer.compare( first.length(), second.length() )

			You can supply a lambda expression whenever an object of an interface with a single abstract method is expected

			such an interface is called functional interface

			functional interfaces can be annotated with @FunctionalInterface

			The Java API defines a number of very generic functional interfaces in the java.util.function package
		*/
		Arrays.sort( words, (first, second) -> Integer.compare( first.length(), second.length() ) );

		assertThat( words[0], is( "a" ) );
		assertThat( words[2], is( "aaa" ) );
	}

	@Test
	public void testMethodReference() {
		/*
			The expression System.out::println is a method reference
			and is equivalent to the lamda expression x -> System.out.println(x)

			there are 3 cases:
				1 - object::instanceMethod, System.out::println is equivalent to x -> System.out.println(x)
				2 - Class::staticMethod, Math::pow is equivalent to (x,y) -> Math.pow(x,y)
				3 - Class::instanceMethod, the String::compareToIgnoreCase is equivalent to ( x, y ) -> x.compareToIgnoreCase( y )
e
				You can also invoke a method of an enclosing class this::equals is x -> this.equals(x), it's also valid super
		 */

		List<String> names = new ArrayList<>();
		names.add( "Fabiana" );
		names.add( "Alberto" );

		// map call Greeting(String) constructor for each element of the names list
		Stream<Greeting> greetingStream = names.stream().map( Greeting::new );

		List<Greeting> greetings = greetingStream.collect( Collectors.toList() );

		assertThat( greetings.get( 0 ).print(), is( "Greeting Fabiana" ) );
		assertThat( greetings.get( 1 ).print(), is( "Greeting Alberto" ) );

//		Arrays.sort( names, Comparator.comparing( String::length ) );
	}

	// ---- Default methods -----

	public class Greeting {
		private String to;

		Greeting(String to) {
			this.to = to;
		}

		String print() {
			return "Greeting " + to;
		}
	}

	interface Person {
		long getId();

		default String getName() {
			return "Pippo";
		}
	}

	interface Named {
		default String getName(){
			return getClass().getName() + "_" + hashCode();
		}
	}

	class Student implements Person, Named{
		@Override
		public long getId() {
			return 0;
		}

		/*
			class Student inherits the same default method getName() from Person and Named interfaces
			to avoid compilation error you have to Override it providing the desired implementation
			this happen even if the Named interface define getName() as non default
				interface Named {
					String getName();
				}
		 */
		@Override
		public String getName(){
			return Person.super.getName();
		}
	}

	// --- Static methods to interfaces ---


}
