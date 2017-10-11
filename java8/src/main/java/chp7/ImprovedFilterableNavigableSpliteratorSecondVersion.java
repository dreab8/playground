package chp7;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Steve Ebersole
 */
public class ImprovedFilterableNavigableSpliteratorSecondVersion<N extends Navigable<?>> implements Spliterator<N> {
	public static final int CHARACTERISTICS = DISTINCT & NONNULL & IMMUTABLE;

	private final List<Navigable<?>> listOfNavigables;
	private final Class<N> filterType;

	private Iterator iterator;

	public ImprovedFilterableNavigableSpliteratorSecondVersion(
			List<Navigable<?>> listOfNavigables,
			Class<N> filterType) {
		this.listOfNavigables = listOfNavigables;
		this.filterType = filterType;
		iterator = listOfNavigables.iterator();
	}

	@Override
	public boolean tryAdvance(Consumer<? super N> action) {

		while ( iterator.hasNext() ) {
			final N next = (N) iterator.next();
			if ( filterType == null || filterType.isInstance( next ) ) {
				action.accept( next );
				return true;
			}
		}

		iterator = null;
		return false;
	}

	@Override
	public Spliterator<N> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return listOfNavigables.size();
	}

	@Override
	public int characteristics() {
		return CHARACTERISTICS;
	}
}
