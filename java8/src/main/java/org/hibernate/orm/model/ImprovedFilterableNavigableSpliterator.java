package org.hibernate.orm.model;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Steve Ebersole
 */
public class ImprovedFilterableNavigableSpliterator<N extends Navigable<?>> implements Spliterator<N> {
	public static final int CHARACTERISTICS = DISTINCT & NONNULL & IMMUTABLE;

	private final List<Navigable<?>> listOfNavigables;
	private final Class<N> filterType;

	private Iterator iterator;
	private boolean reachedEnd;

	public ImprovedFilterableNavigableSpliterator(
			List<Navigable<?>> listOfNavigables,
			Class<N> filterType) {
		this.listOfNavigables = listOfNavigables;
		this.filterType = filterType;
	}

	@Override
	public boolean tryAdvance(Consumer<? super N> action) {
//		System.out.println("try advance");
		if ( reachedEnd ) {
			// should indicate we are past the end of all sub-spliterators
			return false;
		}

		if ( iterator == null ) {
			iterator = listOfNavigables.iterator();
		}

		return internalTryAdvance( action );
	}

	@SuppressWarnings("PointlessBooleanExpression")
	private boolean internalTryAdvance(Consumer<? super N> action) {
		final N nextMatch = findNextMatch();

		if ( reachedEnd ) {
			assert nextMatch == null;
			return false;
		}

		action.accept( nextMatch );

		return true;
	}

	@SuppressWarnings("unchecked")
	private N findNextMatch() {
		assert iterator != null;

		while ( iterator.hasNext() ) {
			final N next = (N) iterator.next();
			if ( filterType == null || filterType.isInstance( next ) ) {
				return next;
			}
		}

		// if we get here, the iterator is finished - `! hasNext()`
		reachedEnd = true;
		iterator = null;

		return null;
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
