package org.hibernate.orm.model;

/**
 * @author Steve Ebersole
 */
public class SimpleIdentifierDescriptor implements IdentifierDescriptor {
	@Override
	public int getNavigablePosition() {
		// the id is always the first Navigable
		return 0;
	}

	@Override
	public void setNavPosition(int navPosition) {

	}
}
