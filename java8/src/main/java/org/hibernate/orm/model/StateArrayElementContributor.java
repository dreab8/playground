package org.hibernate.orm.model;

/**
 * @author Steve Ebersole
 */
public interface StateArrayElementContributor<J> extends Navigable<J> {
	Object NOT_NULL = new Object();

	int getStateArrayPosition();

	default boolean isIncludedInDirtyChecking() {
		return true;
	}

	@SuppressWarnings("unchecked")
	default J deepCopy(J original) {
		// for now just return a marker
		// 		-- the call and the array access are the important aspect to
		// 			test anyway
		return (J) NOT_NULL;
	}

	void setStateArrayPosition(int stateArrayPosition);
}
