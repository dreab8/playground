package chp7;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Steve Ebersole
 */
public interface NavigableContainer<J> {
	/**
	 * Obtain a List of all Navigables in this container.
	 *
	 * @apiNote When the NavigableContainer is also a {@link InheritanceCapable}
	 * the Navigables returned here are all Navigables for this type
	 * as well as its super types.
	 *
	 * @see InheritanceCapable#getDeclaredNavigables()
	 */
	List<Navigable<?>> getNavigables();

	/**
	 * Obtain a Stream of all Navigables in this container.
	 *
	 * @apiNote When the NavigableContainer is also a {@link InheritanceCapable}
	 * the Navigables contained in this Stream are all Navigables for this type
	 * as well as its super types.
	 *
	 * @see InheritanceCapable#declaredNavigableStream()
	 */
	default <N extends Navigable<?>> Stream<N> navigableStream() {
		return navigableStream( null );
	}

	/**
	 * Obtain a filtered Stream of all Navigables in this container.
	 *
	 * @apiNote When the NavigableContainer is also a {@link InheritanceCapable}
	 * the Navigables contained in this Stream are all Navigables for this type
	 * as well as its super types.
	 *
	 * @see InheritanceCapable#declaredNavigableStream(Class)
	 */
	<N extends Navigable<?>> Stream<N> navigableStream(Class<N> filterType);
}
