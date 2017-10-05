package chp1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Andrea Boriero
 */
public class Apple {
	private String color;
	private int weight;

	public static List<Apple> filterGreenApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<>();
		for ( Apple apple : inventory ) {
			if ( "green".equals( apple.getColor() ) ) {
				result.add( apple );
			}
		}
		return result;
	}

	public static List<Apple> filterHeavyApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<>();
		for ( Apple apple : inventory ) {
			if ( apple.getWeight() > 150 ) {
				result.add( apple );
			}
		}
		return result;
	}

	public String getColor() {
		return color;
	}

	public int getWeight() {
		return weight;
	}

	// Java 8 style

	public static boolean isGreenApple(Apple apple) {
		return "green".equals( apple.getColor() );
	}

	public static boolean isHeavyApple(Apple apple) {
		return apple.getWeight() > 150;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/*
	public interface Predicate<T> {
		boolean test(T t);
	}

	The word predicate is often used in mathematics to mean something function-like that takes a value for an argument
	and returns true or false. As you’ll see later, Java 8 would also allow you to write Function<Apple,Boolean> but
	using Predicate<Apple> is more standard (and slightly more efficient because it avoids boxing a boolean into a Boolean).
	 */
	// filterApples(inventory, Apple::isGreenApple);
	// filterApples(inventory, Apple::isHeavyApple);
	public static List<Apple> filterApples(
			List<Apple> inventory,
			Predicate<Apple> p) { // A method is passed as a Predicate parameter named p
		List<Apple> result = new ArrayList<>();
		for ( Apple apple : inventory ) {
			if ( p.test( apple ) ) {
				result.add( apple );
			}
		}
		return result;
	}

}
