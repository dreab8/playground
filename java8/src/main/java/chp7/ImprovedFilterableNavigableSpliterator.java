package chp7;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @author Steve Ebersole
 */
public class ImprovedFilterableNavigableSpliterator<N extends Navigable<?>> implements Spliterator<N> {
	private final Stack<SubSpliterator> topDownNavSpliteratorStack;

	private final long estimatedSize;

	private SubSpliterator currentSubSpliterator;
	private boolean reachedEnd;

	public ImprovedFilterableNavigableSpliterator(
			InheritanceCapable container,
			Class<N> filterType,
			boolean includeSuperNavigables) {
		this.topDownNavSpliteratorStack = new Stack<>();

		this.estimatedSize = addToStack( container, filterType, includeSuperNavigables );
	}

	private long addToStack(
			InheritanceCapable<?> container,
			Class<N> filterType,
			boolean includeSuperNavigables) {
		final SubSpliterator subSpliterator = topDownNavSpliteratorStack.push( new SubSpliterator( container, filterType ) );
		long estimatedSize = subSpliterator.estimateSize();

		final InheritanceCapable<?> superType = container.getSuperclassType();
		if ( includeSuperNavigables && superType != null ) {
			estimatedSize += addToStack( superType, filterType, true );
		}

		return estimatedSize;
	}

	@Override
	public boolean tryAdvance(Consumer<? super N> action) {
//		System.out.println("try advance");
		if ( reachedEnd ) {
			// should indicate we are past the end of all sub-spliterators
			return false;
		}

		if ( currentSubSpliterator == null ) {
			// should indicate the first pass
			currentSubSpliterator = nextSubSpliterator();

			if ( currentSubSpliterator == null ) {
				return false;
			}
		}

		return internalTryAdvance( action );
	}

	private SubSpliterator nextSubSpliterator() {
		if ( topDownNavSpliteratorStack.isEmpty() ) {
			reachedEnd = true;
			return null;
		}

//		System.out.println( "Popping Stack element for next Spliterator" );

		return topDownNavSpliteratorStack.pop();
	}

	@SuppressWarnings("PointlessBooleanExpression")
	private boolean internalTryAdvance(Consumer<? super N> action) {
		boolean reply = currentSubSpliterator.tryAdvance( action );

		if ( reply == false ) {
			while ( !reachedEnd && reply == false ) {
				// see if there is another Spliterator left
				currentSubSpliterator = nextSubSpliterator();
				if ( currentSubSpliterator == null ) {
					assert reachedEnd;
					return false;
				}
				reply = currentSubSpliterator.tryAdvance( action );
			}
		}

		return reply;
	}

	@Override
	public Spliterator<N> trySplit() {
//		System.out.println("try split");
		if ( topDownNavSpliteratorStack.isEmpty() ) {
			return null;
		}

		return nextSubSpliterator();
	}

	@Override
	public long estimateSize() {
		return estimatedSize;
	}

	@Override
	public long getExactSizeIfKnown() {
		return estimatedSize;
	}

	public static final int CHARACTERISTICS = SORTED
			& DISTINCT
			& NONNULL
			& IMMUTABLE;

	@Override
	public int characteristics() {
		return CHARACTERISTICS;
	}

	public class SubSpliterator implements Spliterator<N> {
		private final InheritanceCapable container;
		private final Class<N> filterType;

		private final int sizeEstimate;

		private Iterator<N> iterator;
		private boolean reachedEnd;

		public SubSpliterator(InheritanceCapable container, Class<N> filterType) {
			this.container = container;
			this.filterType = filterType;

			this.sizeEstimate = container.getDeclaredNavigables().size();
		}

		@Override
		public boolean tryAdvance(Consumer<? super N> action) {
			if ( reachedEnd ) {
				// should indicate we are past the end of all sub-spliterators
				return false;
			}

			if ( iterator == null ) {
				iterator = container.getDeclaredNavigables().iterator();
			}

			final N nextMatch = findNextMatch();

			if ( reachedEnd ) {
				assert nextMatch == null;
				return false;
			}

			action.accept( nextMatch );

			return true;
		}

		private N findNextMatch() {
			assert iterator != null;

			while ( iterator.hasNext() ) {
				final N next = iterator.next();
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
			return sizeEstimate;
		}

		@Override
		public int characteristics() {
			return SUB_CHARACTERISTICS;
		}

		public static final int SUB_CHARACTERISTICS = SORTED
				& DISTINCT
				& NONNULL
				& IMMUTABLE;

	}
}
