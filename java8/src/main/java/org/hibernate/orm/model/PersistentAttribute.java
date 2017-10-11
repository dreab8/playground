package org.hibernate.orm.model;

/**
 * @author Steve Ebersole
 */
public interface PersistentAttribute<J> extends StateArrayElementContributor<J> {
	String getAttributeInfo();
}
