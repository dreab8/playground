/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package chp7;

import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @author Andrea Boriero
 */
public abstract class AbstractNavigableSpliterator<T extends Navigable> implements Spliterator<T> {
	public static final int CHARACTERISTICS = SIZED
			& SORTED
			& DISTINCT
			& NONNULL
			& IMMUTABLE;
//	public static final int CHARACTERISTICS = ORDERED
//			& SORTED
//			& DISTINCT
//			// todo (6.0) : see note in `#navigableStream` - & Spliterator.SIZED
//			& NONNULL
//			& IMMUTABLE
//			& CONCURRENT;

	private final Stack<InheritanceCapable> topDownHierarchy;
	private final int size;

	private Spliterator<T> currentSpliterator;

	private boolean reachedEnd;

	public AbstractNavigableSpliterator(InheritanceCapable container, boolean includeSuperNavigables) {
		this.topDownHierarchy = new Stack<>();

		this.size = addToStack( container, includeSuperNavigables );
	}

	private int addToStack(InheritanceCapable container, boolean includeSuperNavigables) {
		int count = container.getDeclaredAttributes().size();

		topDownHierarchy.push( container );

		final InheritanceCapable superType = container.getSuperclassType();
		if ( includeSuperNavigables && superType != null ) {
			count+= superType.getDeclaredAttributes().size();
			addToStack( superType, includeSuperNavigables );
		}

		return count;
	}


	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
//		System.out.println("try advance");
		if ( reachedEnd ) {
			return false;
		}

		if ( currentSpliterator == null ) {
			currentSpliterator = nextSpliterator();

			if ( currentSpliterator == null ) {
				return false;
			}
		}

		return internalTryAdvance( action );
	}

	private Spliterator<T> nextSpliterator() {
		if ( topDownHierarchy.isEmpty() ) {
			reachedEnd = true;
			return null;
		}

//		System.out.println( "Popping hierarchy for next Spliterator" );

		return nextSpliterator( topDownHierarchy.pop() );
	}

	protected abstract Spliterator<T> nextSpliterator(InheritanceCapable container);

	private boolean internalTryAdvance(Consumer<? super T> action) {
		boolean reply = currentSpliterator.tryAdvance( action );

		if ( reply == false ) {
			while ( !reachedEnd && reply == false ) {
				// see if there is another Spliterator left
				currentSpliterator = nextSpliterator();
				if ( currentSpliterator == null ) {
					assert reachedEnd;
					return false;
				}
				reply = currentSpliterator.tryAdvance( action );
			}
		}

		return reply;
	}

	@Override
	public Spliterator<T> trySplit() {
//		System.out.println("try split");
		if ( topDownHierarchy.isEmpty() ) {
			return null;
		}

		return nextSpliterator();
	}

	@Override
	public long estimateSize() {
		// todo (6.0) : the container should be able to pass this to us eventually
		//		the idea being that we could calculate and store this as part
		//		of finalizing the runtime model.  at that time the hierarchy would
		// 		know all of its declared attributes at each level.
		return size;
	}

	@Override
	public long getExactSizeIfKnown() {
		return size;
	}

	@Override
	public int characteristics() {
		return CHARACTERISTICS;
	}
}
