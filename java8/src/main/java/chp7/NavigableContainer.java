package chp7;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Steve Ebersole
 */
public interface NavigableContainer<J> {
	List<Navigable<?>> getNavigables();

	<N extends Navigable<?>> List<N> getNavigables(Class<N> filterType);

	@SuppressWarnings("unchecked")
	default <N extends Navigable<?>> Spliterator<N> navigableSource() {
		return navigableSource( null );
	}

	@SuppressWarnings("unchecked")
	<N extends Navigable<?>> Spliterator<N> navigableSource(Class<N> filterType);

	default <N extends Navigable<?>> Stream<N> navigableStream() {
		return navigableStream( null );
	}

	default <N extends Navigable<?>> Stream<N> navigableStream(Class<N> filterType) {
		return StreamSupport.stream( navigableSource( filterType ), false );
	}

}
