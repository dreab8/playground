package org.hibernate.orm.model;

/**
 * @author Steve Ebersole
 */
public class SimpleIdentifierDescriptor implements IdentifierDescriptor {
	@Override
	public int getNavigablePosition() {
		return 0;
	}
}
