package java8inaction.chp4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class FunctionalInterfaceTest {

	@Test
	public void testPredicate() {
		List<Item> items = getItems();

		List<Item> toDoList = filter( items, (Item i) -> !i.isDone() );

		assertThat( toDoList.size(), is( 2 ) );

		List<Item> done = filter( items, (Item i) -> i.isDone() );

		assertThat( done.size(), is( 1 ) );

		List<Item> buyFlights = filter( items, (Item i) -> i.getName().contains( "Buy flight" ) );
		assertThat( buyFlights.size(), is( 2 ) );
	}

	@Test
	public void testConsumer() {
		List<Item> items = getItems();

		final Consumer<Item> itemConsumer = (Item i) -> i.setDone( true );

		foreach( items, itemConsumer );

		assertThat( items.stream().allMatch( i -> i.isDone() ), is( true ) );

		foreach( items, this::setDone );

		final Consumer<Item> itemConsumer2 = i -> i.setName( i.getName() + "_e" );

		foreach( items, itemConsumer.andThen( itemConsumer2 ) );

		assertThat( items.stream().allMatch( i -> i.getName().contains( "_e" ) ), is( true ) );

	}

	@Test
	public void testIntegrate() {
		DoubleFunction<Double> f = x -> x +10;
		final double integrate = integrate( f, 2, 6 );
	}

	public double integrate(DoubleFunction<Double> f, double a, double b) {
		return (f.apply( a ) + f.apply( b )) * (b - a) / 2.0;
	}

	@Test
	public void testFunction() {
		List<Item> items = getItems();

		Function<Item, Boolean> f = (Item i) -> i.isDone();

		final List<Boolean> statusList = map( items, f );

		assertThat( statusList.get( 0 ), is( false ) );
		assertThat( statusList.get( 1 ), is( false ) );
		assertThat( statusList.get( 2 ), is( true ) );

		Function<Boolean, Integer> f1 = i -> {
			if ( i ) {
				return 1;
			}
			else {
				return 0;
			}
		};
		final List<Boolean> intStatusList = map( items, f );

	}

	@Test
	public void testComparator() {
		List<Item> items = getItems();
		items.sort( Comparator.comparing( Item::isDone ).reversed().thenComparing( Item::getName ) );

	}

	private List<Item> getItems() {
		List<Item> items = new ArrayList<>();

		items.add( new Item( 1l, "Buy flight to El Hierro" ) );
		items.add( new Item( 2l, "Gatwick Express" ) );
		items.add( new Item( 3l, "Buy flight to Poland" ) );
		items.get( 2 ).setDone( true );
		return items;
	}

	public void setDone(Item i) {
		i.setDone( true );
	}

	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> results = new ArrayList<>();
		for ( T s : list ) {
			if ( p.test( s ) ) {
				results.add( s );
			}
		}
		return results;
	}

	public static <T> void foreach(List<T> items, Consumer<T> c) {
		for ( T item : items ) {
			c.accept( item );
		}
	}

	public static <T, R> List<R> map(List<T> items, Function<T, R> f) {
		List<R> results = new ArrayList<>();
		for ( T item : items ) {
			results.add( f.apply( item ) );
		}
		return results;

	}

	public static <T, R> Integer sum(List<T> items, Function<T, R> f) {
		Integer result = 0;
		for ( T item : items ) {
			result += (Integer) f.apply( item );
		}
		return result;
	}

	public static <T, R> Integer sum2(List<T> items, ToIntFunction<T> f) {
		Integer result = 0;
		for ( T item : items ) {
			result += f.applyAsInt( item );
		}
		return result;
	}

	public class Item {
		private Long id;
		private String name;
		private boolean done;

		public Item(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isDone() {
			return done;
		}

		public void setDone(boolean done) {
			this.done = done;
		}
	}
}
