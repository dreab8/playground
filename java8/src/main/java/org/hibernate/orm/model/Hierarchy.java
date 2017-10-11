package org.hibernate.orm.model;

/**
 * @author Steve Ebersole
 */
public class Hierarchy {
	private final EntityDescriptor rootEntityDescriptor;
	private final IdentifierDescriptor identifierDescriptor;

	public Hierarchy(
			EntityDescriptor rootEntityDescriptor,
			IdentifierDescriptor identifierDescriptor) {
		this.rootEntityDescriptor = rootEntityDescriptor;
		this.identifierDescriptor = identifierDescriptor;
	}

	public EntityDescriptor getRootEntityDescriptor() {
		return rootEntityDescriptor;
	}

	public IdentifierDescriptor getIdentifierDescriptor() {
		return identifierDescriptor;
	}
}
